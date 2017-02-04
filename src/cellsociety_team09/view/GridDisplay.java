package cellsociety_team09.view;
import cellsociety_team09.model.Grid;
import cellsociety_team09.model.Point;

import java.util.ResourceBundle;

import cellsociety_team09.model.Cell;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * @author tahiaemran
 * Display class creates control pane and grid view 
 */
public class GridDisplay {
	Shape cellShape; 
	private Group gridRoot; 
	private final int displayX = 600; 
	private final int displayY = 800;
	public final int gridY = 600;
	private int myWidth; // number cells per row 
	private int myHeight; // number cells col 
	private ResourceBundle myResources; 
	public static final String DEFAULT_RESOURCE_PACKAGE = "Resources/";
	private Grid current; 
	
	public GridDisplay(Grid grid){
		gridRoot = new Group();
		current = grid;
		//myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE+ "BinaryStates");
		myWidth = grid.getWidth();
		myHeight = grid.getHeight();
	}
	
	/**
	 * @param cellsPerRow 
	 * @param cellsPerColumn
	 * creates the grid part of the display 
	 */
	private void createGridDisplay(){  
		gridRoot.getChildren().clear();
		int cellWidth = displayX/myWidth; // equivalent to columnWidth
		int cellHeight = gridY/myHeight; // equivalent to rowHeight
		for (int x = 0; x < myWidth; x++){
			for (int y = 0; y < myHeight; y++){
				Point p = new Point(x,y);
				Cell c = current.getCellAtPoint(p); 
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
		// PASS AS PARAMETER
		// assuming two possible states here
		if (c.getState()==0){
			gridCell.setFill(Color.DEEPPINK);
			//gridCell.setFill(Paint.valueOf(myResources.getString("State0")));
		}
		else{
			gridCell.setFill(Color.AQUA);
			//gridCell.setFill(Paint.valueOf(myResources.getString("State1")));
		}
		return gridCell; 
	}
	/**
	 * @return control pane for use by other classes 
	 */
	/**
	 * @return grid view for use by other   
	 */
	public Group getGridView(){
		return gridRoot; 
	}
	public void update() {
		current.stepGrid();
		createGridDisplay();
	}
}
