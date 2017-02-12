package cs.view;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

/**
 * @author tahiaemran
 * Display class creates control pane and grid view 
 */
public class GridDisplay {
	public static final int DISPLAY_WIDTH = 600; 
	public static final int DISPLAY_HEIGHT = 600;
	
	private Group gridRoot; 

	private ResourceBundle myResources; 
	private Simulation myGrid; 
	private ConfigDoc myConfig;
	
	private HashMap <Integer, Integer> stateCounts; // map of each state to number of cells in that state 
	private HashMap <Shape, Cell> gridToCells; 
	
	public GridDisplay(Simulation grid, ConfigDoc config){
		gridRoot = new Group();
		myGrid = grid;
		myConfig = config;
		myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + "CellColors");	
		drawGridDisplay();
	}
	public Map<Integer, Integer> getStateMap(){
		return stateCounts; 
	}
	public Map<Shape, Cell> getCellMap(){
		return gridToCells; 
	}
	private void initializeStateCounts() {
		stateCounts = new HashMap<Integer, Integer>(); 
		int numStates = myConfig.getNumStates(); 
		// initialize hashMap keys to zero
		for (int i=0; i<numStates; i++){
			if (!stateCounts.containsKey(i)){
				stateCounts.put(i, 0);
			}
		}
		

	}

	protected Group getGridRoot(){
		return gridRoot;
	}
	protected Simulation getGrid(){
		return myGrid; 
	}
	protected ConfigDoc getConfig() {
		return myConfig;
	}
	public boolean clickIsHandled(){
		return false; 
	}
	/**
	 * @param cellsPerRow 
	 * @param cellsPerColumn
	 * creates the grid part of the display where the simulation occurs
	 */
	
	protected void drawGridDisplay(){  
		gridRoot.getChildren().clear();
		initializeStateCounts(); 
		gridToCells = new HashMap<Shape, Cell>();
		int cellWidth = DISPLAY_WIDTH/myConfig.getGridWidth(); 
		int cellHeight = DISPLAY_HEIGHT/myConfig.getGridHeight();
		
		for (int x = 0; x < myConfig.getGridWidth(); x++){
			for (int y = 0; y < myConfig.getGridHeight(); y++){
				Point p = new Point(x,y);
				Cell c = myGrid.getCellAtPoint(p);
				updateStateCounts(c);
				Shape gridCell = setColor(new Rectangle(x*cellWidth, y*cellHeight, cellWidth, cellHeight), c);	
				gridToCells.put(gridCell, c);
				gridCell.setOnMouseClicked(e->handleClick(gridCell,c));
				gridRoot.getChildren().add(gridCell);
			}
		}
	}

	public void handleClick(Shape gridCell, Cell c) {
		Stage dialogBox = new Stage(); 
		HBox dialogButtons = new HBox(); 
		Button changeButton = new Button("Change State"); ////// RESOURCE BUNDLES 
		changeButton.setOnMouseClicked(e -> changeState(gridCell, c, dialogBox));
		dialogButtons.getChildren().addAll(changeButton);
		Scene s = new Scene(dialogButtons, 200, 200);
		dialogBox.setScene(s);
		dialogBox.show();
	}

	private void changeState(Shape gridCell, Cell c, Stage dialogBox) {
		HBox stateButtons = new HBox(); 
		for (int i=0; i<myGrid.getConfig().getNumStates();i++){
			int state = i; 
			Button stateButton = new Button(Integer.toString(i));
			stateButton.setOnMouseClicked(e->changeStateTo(gridCell, state, c));
			stateButtons.getChildren().add(stateButton);
		}
		Scene s1 = new Scene(stateButtons);
		dialogBox.setScene(s1);
		dialogBox.show();
	}

	private void changeStateTo(Shape gridCell, int i, Cell c) {
		Cell c1  = gridToCells.get(gridCell); 
		System.out.println(c1);
		c1.setState(i);
		System.out.println(c1.getState());
		drawGridDisplay();
	}
	
	private void updateStateCounts(Cell c) {
		int currentCount = stateCounts.get(c.getState());
		stateCounts.put(c.getState(), currentCount+1);
	}

	/**
	 * @param Shape gridCell
	 * @param Cell c
	 * @return Shape with color set according to cell state 
	 */
	public Shape setColor(Shape gridCell, Cell c) { 
		gridCell.setFill(Paint.valueOf(myResources.getString("State" + c.getState())));
		return gridCell; 
	}
	
	/**
	 * @return grid view for use by other classes
	 */
	public Group getGridView(){
		return gridRoot; 
	}
	
	/**
	 * advances the grid to the next state
	 */
	public void step() {
		myGrid.stepGrid();
		drawGridDisplay();
	}
	
	/**
	 * resets grid to original state
	 */
	public void reset() {
		myGrid.reset();
		drawGridDisplay();
	}
	
	/**
	 * randomizes the grid 
	 */
	public void randomizeGrid() {
		myGrid.randomizeGrid();
		drawGridDisplay();
	}
}