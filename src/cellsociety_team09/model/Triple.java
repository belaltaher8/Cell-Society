package cellsociety_team09.model;

public class Triple {
	int myX, myY, myZ;
	
	public Triple(int x, int y, int z) {
		myX = x;
		myY = y;
		myZ = z;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o != null &&
				o instanceof Triple &&
				myX == ((Triple)o).getX() &&
				myY == ((Triple)o).getY() &&
				myZ == ((Triple)o).getZ());
	}
	
	@Override
	public int hashCode() {
		return ((myX ^ myY) ^ myZ);
	}
	
	public int getX() {
		return myX;
	}
	public int getY() {
		return myY;
	}
	public int getZ() {
		return myZ;
	}
}
