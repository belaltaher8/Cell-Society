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
import javafx.scene.paint.Color;
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

	
	public GridDisplay(Simulation grid, ConfigDoc config){
		gridRoot = new Group();
		myGrid = grid;
		myConfig = config;
		myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + "CellColors");	
		initializeStateCounts();
		drawGridDisplay();
	}
	
	public Map<Integer, Integer> getStateMap(){
		return stateCounts; 
	}
	
	protected void initializeStateCounts() {
		stateCounts = new HashMap<Integer, Integer>(); 
		int numStates = myGrid.getNumStates(); 
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
	
	public void drawGridDisplay(){  
		gridRoot.getChildren().clear();
		int cellWidth = DISPLAY_WIDTH/myConfig.getGridWidth(); 
		int cellHeight = DISPLAY_HEIGHT/myConfig.getGridHeight();
		
		for (int x = 0; x < myConfig.getGridWidth(); x++){
			for (int y = 0; y < myConfig.getGridHeight(); y++){
				Cell c = myGrid.getCellAtPoint(new Point(x,y));
				if(c != null) {
					updateStateCounts(c);
					Shape gridCell = makeShape(c,x, y, cellWidth, cellHeight);
					gridRoot.getChildren().add(gridCell);
				}

			}
		}
	}

	private Shape makeShape(Cell c, double x, double y, double width, double height) {
		Rectangle r = new Rectangle(x*width, y*height, width, height);
		r.setOnMouseClicked(e->this.handleClick(c));
		if(myConfig.hasGridLines()) {
			r.setStroke(Color.BLACK);
		}
		Shape gridCell = setColor(r, c);
		return gridCell;
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
	
	protected void handleClick(Cell c) {
		int nextState = c.getState() + 1;
		if(nextState >= myGrid.getNumStates()) {
			nextState = Cell.DEFAULT_STATE;
		}
		myGrid.replaceCell(c, myGrid.placeCell(nextState, c.getCoords()));
		this.drawGridDisplay();
	}
}