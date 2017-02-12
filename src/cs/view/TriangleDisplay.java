package cs.view;

import java.util.ArrayList;

import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import javafx.scene.Group;
import javafx.scene.shape.Shape;

public class TriangleDisplay extends GridDisplay{
	private Group gridRoot; 

	private Simulation myGrid; 

	public TriangleDisplay(Simulation grid) {
		super(grid);
		gridRoot = new Group();
		myGrid = grid; 
		drawGridDisplay();
	}

	// lot of duplicated code, figure out refactor for this

	@Override
	protected void drawGridDisplay(){
		//this.gridRoot.getChildren().clear(); 
		// should these be encapsulated further?
		int cellWidth = GridDisplay.DISPLAY_WIDTH/ ((myGrid.getWidth()/2)+1); 
		int cellHeight = GridDisplay.DISPLAY_HEIGHT/ myGrid.getHeight();
		for (int y= 0; y<myGrid.getHeight(); y++){
			for (int x=0; y<myGrid.getWidth(); x++){
				Cell c = myGrid.getCellAtPoint(new Point(x,y));
				ArrayList<GUIPoint> trianglePoints = createTriangleCoordinates(new Point(x,y), cellWidth, cellHeight);
				Shape gridCell = setColor(new Triangle(trianglePoints.get(0), trianglePoints.get(1), trianglePoints.get(2)),c);
				gridRoot.getChildren().add(gridCell);
			}

		}
	}

	private ArrayList<GUIPoint> createTriangleCoordinates(Point point, int width, int height) {
		ArrayList<GUIPoint> triangleCoords = new ArrayList<GUIPoint>(); 
		int x = point.getX(); 
		int y = point.getY(); 
		GUIPoint p1 = new GUIPoint();
		GUIPoint p2 = new GUIPoint(); 
		GUIPoint p3 = new GUIPoint(); 
		if (point.getX()%2==0 && (point.getX()+point.getY())%2==0){
			// triangle is in even row and points upwards 
			p1 = new GUIPoint(((y+1)/2.0)*width, x*height);
			p2 = new GUIPoint((y/2.0)*width,(x+1)*height);
			p3 = new GUIPoint(((y/2.0)+1.0)*width, (x+1)*height);	
		}
		if (point.getX()%2==0 && (point.getX()+point.getY())%2!=0){
			// triangle is in even row and points downwards
			p1 = new GUIPoint((y/2.0)*width, x*height); 
			p2 = new GUIPoint(((y/2.0)+1)*width, x*height);
			p3 = new GUIPoint((Math.floor(y/2.0)+1)*width, (x+1)*height);

		}
		if (point.getX()%2!=0 && (point.getX()+point.getY())%2==0){
			// triangle is in odd row and points upwards 
			p1 = new GUIPoint((y/2.0)*width, (x+1)*height);
			p2 = new GUIPoint(((y/2.0)+1)*width, (x+1)*height);
			p3 = new GUIPoint((Math.floor(y/2.0)+1)*width, x*height);
		}
		if (point.getX()%2!=0 && (point.getX()+point.getY())%2!=0){
			// triangle is in odd row and points downwards
			p1 = new GUIPoint(((y+1)/2.0)*width, (x+1)*height);
			p2 = new GUIPoint((y/2.0)*width, x*height);
			p3 = new GUIPoint(((y/2.0)+1)*width, x*height);
		}
		triangleCoords.add(p1);
		triangleCoords.add(p2);
		triangleCoords.add(p3);
		return triangleCoords; 
	}

	@Override
	public Group getGridView(){
		return gridRoot; 
	}

}
