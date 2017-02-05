package cellsociety_team09.view;

import java.util.ResourceBundle;
import cellsociety_team09.configuration.XMLReader;
import cellsociety_team09.model.Grid;
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
	public static final String DEFAULT_RESOURCE_PACKAGE = "Resources/";
	
	private ResourceBundle myResources; 
	
	private XMLReader myXMLReader;
	private GridDisplay societyView; 
	private Grid myGrid;
	
	private Group sceneRoot; 
	private Scene myScene; 
	private Timeline animation; 

	private BorderPane mainView; 
	private HBox controlPane; 
	private Pane gridPane;

	public GUIController(XMLReader reader, Grid grid){
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "ButtonLabels");
		
		myXMLReader = reader;
		myGrid = grid;
		societyView = new GridDisplay(grid);
		
		sceneRoot = new Group(); 
		myScene = new Scene(sceneRoot, sceneWidth, sceneHeight);
		animation = new Timeline();
		
		mainView = new BorderPane();
		controlPane = new HBox();
		gridPane = new Pane();
	} 
	
	public void setup(Stage primaryStage) {
		configureDisplay();
		sceneRoot.getChildren().add(mainView);
		
		animation.setCycleCount(Timeline.INDEFINITE);
		KeyFrame frame = new KeyFrame(Duration.millis(250), e->stepAnimation());
		animation.getKeyFrames().add(frame);
		
		primaryStage.setTitle(myXMLReader.getSimulationName());
		primaryStage.setScene(myScene);
		primaryStage.show();
	}

	private void configureDisplay(){
		gridPane.setPrefSize(sceneWidth, gridY);
		gridPane.getChildren().add(societyView.getGridView()); 
		mainView.setCenter(gridPane);
		
		controlPane.setPrefSize(sceneWidth, controlY);
		configureControls();
		mainView.setBottom(controlPane);
	}

	private void configureControls() {
		Button startButton = new Button(myResources.getString("StartLabel"));
		startButton.setOnMouseClicked(e->animate());
		
		Button stopButton = new Button(myResources.getString("StopLabel"));
		stopButton.setOnMouseClicked(e->stopAnimation());
		
		Button stepButton = new Button(myResources.getString("StepLabel"));
		stepButton.setOnMouseClicked(e->stepAnimation());
		
		Button resetButton = new Button(myResources.getString("ResetLabel"));
		resetButton.setOnMouseClicked(e->resetAnimation());
		
		controlPane.getChildren().addAll(startButton,stopButton,stepButton,resetButton);
	}
	
	private void makeButton(String text) {
		//see the "makeButton()" method in lab_browser
	}
	
	private void resetAnimation() {
		societyView.reset();
		animation.stop();
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