package cellsociety_team09.view;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class Display {
	Group root; 
	private final int displayX = 600; 
	private final int displayY = 800; 
	private final int controlY = 200; 
	public final int gridY = 600; 
	public Display(){
		root = new Group();
	}
	public Group getRoot(){
		return root; 
	}
	// create control HBOX
	private HBox createControls(){
		HBox controlPane = new HBox(); 
		controlPane.setPrefWidth(displayX);
		controlPane.setPrefHeight(controlY);
		return controlPane;
	}
	// create Grid part + Animate 
}
