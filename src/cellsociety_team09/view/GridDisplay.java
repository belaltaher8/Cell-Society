package cellsociety_team09.view;
import cellsociety_team09.model.Grid;
import cellsociety_team09.model.Point;

import java.util.ResourceBundle;

import cellsociety_team09.model.Cell;
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
	private int myGridWidth; 
	private int myGridHeight; 
	
	private ResourceBundle myResources; 
	private Grid myGrid; 
	
	public GridDisplay(Grid grid){
		gridRoot = new Group();
		myGrid = grid;
		myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + "CellColors");
		myGridWidth = grid.getWidth();
		myGridHeight = grid.getHeight();
		drawGridDisplay();
	}
	
	/**
	 * @param cellsPerRow 
	 * @param cellsPerColumn
	 * creates the grid part of the display where the simulation occurs
	 */
	private void drawGridDisplay(){  
		gridRoot.getChildren().clear();
		int cellWidth = DISPLAY_WIDTH/myGridWidth; 
		int cellHeight = DISPLAY_HEIGHT/myGridHeight;
		
		for (int x = 0; x < myGridWidth; x++){
			for (int y = 0; y < myGridHeight; y++){
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
	private Shape setColor(Shape gridCell, Cell c) { 
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