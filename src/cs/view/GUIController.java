package cs.view;

import java.io.File;
import java.util.ResourceBundle;
import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;
import cs.configuration.configs.FireDoc;
import cs.configuration.configs.PredatorPreyDoc;
import cs.configuration.configs.SegregationDoc;
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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class GUIController {
	public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
	public static final String DATA_FILE_EXTENSION = "*.xml";

	public static final double SPEED_UP_FACTOR = -3.0;
	public static final double SPEED_UP_MAX_DELAY = 350;
	public static final double DEFAULT_ANIMATION_SPEED = 275;
	
	public static final int SCENE_WIDTH = 600; 
	public static final int SCENE_HEIGHT = 800; 
	public static final int CONTROLS_HEIGHT = 200; 
	public static final int GRAPH_WIDTH = 600; 
	public static final int GRAPH_HEIGHT = 400; 
	
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
	private Group graphView;
	private Pane myGridPane;
	private Pane myControlPane;
	
	private double animationSpeed; 
	private int stepCount; 

	public GUIController(Stage primaryStage){
		myStage = primaryStage;
		Stage graphStage = new Stage();
		myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + "GUI");	
		animationSpeed = DEFAULT_ANIMATION_SPEED; 
		stepCount = 0; 
		
		configureAnimation();
		resetAll();
		
		Scene graphScene = configureGraphScene(); 
		graphStage.setTitle(myResources.getString("GraphTitle")); // GET FROM RESOURCE BUNDLE 
		graphStage.setScene(graphScene);
		graphStage.show();
		
		Scene mainScene = configureMainScene();
		primaryStage.setTitle(myConfigDoc.getSimulationName());
		primaryStage.setScene(mainScene);
		primaryStage.show();
	} 

	private void resetAll() {
		try {
			File xmlFile = promptForFile(myStage);
			myXMLReader = makeXMLReader(xmlFile);
			myConfigDoc = makeConfigDoc(myXMLReader);
			mySimulation = makeSimulation(myConfigDoc);
			myGridDisplay = makeGridDisplay(mySimulation, myConfigDoc);
			myControlDisplay = makeControlDisplay(myConfigDoc, this);
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
			ButtonType response = alertAndWait(myResources.getString("ErrorTitle"),myResources.getString("ErrorNoFile"));
	        if(response == ButtonType.OK) {
	        	return promptForFile(primaryStage);
	        } else {
	        	System.exit(0);
	        	return null;
	        }
		}
	}
	
	private XMLReader makeXMLReader(File file) throws XMLException {
		try {
			return new XMLReader(file);
		} catch(XMLException e) {
			alertAndWait(myResources.getString("ErrorTitle"),  String.format(myResources.getString("ErrorReader"), e.getMessage()));
	        throw e;
		}
	}
	
	private ConfigDoc makeConfigDoc(XMLReader reader) throws XMLException {
		try {
			return reader.getConfigDoc();
		} catch(XMLException e) {
			alertAndWait(myResources.getString("ErrorTitle"), String.format(myResources.getString("ErrorConfig"), e.getMessage()));
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
			alertAndWait(myResources.getString("ErrorTitle"), myResources.getString("ErrorSim"));
	        throw new XMLException(myResources.getString("ErrorSim"));
		}
	}
	
	private GridDisplay makeGridDisplay(Simulation sim, ConfigDoc config) throws XMLException {
		if(config.getGridShape().equals(ConfigDoc.GRID_SHAPE_SQUARE)){
			return new GridDisplay(sim, config);
		} else if(config.getGridShape().equals(ConfigDoc.GRID_SHAPE_TRIANGLE)) {
			return new TriangleDisplay(sim,config);
		} else {
			alertAndWait(myResources.getString("ErrorTitle"), myResources.getString("ErrorDisplay"));
	        throw new XMLException(myResources.getString("ErrorDisplay"));
		}
	}
	
	private ControlDisplay makeControlDisplay(ConfigDoc config, GUIController controller){
		return new ControlDisplay(config, controller);
	}

	private void configureAnimation() {
		animation = new Timeline();
		animation.getKeyFrames().clear();
		animation.setCycleCount(Timeline.INDEFINITE);
		KeyFrame frame = new KeyFrame(Duration.millis(animationSpeed), e->stepAnimation());
		animation.getKeyFrames().add(frame);
	}
	
	private Scene configureGraphScene() {
		graphView = new Group(); 
		graphView.getChildren().add(myGraph);
		Scene graph = new Scene(graphView, GRAPH_WIDTH, GRAPH_HEIGHT);
		return graph;	
	}
	
	private Scene configureMainScene() {
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
	
	private void detachGridDisplay() {
		myGridPane.getChildren().remove(myGridDisplay.getGridView()); 
	}
	private void attachGridDisplay() {
		myGridPane.getChildren().add(myGridDisplay.getGridView()); 
	}
	private void detachControlsDisplay() {
		myControlPane.getChildren().remove(myControlDisplay.getControlView());
	}
	private void attachControlsDisplay() {
		myControlPane.getChildren().add(myControlDisplay.getControlView());
	}
	
	public void loadNewFile() {
		animation.stop();
		animationSpeed = DEFAULT_ANIMATION_SPEED;
		detachGridDisplay();
		detachControlsDisplay();
		resetAll();
		attachGridDisplay();
		attachControlsDisplay();
	}
	
	public void saveSnapshot() {
		try {
			myXMLReader.writeToFile(myConfigDoc.getParamsAsXML(), myConfigDoc.getNeighborsAsXML(), mySimulation.getContentsAsXML());
		} catch(XMLException e) {
			alertAndWait(myResources.getString("ErrorTitle"), String.format(myResources.getString("ErrorIO"), e.getMessage()));
		}
	}
	
	public void randomizeGrid() {
		animation.pause();
		mySimulation.randomizeGrid();
		myGridDisplay.drawGridDisplay();
	}
	
	public void resetGrid() {
		animation.pause();
		mySimulation.reset();
		myGridDisplay.drawGridDisplay();
	}
	
	public void speedAnimation(double value) {
		boolean wasPlaying = animation.getCurrentRate() > 0;
		animation.pause();
		animationSpeed = value * SPEED_UP_FACTOR + SPEED_UP_MAX_DELAY;
		configureAnimation();
		if(wasPlaying) {
			animation.play();
		}
	}
	
	public void updateWidth(int width) {
		myConfigDoc.setGridWidth(width);
		mySimulation.buildGrid();
		myGridDisplay.drawGridDisplay();
	}
	
	public void updateHeight(int height) {
		myConfigDoc.setGridHeight(height);
		mySimulation.buildGrid();
		myGridDisplay.drawGridDisplay();
	}
	
	public void updateGridEdgeType(String type) {
		myConfigDoc.setGridEdge(type);
	}
	
	public void updateGridShapeType(String shape) {
		myConfigDoc.setGridShape(shape);
		try {
			detachGridDisplay();
			myGridDisplay = makeGridDisplay(mySimulation, myConfigDoc);
			attachGridDisplay();
		} catch(XMLException e) {
			/*Ignore. Since this value is set by a ComboBox, it's impossible to specify an invalid shape */
			throw new RuntimeException(e);
		}
	}
	
	public void toggleGridLines(boolean show) {
		myConfigDoc.setGridLines(show);
		myGridDisplay.drawGridDisplay();
	}
	
	public void stepAnimation() {
		stepCount++; 
		myGridDisplay.step();
		myGraphDisplay.createGraph(myGridDisplay, stepCount);
		graphView.getChildren().remove(myGraph);
		myGraph = myGraphDisplay.getChart();
		graphView.getChildren().add(myGraph);
	}

	public void stopAnimation() {
		animation.pause();
	}

	public void animate() {
		animation.play();
	}
}