package cs.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import cs.configuration.ConfigDoc;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;

public class ControlDisplay {
	public static final double SPEED_UP_FACTOR = 2.0;
	public static final double DEFAULT_ANIMATION_SPEED = 250;

	private ResourceBundle myResources;
	private Group controlRoot;
	
	private ConfigDoc myConfig;
	private GUIController myController;
	
	private HBox myBasicControls;
	private HBox myInputFieldsRow1;
	private HBox myInputFieldsRow2;
	
	public ControlDisplay(ConfigDoc config, GUIController controller) {
		myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + "GUI");
		controlRoot = new Group();
		myConfig = config;
		myController = controller;
		myBasicControls = new HBox();
		myInputFieldsRow1 = new HBox();
		myInputFieldsRow2 = new HBox();
		
		configureBasicControls();
		configureInputFields();
		controlRoot.getChildren().addAll(myBasicControls,myInputFieldsRow1, myInputFieldsRow2);
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
		
		Label speedLabel = new Label("Speed:");
		Slider speedSlider = makeSlider(0,100,25);
		speedSlider.valueProperty().addListener(e->myController.speedAnimation(speedSlider.getValue()));
		
		myBasicControls.getChildren().addAll(startButton,stopButton,stepButton,resetButton,
				randomizeButton,loadButton,saveButton,speedLabel,speedSlider);
	}
	
	private void configureInputFields() {
		myInputFieldsRow1.setLayoutY(30);
		myInputFieldsRow2.setLayoutY(60);
		
		Label widthLabel = new Label("Width: ");
		Label heightLabel = new Label("Height:");
		Label edgeLabel = new Label( "Edge: ");
		Label shapeLabel = new Label(" Shape: ");
		Label enableLabel = new Label(" Grid: ");
		
		final Spinner<Integer> widthSpinner = makeSpinner(1,100,myConfig.getGridWidth());
		widthSpinner.valueProperty().addListener(e->myController.updateWidth((int)widthSpinner.getValue()));
		
		final Spinner<Integer> heightSpinner = makeSpinner(1,100,myConfig.getGridHeight());
		heightSpinner.valueProperty().addListener(e->myController.updateHeight((int)heightSpinner.getValue()));
		
		ComboBox<String> edgeSelect = makeComboBox(Arrays.asList(new String[] {ConfigDoc.GRID_EDGE_FINITE,ConfigDoc.GRID_EDGE_TOROIDAL}));
		edgeSelect.setValue(myConfig.getGridEdge());
		edgeSelect.valueProperty().addListener(e->myController.updateGridEdgeType(edgeSelect.getValue()));
		
		ComboBox<String> shapeSelect = makeComboBox(Arrays.asList(new String[] {ConfigDoc.GRID_SHAPE_SQUARE,ConfigDoc.GRID_SHAPE_TRIANGLE}));
		shapeSelect.setValue(myConfig.getGridShape());
		shapeSelect.valueProperty().addListener(e->myController.updateGridShapeType(shapeSelect.getValue()));
		
		CheckBox enableGrid = new CheckBox();
		enableGrid.setSelected(myConfig.hasGridLines());
		enableGrid.selectedProperty().addListener(e->myController.toggleGridLines(enableGrid.selectedProperty().get()));
		
		myInputFieldsRow1.getChildren().addAll(widthLabel, widthSpinner, edgeLabel, edgeSelect, shapeLabel, shapeSelect, enableLabel, enableGrid);
		myInputFieldsRow2.getChildren().addAll(heightLabel, heightSpinner);
	}
	
	private Slider makeSlider(int min, int max, int init) {
		Slider slide = new Slider(min, max, init);
		slide.setPrefWidth(100);
		slide.setShowTickMarks(true);
		slide.setShowTickLabels(true);
		return slide;
	}
	
	private Spinner<Integer> makeSpinner(int min, int max, int init) {
		Spinner<Integer> spin = new Spinner<Integer>();
		spin.setPrefWidth(70);
		spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, init));
		spin.setEditable(true);
		return spin;
	}
	
	private ComboBox<String> makeComboBox(List<String> entries) {
		ComboBox<String> combo = new ComboBox<String>();
		combo.setPrefWidth(100);
		for(String s: entries) {
			combo.getItems().add(s);
		}
		return combo;
	}
}
