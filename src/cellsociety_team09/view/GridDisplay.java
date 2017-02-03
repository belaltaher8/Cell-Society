package cellsociety_team09.view;
import cellsociety_team09.model.Grid;
import cellsociety_team09.model.Point;

import java.util.ResourceBundle;

import cellsociety_team09.model.Cell;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * @author tahiaemran
 * Display class creates control pane and grid view 
 */
public class GridDisplay extends Parent {
	Shape cellShape; 
	private Group gridRoot; 
	private final int displayX = 600; 
	private final int displayY = 800;
	public final int gridY = 600;
	////////PLACEHOLDERS////////
	private int rowCell = 10; 
	private int colCell = 10; 
	///////////////////////////
	private ResourceBundle myResources; 
	public static final String DEFAULT_RESOURCE_PACKAGE = "Resources/";
	private Grid current; 
	public GridDisplay(){
		current = new Grid();
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE+ "BinaryStates");
	}
	/**
	 * @param cellsPerRow 
	 * @param cellsPerColumn
	 * creates the grid part of the display 
	 */
	private void createGridDisplay(int cellsPerRow, int cellsPerColumn){  
		gridRoot = new Group(); 
		int cellWidth =displayX/ cellsPerRow; // equivalent to columnWidth
		int cellHeight = gridY/cellsPerColumn; // equivalent to rowHeight
		for (int i=0; i<gridY; i+=cellHeight){
			for (int j=0; j<displayX;j+=cellWidth){
				Point p = new Point(i,j);
				Cell c = current.getCellAtPoint(p); 
				Shape gridCell = setColor(new Rectangle(i,j), c);
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
			gridCell.setFill(Paint.valueOf(myResources.getString("State0")));
		}
		else{
			gridCell.setFill(Paint.valueOf(myResources.getString("State1")));

		}
		return gridCell; 
	}
	/**
	 * @return control pane for use by other classes 
	 */
	/**
	 * @return grid view for use by other   
	 */
	public Parent getGridView(){
		return gridRoot; 
	}
	public void update() {
		current.advanceGrid();
		createGridDisplay(rowCell,colCell);
	}
}
