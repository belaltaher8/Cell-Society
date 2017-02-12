package cs.view;
import java.util.ResourceBundle;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

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

	
	public GridDisplay(Simulation grid, ConfigDoc config){
		gridRoot = new Group();
		myGrid = grid;
		myConfig = config;
		myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + "CellColors");
		drawGridDisplay();
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
	
	/**
	 * @param cellsPerRow 
	 * @param cellsPerColumn
	 * creates the grid part of the display where the simulation occurs
	 */
	
	protected void drawGridDisplay(){  
		gridRoot.getChildren().clear();
		int cellWidth = DISPLAY_WIDTH/myConfig.getGridWidth(); 
		int cellHeight = DISPLAY_HEIGHT/myConfig.getGridHeight();
		
		for (int x = 0; x < myConfig.getGridWidth(); x++){
			for (int y = 0; y < myConfig.getGridHeight(); y++){
				Point p = new Point(x,y);
				Cell c = myGrid.getCellAtPoint(p);
				Shape gridCell = setColor(new Rectangle(x*cellWidth, y*cellHeight, cellWidth, cellHeight), c);
				gridRoot.getChildren().add(gridCell);
			}
		}
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