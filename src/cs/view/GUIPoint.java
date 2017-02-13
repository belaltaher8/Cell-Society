package cs.view;

/**
 * @author tahiaemran
 *
 *	Point class for use by the GUI (takes doubles instead of ints) 
 *
 *  Use in creating polygons/ shapes with  non-whole number coordinates for the GUI
 *  
 *  To use: create a new GUIPoint with the given x and y 
 */
public class GUIPoint {
	double myX; 
	double myY; 
	public GUIPoint(double x, double y){
		myX = x; 
		myY = y; 
	}
	public GUIPoint(){
		myX = 0; 
		myY= 0; 
	}
	/**
	 * @return the x value of the specified GUIPoint 
	 */
	public double getX(){
		return myX; 
	}
	/**
	 * @return the y value of the specified GUIPoint
	 */
	public double getY(){
		return myY; 
	}
	/**
	 * print method used for debugging
	 */
	public void print(){
		System.out.print(this.getX() + ", " + this.getY());
		System.out.println();
	}
}
