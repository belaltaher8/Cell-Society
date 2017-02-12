package cs.view;

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
	public double getX(){
		return myX; 
	}
	public double getY(){
		return myY; 
	}
	public void setX(double x){
		myX = x; 
	}
	public void setY(double y){
		myY = y; 
	}
}
