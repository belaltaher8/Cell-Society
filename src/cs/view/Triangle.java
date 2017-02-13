package cs.view;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Triangle {
	Polygon t; 
	
	public Triangle(GUIPoint a, GUIPoint b, GUIPoint c){
		t = new Polygon();
		t.getPoints().addAll(new Double[]{ a.getX(),a.getY(),
				b.getX(), b.getY(),
				c.getX(), c.getY()});

	}
	
	public Shape getTriangle(){
		return t; 
	}
}
