package cellsociety_team09.model;

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
	
	public int getX() {
		return myX;
	}
	public int getY() {
		return myY;
	}
}
