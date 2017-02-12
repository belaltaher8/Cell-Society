package cs.view;

import java.io.File;
import java.util.ResourceBundle;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;
import cs.configuration.configs.FireDoc;
import cs.configuration.configs.PredatorPreyDoc;
import cs.configuration.configs.SegregationDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.sims.FireSpreadSim;
import cs.model.sims.GameOfLifeSim;
import cs.model.sims.SegregationSim;
import cs.model.sims.PredatorPreySim;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class GUIController {
	public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
	public static final String DATA_FILE_EXTENSION = "*.xml";

	public static final double SPEED_UP_FACTOR = 2.0;
	public static final double DEFAULT_ANIMATION_SPEED = 250;
	
	public static final int SCENE_WIDTH = 600; 
	public static final int SCENE_HEIGHT = 800; 
	public static final int CONTROLS_HEIGHT = 200; 
	
	public static final int GRAPH_WIDTH = 600; 
	public static final int GRAPH_HEIGHT = 200; 
	
	private ResourceBundle myResources; 
	
	private XMLReader myXMLReader;
	private ConfigDoc myConfigDoc;
	private Simulation mySimulation;
	private GridDisplay myGridDisplay; 
	private ControlDisplay myControlDisplay;
	private GraphDisplay myGraphDisplay; 
	
	private Chart myGraph; 
	
	private Timeline animation; 
	private Stage myStage;
	private Stage graphStage; 
	private Pane myGridPane;
	private Pane myControlPane;
	private double animationSpeed; 
	
	private int stepCount; 

	public GUIController(Stage primaryStage){
		myStage = primaryStage;
		animationSpeed = DEFAULT_ANIMATION_SPEED; 
		configureAnimation();
	
		resetAll();
		
		graphStage = new Stage();
		Scene graphScene = configureGraphScene(); 
		graphStage.setTitle("Graph"); // GET FROM RESOURCE BUNDLE 
		graphStage.setScene(graphScene);
		graphStage.show();
		
		stepCount = 0; 
		
		Scene mainScene = configureScene();
		primaryStage.setTitle(myConfigDoc.getSimulationName());
		primaryStage.setScene(mainScene);
		primaryStage.show();
	} 
	
	private Scene configureGraphScene() {
		Group graphView = new Group(); 
		graphView.getChildren().add(myGraph);
		Scene graph = new Scene(graphView, GRAPH_WIDTH, GRAPH_HEIGHT);
		return graph;	
	}

	private void resetAll() {
		try {
			File xmlFile = promptForFile(myStage);
			myXMLReader = makeXMLReader(xmlFile);
			myConfigDoc = makeConfigDoc(myXMLReader);
			mySimulation = makeSimulation(myConfigDoc);
			myGridDisplay = new GridDisplay(mySimulation, myConfigDoc);
			myControlDisplay = new ControlDisplay(myConfigDoc, this);
			myGraphDisplay = new GraphDisplay(myGridDisplay);
			myGraph = myGraphDisplay.getChart();
		} catch(XMLException e) {
			resetAll();
		}
	}
	
	private File promptForFile(Stage primaryStage) {
		FileChooser myChooser = new FileChooser();
		myChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/"));
		myChooser.getExtensionFilters().setAll(new ExtensionFilter("Text Files", DATA_FILE_EXTENSION));
		File dataFile = myChooser.showOpenDialog(primaryStage);
		if(dataFile != null) {
			return dataFile;
		} else {
			//TODO: clean this up
			ButtonType response = alertAndWait("Error","No file selected. Press OK to choose another file.");
	        if(response == ButtonType.OK) {
	        	return promptForFile(primaryStage);
	        } else {
	        	//TODO: solve this in a better way
	        	//return new File(System.getProperty("user.dir") + "/data/game_of_life.xml");
	        	System.exit(0);
	        	return null;
	        }
		}
	}
	
	private XMLReader makeXMLReader(File file) throws XMLException {
		try {
			return new XMLReader(file);
		} catch(XMLException e) {
			//TODO: use resource pack
			alertAndWait("Error",  String.format("Error constructing the XML parser: %s", e.getMessage()));
	        throw e;
		}
	}
	
	private ConfigDoc makeConfigDoc(XMLReader reader) throws XMLException {
		try {
			return reader.getConfigDoc();
		} catch(XMLException e) {
			//TODO: use resource pack
			alertAndWait("Error", String.format("Error parsing the XML configuration parameters : %s", e.getMessage()));
			throw e;
		}
	}
	
	private Simulation makeSimulation(ConfigDoc config) throws XMLException {
		//ugly if-statement to choose what kind of Simulation to return
		if(config.getSimType().equals(ConfigDoc.SIM_TYPE_GAME_OF_LIFE)) {
			return new GameOfLifeSim(config);
		} else if(config.getSimType().equals(ConfigDoc.SIM_TYPE_FIRE_SPREAD)) {
			return new FireSpreadSim((FireDoc)config);
		} else if(config.getSimType().equals(ConfigDoc.SIM_TYPE_SEGREGATION)) {
			return new SegregationSim((SegregationDoc)config);
		} else if(config.getSimType().equals(ConfigDoc.SIM_TYPE_PRED_PREY)) {
			return new PredatorPreySim((PredatorPreyDoc)config); 
		} else {
			//TODO: use resource pack
			alertAndWait("Error", "Invalid simulation type specified in the XML input file.");
	        throw new XMLException("Invalid simulation type specified in the XML input file.");
		}
	}

	private void configureAnimation() {
		animation = new Timeline();
		animation.getKeyFrames().clear();
		animation.setCycleCount(Timeline.INDEFINITE);
		KeyFrame frame = new KeyFrame(Duration.millis(animationSpeed), e->stepAnimation());
		animation.getKeyFrames().add(frame);
	}
	
	private Scene configureScene() {
		myGridPane = configureDisplay();
		myControlPane = configureControls();
		
		BorderPane mainView = new BorderPane();
		mainView.setCenter(myGridPane);
		mainView.setBottom(myControlPane);
		
		Group sceneRoot = new Group(); 
		sceneRoot.getChildren().add(mainView);
		return new Scene(sceneRoot, SCENE_WIDTH, SCENE_HEIGHT);
	}
	

	private Pane configureDisplay(){
		Pane gridPane = new Pane();
		gridPane.setPrefSize(GridDisplay.DISPLAY_WIDTH, GridDisplay.DISPLAY_HEIGHT);
		gridPane.getChildren().add(myGridDisplay.getGridView()); 
		return gridPane;
	}

	private Pane configureControls() {
		Pane controlPane = new Pane();
		controlPane.setPrefSize(SCENE_WIDTH, CONTROLS_HEIGHT);
		controlPane.getChildren().add(myControlDisplay.getControlView());
		return controlPane;
	}

	private ButtonType alertAndWait(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR, message ,ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(title);
        return alert.showAndWait().get();
	}
	
	public void loadNewFile() {
		System.out.println("hi");
		animation.stop();
		animationSpeed = DEFAULT_ANIMATION_SPEED;
		myGridPane.getChildren().remove(myGridDisplay.getGridView()); 
		resetAll();
		myGridPane.getChildren().add(myGridDisplay.getGridView()); 
	}
	
	public void saveSnapshot() {
		myXMLReader.writeToFile(myConfigDoc.getParamsAsXML(), myConfigDoc.getNeighborsAsXML(), mySimulation.getContentsAsXML());
	}
	
	public void randomizeGrid() {
		animation.stop();
		myGridDisplay.randomizeGrid();
	}
	
	public void resetGrid() {
		animation.stop();
		animationSpeed = DEFAULT_ANIMATION_SPEED;
		configureAnimation();
		myGridDisplay.reset();
	}
	
	public void speedAnimation() {
		boolean wasPlaying = animation.getCurrentRate() > 0;
		animation.pause();
		animationSpeed = myControlDisplay.getAnimationSpeed() * -10 + 1050;
		configureAnimation();
		if(wasPlaying) {
			animation.play();
		}
	}
	
	public void updateParameters(int width, int height) {
		myConfigDoc.setGridWidth(width);
		myConfigDoc.setGridHeight(height);
		mySimulation.reset();
		myGridDisplay.reset();
	}
	
	public void stepAnimation() {
		stepCount++; 
		myGridDisplay.step();
		myGraphDisplay = new GraphDisplay(myGridDisplay);
		myGraph = myGraphDisplay.getChart();
	}

	public void stopAnimation() {
		animation.pause();
	}

	public void animate() {
		animation.play();
	}
}