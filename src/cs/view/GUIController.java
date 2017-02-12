package cs.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;
import cs.configuration.configs.PredatorPreyDoc;
import cs.model.Simulation;
import cs.model.sims.MovingSim;
import cs.model.sims.PredatorPreySim;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class GUIController{
	public static final String DATA_FILE_EXTENSION = "*.xml";
	public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";

	public static final double SPEED_UP_FACTOR = 2.0;
	public static final double DEFAULT_ANIMATION_SPEED = 250;
	
	public static final int SCENE_WIDTH = 600; 
	public static final int SCENE_HEIGHT = 800; 
	public static final int CONTROLS_HEIGHT = 200; 
	
	private ResourceBundle myResources; 
	
	private XMLReader myXMLReader;
	private ConfigDoc myConfigDoc;
	private Simulation mySimulation;
	private GridDisplay mySocietyView; 
	
	private Timeline animation; 
	private Stage myStage;
	private Pane gridPane;
	private double animationSpeed; 

	public GUIController(Stage primaryStage){
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "GUI");
		myStage = primaryStage;
		animationSpeed = DEFAULT_ANIMATION_SPEED; 
		configureAnimation();
		
		resetAll();
		
		Scene mainScene = configureScene();
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
			mySocietyView = new GridDisplay(mySimulation);
		} catch(XMLException e) {
			resetAll();
		}
	}
	
	private File promptForFile(Stage primaryStage) {
		FileChooser myChooser = new FileChooser();
		myChooser.setTitle(myResources.getString("ChooseFilePrompt"));
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
		if(config.getSimType().equals(ConfigDoc.SIM_TYPE_DEFAULT)) {
			return new Simulation(config);
		} else if(config.getSimType().equals(ConfigDoc.SIM_TYPE_MOVING)) {
			return new MovingSim(config);
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
		Pane gridPane = configureDisplay();
		HBox controlPane = configureControls();
		
		BorderPane mainView = new BorderPane();
		mainView.setCenter(gridPane);
		mainView.setBottom(controlPane);
		
		Group sceneRoot = new Group(); 
		sceneRoot.getChildren().add(mainView);
		return new Scene(sceneRoot, SCENE_WIDTH, SCENE_HEIGHT);
	}
	
	private Pane configureDisplay(){
		gridPane = new Pane();
		gridPane.setPrefSize(GridDisplay.DISPLAY_WIDTH, GridDisplay.DISPLAY_HEIGHT);
		gridPane.getChildren().add(mySocietyView.getGridView()); 
		return gridPane;
	}

	private HBox configureControls() {
		HBox controlPane = new HBox();
		controlPane.setPrefSize(SCENE_WIDTH, CONTROLS_HEIGHT);
		
		Button startButton = new Button(myResources.getString("StartLabel"));
		startButton.setOnMouseClicked(e->animate());
		
		Button stopButton = new Button(myResources.getString("StopLabel"));
		stopButton.setOnMouseClicked(e->stopAnimation());
		
		Button stepButton = new Button(myResources.getString("StepLabel"));
		stepButton.setOnMouseClicked(e->stepAnimation());
		
		Button resetButton = new Button(myResources.getString("ResetLabel"));
		resetButton.setOnMouseClicked(e->resetGrid());
		
		Button randomizeButton = new Button(myResources.getString("RandomLabel"));
		randomizeButton.setOnMouseClicked(e->randomizeGrid());
		
		Button loadButton = new Button(myResources.getString("LoadLabel"));
		loadButton.setOnMouseClicked(e->loadNewFile());
		
		Button speedButton = new Button(myResources.getString("Speed"));
		speedButton.setOnMouseClicked(e->speedAnimation(animationSpeed/SPEED_UP_FACTOR));
		
		Button slowButton = new Button(myResources.getString("Slow"));
		slowButton.setOnMouseClicked(e->speedAnimation(animationSpeed*SPEED_UP_FACTOR));
		
		controlPane.getChildren().addAll(startButton,stopButton,stepButton,resetButton,
				randomizeButton,loadButton, speedButton, slowButton);
		
		return controlPane;
	}

	private ButtonType alertAndWait(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR, message ,ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(title);
        return alert.showAndWait().get();
	}
	
	private void loadNewFile() {
		animation.stop();
		animationSpeed = DEFAULT_ANIMATION_SPEED;
		gridPane.getChildren().remove(mySocietyView.getGridView()); 
		resetAll();
		gridPane.getChildren().add(mySocietyView.getGridView()); 
	}
	
	private void randomizeGrid() {
		animation.stop();
		mySocietyView.randomizeGrid();
	}
	
	private void resetGrid() {
		animation.stop();
		animationSpeed = DEFAULT_ANIMATION_SPEED;
		mySocietyView.reset();
	}
	
	private void speedAnimation(double newSpeed) {
		boolean wasPlaying = animation.getCurrentRate() > 0;
		animation.pause();
		animationSpeed = newSpeed; 
		configureAnimation();
		if(wasPlaying) {
			animation.play();
		}
	}
	
	private void stepAnimation() {
		mySocietyView.step();
	}

	private void stopAnimation() {
		animation.pause();
		//myXMLReader.writeToFile(myConfigDoc.getParamsAsXML(), myConfigDoc.getRuleAsXML(), mySimulation.getContentsAsXML());
	}

	private void animate() {
		animation.play();
	}
}