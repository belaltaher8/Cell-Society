package cs.view;

import cs.model.Point;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Hexagon extends Polygon {
	Polygon h;  
	public Hexagon (GUIPoint a, GUIPoint b, GUIPoint c, GUIPoint d, GUIPoint e, GUIPoint f){
		h = new Polygon(); 
		h.getPoints().addAll(new Double[]{ a.getX(),a.getY(),
				b.getX(), b.getY(),
				c.getX(), c.getY(),
				d.getX(), d.getY(), 
				e.getX(), e.getY(), 
				f.getX(), f.getY()}); 
	}
	/**
	 * @return the polygon associated with the Hexagon object
	 */
	public Shape getHexagon(){
		return h; 
	}

}
