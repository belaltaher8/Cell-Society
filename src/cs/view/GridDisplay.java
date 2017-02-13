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
 * superclass for all the different display types 
 * default will draw a square configuration 
 * 
 * dependencies: dependent upon the Simulation and ConfigDoc classes for data 
 * 
 * to use: create a new GridDisplay object for a rectangular configuration of cells, 
 * 	for other configurations use those classes 
 * 
 */

public class GridDisplay {
	public static final int DISPLAY_WIDTH = 600; 
	public static final int DISPLAY_HEIGHT = 600;
	
	private Group gridRoot; 

	private ResourceBundle myResources; 
	private Simulation mySim; 
	private ConfigDoc myConfig;
	

	private HashMap <Integer, Integer> stateCounts; // map of each state to number of cells in that state 


	
	public GridDisplay(Simulation sim, ConfigDoc config){
		gridRoot = new Group();
		mySim = sim;
		myConfig = config;
		myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + myConfig.getSimType());	
		clearStateCounts();
		drawGridDisplay();
	}
	
	/**
	 * @param cellsPerRow 
	 * @param cellsPerColumn
	 * creates the grid part of the display where the simulation occurs
	 */
	public void drawGridDisplay(){  
		gridRoot.getChildren().clear();
		clearStateCounts();
		
		for (int x = 0; x < myConfig.getGridWidth(); x++){
			for (int y = 0; y < myConfig.getGridHeight(); y++){
				Cell c = mySim.getCellAtPoint(new Point(x,y));
				if(c != null) {
					updateStateCounts(c);
					Shape gridCell = makeShape(c,x, y);
					gridRoot.getChildren().add(gridCell);
				}

			}
		}
	}

	protected Shape makeShape(Cell c, double x, double y) {
		int cellWidth = DISPLAY_WIDTH/myConfig.getGridWidth(); 
		int cellHeight = DISPLAY_HEIGHT/myConfig.getGridHeight();
		
		Rectangle r = new Rectangle(x*cellWidth, y*cellHeight, cellWidth, cellHeight);
		r.setOnMouseClicked(e->this.handleClick(c));
		if(myConfig.hasGridLines()) {
			r.setStroke(Paint.valueOf(myResources.getString("GridLines")));
		}
		
		Shape gridCell = setColor(r, c);
		return gridCell;
	}
	
	protected ResourceBundle getResources() {
		return myResources;
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
	 * advances the grid to the next state
	 */
	public void step() {
		mySim.stepGrid();
		drawGridDisplay();
	}
	
	/**
	 * @return grid view for use by other classes
	 */
	public Group getGridView(){
		return gridRoot; 
	}
	
	protected Simulation getGrid(){
		return mySim; 
	}
	protected ConfigDoc getConfig() {
		return myConfig;
	}
	
	/**
	 * @return a map of each state to the number of cells in that state 
	 * 	used by the Graph class to graph the appropriate data 
	 */
	public Map<Integer, Integer> getStateCounts(){
		return stateCounts; 
	}
	
	protected void clearStateCounts() {
		stateCounts = new HashMap<Integer, Integer>(); 
		int numStates = mySim.getNumStates(); 
		// initialize hashMap keys to zero
		for (int i=0; i<numStates; i++){
			if (!stateCounts.containsKey(i)){
				stateCounts.put(i, 0);
			}
		}
	}

	protected void updateStateCounts(Cell c) {
		int currentCount = stateCounts.get(c.getState());
		stateCounts.put(c.getState(), currentCount+1);
	}
	
	protected void handleClick(Cell c) {
		int nextState = c.getState() + 1;
		if(nextState >= mySim.getNumStates()) {
			nextState = Cell.DEFAULT_STATE;
		}
		mySim.replaceCell(c, mySim.placeCell(nextState, c.getCoords()));
		this.drawGridDisplay();
	}
}