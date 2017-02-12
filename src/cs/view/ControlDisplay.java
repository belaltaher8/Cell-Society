package cs.view;

import java.util.ResourceBundle;

import cs.configuration.ConfigDoc;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class ControlDisplay {
	public static final double SPEED_UP_FACTOR = 2.0;
	public static final double DEFAULT_ANIMATION_SPEED = 250;

	private ResourceBundle myResources;
	private Group controlRoot;
	
	private ConfigDoc myConfig;
	private GUIController myController;
	
	private HBox myBasicControls;
	private HBox mySliders;
	private Slider speedSlider;
	
	public ControlDisplay(ConfigDoc config, GUIController controller) {
		myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + "GUI");
		controlRoot = new Group();
		myConfig = config;
		myController = controller;
		myBasicControls = new HBox();
		mySliders = new HBox();
		
		configureBasicControls();
		configureSliders();
		controlRoot.getChildren().addAll(myBasicControls, mySliders);
	}
	
	public Group getControlView() {
		return controlRoot;
	}
	
	private void configureBasicControls() {
		Button startButton = new Button(myResources.getString("StartLabel"));
		startButton.setOnMouseClicked(e->myController.animate());
		
		Button stopButton = new Button(myResources.getString("StopLabel"));
		stopButton.setOnMouseClicked(e->myController.stopAnimation());
		
		Button stepButton = new Button(myResources.getString("StepLabel"));
		stepButton.setOnMouseClicked(e->myController.stepAnimation());
		
		Button resetButton = new Button(myResources.getString("ResetLabel"));
		resetButton.setOnMouseClicked(e->myController.resetGrid());
		
		Button randomizeButton = new Button(myResources.getString("RandomLabel"));
		randomizeButton.setOnMouseClicked(e->myController.randomizeGrid());
		
		Button loadButton = new Button(myResources.getString("LoadLabel"));
		loadButton.setOnMouseClicked(e->myController.loadNewFile());
		
		Button saveButton = new Button(myResources.getString("SaveLabel"));
		saveButton.setOnMouseClicked(e->myController.saveSnapshot());
		
		Button speedButton = new Button(myResources.getString("Speed"));
		speedButton.setOnMouseClicked(e->myController.speedAnimation());
		
		Button updateButton = new Button(myResources.getString("Update"));
		updateButton.setOnMouseClicked(e->myController.updateParameters((int)speedSlider.getValue(), (int)speedSlider.getValue()));
		
		myBasicControls.getChildren().addAll(startButton,stopButton,stepButton,resetButton,
				randomizeButton,loadButton,saveButton, speedButton, updateButton);
	}
	
	private void configureSliders() {
		mySliders.setLayoutY(50);
		speedSlider = new Slider(0, 100, 25);
		speedSlider.setShowTickMarks(true);
		speedSlider.setShowTickLabels(true);
		mySliders.getChildren().addAll(speedSlider);
	}
	
	public double getAnimationSpeed() {
		return speedSlider.getValue();
	}
}
