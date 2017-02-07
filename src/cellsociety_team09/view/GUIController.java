package cellsociety_team09.view;

import java.io.File;
import java.util.ResourceBundle;
import cellsociety_team09.configuration.XMLReader;
import cellsociety_team09.model.Grid;
import cellsociety_team09.model.MovingGrid;
import cellsociety_team09.model.PredatorGrid;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	private GridDisplay societyView; 
	private Grid myGrid;
	
	private Timeline animation; 
	private Stage myStage;
	private Pane gridPane;
	private double animationSpeed; 

	public GUIController(Stage primaryStage){
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "ButtonLabels");
		myStage = primaryStage;
		animationSpeed = DEFAULT_ANIMATION_SPEED; 
		configureAnimation();
		
		promptForFile(primaryStage);
		makeSimulation();
		
		Scene mainScene = configureScene();
		primaryStage.setTitle(myXMLReader.getSimulationName());
		primaryStage.setScene(mainScene);
		primaryStage.show();
	} 
	
	private void promptForFile(Stage primaryStage) {
		FileChooser myChooser = new FileChooser();
		myChooser.setTitle("Open Data File"); /// string
		myChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data")); // string 
		myChooser.getExtensionFilters().setAll(new ExtensionFilter("Text Files", DATA_FILE_EXTENSION)); // string 
		File dataFile = myChooser.showOpenDialog(primaryStage);
		if(dataFile != null) {
			myXMLReader = new XMLReader(dataFile);
		} else {
			//throw new Exception
		}
	}
	
	private void makeSimulation() {
		if(myXMLReader.getCellType().equals("Cell")) {
			myGrid = new Grid(myXMLReader);
		} else if(myXMLReader.getCellType().equals("MovingCell")) {
			myGrid = new MovingGrid(myXMLReader);
		} else if(myXMLReader.getCellType().equals("PredatorPrey")) {
			myGrid = new PredatorGrid(myXMLReader); 
		}
		societyView = new GridDisplay(myGrid);
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
		gridPane.getChildren().add(societyView.getGridView()); 
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


	private void loadNewFile() {
		animation.stop();
		animationSpeed = DEFAULT_ANIMATION_SPEED;
		gridPane.getChildren().remove(societyView.getGridView()); 
		promptForFile(myStage);
		makeSimulation();
		gridPane.getChildren().add(societyView.getGridView()); 
	}
	
	private void randomizeGrid() {
		animation.stop();
		societyView.randomizeGrid();
	}
	
	private void resetGrid() {
		animation.stop();
		animationSpeed = DEFAULT_ANIMATION_SPEED;
		societyView.reset();
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
		societyView.step();
	}

	private void stopAnimation() {
		animation.pause();
	}

	private void animate() {
		animation.play();
	}
}