package cellsociety_team09.view;
import java.util.ResourceBundle;

import cellsociety_team09.model.Grid;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class GUIController{
	private GridDisplay societyView; 
	private Scene myScene; 
	private Group sceneRoot; 
	private Button startButton; 
	private Button stopButton; // calls pause()
	private Button stepButton;
	private Button resetButton; // calls stop()
	private Timeline animation; 
	private final int sceneWidth = 600; 
	private final int sceneHeight = 800; 
	private final int displayX = 600; 
	///private final int displayY = 800; 
	private final int controlY = 200; 
	public final int gridY = 600;
	private HBox controlPane; 
	private ResourceBundle myResources; 
	public static final String DEFAULT_RESOURCE_PACKAGE = "Resources/";
<<<<<<< HEAD
	public GUIController(){
		societyView = new GridDisplay(); 
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "ButtonLabels");
=======
	
	public GUIController(Grid grid){
		societyView = new GridDisplay(grid); 
>>>>>>> master
		createControls();
		BorderPane b = configureDisplay();
		sceneRoot = new Group(); 
		sceneRoot.getChildren().addAll(b);
		myScene = new Scene(sceneRoot, sceneWidth, sceneHeight);
	}
	
	public Scene getScene(){
		return myScene;
	}
	/**
	 * creates control Pane and sets size  
	 */
	private void createControls(){
		controlPane = new HBox(); 
		controlPane.setPrefWidth(displayX);
		controlPane.setPrefHeight(controlY);
		configureControls();
	}

	private BorderPane configureDisplay(){
		BorderPane root = new BorderPane();
		createControls();
		Parent gridPane = societyView.getGridView(); 
		root.setBottom(controlPane);
		root.setCenter(gridPane);
		return root; 
	}

	private void configureControls() {
		startButton = new Button(myResources.getString("StartLabel"));
		startButton.setOnMouseClicked(e->animate());
		stopButton = new Button(myResources.getString("StopLabel"));
		stopButton.setOnMouseClicked(e->stopAnimation());
		stepButton = new Button(myResources.getString("StepLabel"));
		stepButton.setOnMouseClicked(e->stepAnimation());
		resetButton = new Button(myResources.getString("ResetLabel"));
		resetButton.setOnMouseClicked(e->resetAnimation());
		controlPane.getChildren().addAll(startButton,stopButton,stepButton,resetButton);

	}
	private void resetAnimation() {
		animation.stop();
	}

	private void stepAnimation() {
		animation.pause();
		societyView.update();
	}

	private void stopAnimation() {
		// TODO Auto-generated method stub
		animation.pause();
	}

	private void animate() {
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		// should this return a KeyFrame
		KeyFrame frame = new KeyFrame(Duration.millis(2000), e->societyView.update()); // will update every two seconds, until stop is presssed 
		animation.getKeyFrames().add(frame);
		animation.play();

	}
	
}
