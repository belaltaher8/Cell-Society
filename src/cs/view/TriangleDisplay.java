package cs.view;

import java.util.ArrayList;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * @author tahiaemran
 * class for creating a grid with triangular cells 
 * 
 * dependencies: Simulaiton and ConfigDoc classes 
 * 
 * to use: create a new TriangleDisplay object in the GUI class 
 *
 */
public class TriangleDisplay extends GridDisplay{

	public TriangleDisplay(Simulation grid, ConfigDoc config) {
		super(grid, config);
	}

	/* (non-Javadoc)
	 * @see cs.view.GridDisplay#drawGridDisplay()
	 */
	@Override
	public void drawGridDisplay(){
		this.getGridRoot().getChildren().clear(); 
		initializeStateCounts();

		int cellWidth = GridDisplay.DISPLAY_WIDTH / ((this.getConfig().getGridWidth()/2)+1); 
		int cellHeight = GridDisplay.DISPLAY_HEIGHT / this.getConfig().getGridHeight();

		for (int x = 0; x < this.getConfig().getGridWidth(); x++){
			for (int y = 0; y < this.getConfig().getGridHeight(); y++){
				Cell c = this.getGrid().getCellAtPoint(new Point(x,y));
				if(c != null) {
					ArrayList<GUIPoint> trianglePoints = createTriangleCoordinates(new Point(x,y), cellWidth, cellHeight);
					Triangle myTriangle = new Triangle(trianglePoints.get(0), trianglePoints.get(1), trianglePoints.get(2));
					myTriangle.getTriangle().setOnMouseClicked(e->this.handleClick(c));
					if(this.getConfig().hasGridLines()) {
						myTriangle.getTriangle().setStroke(Color.BLACK);
					}
					Shape gridCell = setColor(myTriangle.getTriangle(),c);
					this.getGridRoot().getChildren().add(gridCell);
				}
			}
		}
	}

	private ArrayList<GUIPoint> createTriangleCoordinates(Point point, int width, int height) {
		ArrayList<GUIPoint> triangleCoords = new ArrayList<GUIPoint>(); 
		//Flip x and y intentionally to make the math work
		int x = point.getY(); 
		int y = point.getX(); 
		GUIPoint p1 = new GUIPoint();
		GUIPoint p2 = new GUIPoint(); 
		GUIPoint p3 = new GUIPoint(); 
		if (x%2==0 && (x+y)%2==0){
			// triangle is in even row and points upwards 
			p1 = new GUIPoint(((y+1)/2.0)*width, x*height);
			p2 = new GUIPoint((y/2.0)*width,(x+1)*height);
			p3 = new GUIPoint(((y/2.0)+1.0)*width, (x+1)*height);	
		}
		if (x%2==0 && (x+y)%2!=0){
			// triangle is in even row and points downwards
			p1 = new GUIPoint((y/2.0)*width, x*height); 
			p2 = new GUIPoint(((y/2.0)+1)*width, x*height);
			p3 = new GUIPoint((Math.floor(y/2.0)+1)*width, (x+1)*height);

		}
		if (x%2!=0 && (x+y)%2==0){
			// triangle is in odd row and points upwards 
			p1 = new GUIPoint((y/2.0)*width, (x+1)*height);
			p2 = new GUIPoint(((y/2.0)+1)*width, (x+1)*height);
			p3 = new GUIPoint((Math.floor(y/2.0)+1)*width, x*height);
		}
		if (x%2!=0 && (x+y)%2!=0){
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

}
