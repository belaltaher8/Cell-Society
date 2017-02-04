package cellsociety_team09.view;
import cellsociety_team09.model.Grid;
import cellsociety_team09.model.Point;

import java.util.Map;
import java.util.ResourceBundle;

import cellsociety_team09.model.Cell;
import javafx.scene.Group;
import javafx.scene.Node;
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
	public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety_team09/src/CellSocResources";
	private Grid current; 
	private Map<Point, Cell> gridMap;
	public GridDisplay(Grid grid){
		current = grid;
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE+ "/ButtonLabels");
		myWidth = grid.getWidth();
		myHeight = grid.getHeight();
		gridMap = grid.getMap();
		createGridDisplay();
	}
	/**
	 * @param cellsPerRow 
	 * @param cellsPerColumn
	 * creates the grid part of the display 
	 */
	private void createGridDisplay(){  
		gridRoot = new Group(); 
		int cellWidth = displayX/myWidth; // equivalent to columnWidth
		int cellHeight = gridY/myHeight; // equivalent to rowHeight

		for (int x = 0; x < myWidth; x++){
			for (int y = 0; y < myHeight; y++){
				Point p = new Point(x,y);
				Cell c = gridMap.get(p); 
				System.out.println(c);
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
		if (c.getState()==0){// ERROR
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
	public Node getGridView(){
		return gridRoot; 
	}
	public void update() {
		current.advanceGrid();
		createGridDisplay();
	}
}
