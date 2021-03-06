package cs.model;

/**
 * @author jaydoherty
 * This class packages together x and y coordinates in a convenient object. Overrides equals and hashcode to
 * be used in maps. Every cell in the grid has a Point object associated with it.
 */
public class Point {
	private int myX;
	private int myY;
	
	public Point(int x, int y) {
		myX = x;
		myY = y;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o != null &&
				o instanceof Point &&
				myX == ((Point)o).getX() &&
				myY == ((Point)o).getY());
	}
	
	@Override
	public int hashCode() {
		return (myX ^ myY);
	}
	
	public int getX() {
		return myX;
	}
	public int getY() {
		return myY;
	}
}
