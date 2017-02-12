package cs.view;

import cs.model.Point;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Hexagon extends Polygon {
	Polygon h;  
	public Hexagon (Point a, Point b, Point c, Point d, Point e, Point f){
		h = new Polygon(); 
		h.getPoints().addAll(new Double[]{(double) a.getX(), (double) a.getY(),
				(double) b.getX(), (double) b.getY(),
				(double) c.getX(), (double) c.getY(),
				(double) d.getX(), (double) d.getY(),
				(double) e.getX(), (double) e.getY(),
				(double) f.getX(), (double) f.getY(),}); 
	}
	public Shape getHexagon(){
		return h; 
	}

}
