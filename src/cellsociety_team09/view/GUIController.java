package cellsociety_team09.view;

import java.util.ResourceBundle;
import cellsociety_team09.configuration.XMLReader;
import cellsociety_team09.model.Grid;
import cellsociety_team09.model.MovingGrid;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUIController{
	private final int sceneWidth = 600; 
	private final int sceneHeight = 800; 
	private final int controlY = 200; 
	public final int gridY = 600;
	public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
	
	private ResourceBundle myResources; 
	
	private XMLReader myXMLReader;
	private GridDisplay societyView; 
	private Grid myGrid;
	
	private Timeline animation; 

	public GUIController(XMLReader reader, Stage primaryStage){
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "ButtonLabels");
		myXMLReader = reader;
		
		makeSimulation();
		configureAnimation();
		
		Scene mainScene = configureScene();
		primaryStage.setTitle(myXMLReader.getSimulationName());
		primaryStage.setScene(mainScene);
		primaryStage.show();
	} 
	
	private void makeSimulation() {
		if(myXMLReader.getCellType().equals("Cell")) {
			myGrid = new Grid(myXMLReader);
		} else if(myXMLReader.getCellType().equals("MovingCell")){
			myGrid = new MovingGrid(myXMLReader);
		}
		societyView = new GridDisplay(myGrid);
	}

	private void configureAnimation() {
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		KeyFrame frame = new KeyFrame(Duration.millis(250), e->stepAnimation());
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
		return new Scene(sceneRoot, sceneWidth, sceneHeight);
	}
	
	private Pane configureDisplay(){
		Pane gridPane = new Pane();
		gridPane.setPrefSize(sceneWidth, gridY);
		gridPane.getChildren().add(societyView.getGridView()); 
		return gridPane;
	}

	private HBox configureControls() {
		HBox controlPane = new HBox();
		controlPane.setPrefSize(sceneWidth, controlY);
		
		Button startButton = new Button(myResources.getString("StartLabel"));
		startButton.setOnMouseClicked(e->animate());
		
		Button stopButton = new Button(myResources.getString("StopLabel"));
		stopButton.setOnMouseClicked(e->stopAnimation());
		
		Button stepButton = new Button(myResources.getString("StepLabel"));
		stepButton.setOnMouseClicked(e->stepAnimation());
		
		Button resetButton = new Button(myResources.getString("ResetLabel"));
		resetButton.setOnMouseClicked(e->resetAnimation());
		
		Button randomizeButton = new Button(myResources.getString("RandomLabel"));
		randomizeButton.setOnMouseClicked(e->randomizeGrid());
		
		controlPane.getChildren().addAll(startButton,stopButton,stepButton,resetButton,randomizeButton);
		return controlPane;
	}
	
	private void makeButton(String text) {
		//see the "makeButton()" method in lab_browser
	}
	
	private void loadNewFile() {
		
	}
	
	private void randomizeGrid() {
		animation.stop();
		societyView.randomizeGrid();
	}
	
	private void resetAnimation() {
		animation.stop();
		societyView.reset();
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