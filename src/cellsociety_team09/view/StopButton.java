package cellsociety_team09.view;

import javafx.scene.control.Button;

public class StopButton extends GUIButton {
	Button btn; 
	public StopButton(){
		super("Stop", 200, 300);
		btn = super.getButton(); 
		btn.setOnMouseClicked(e -> handleClick(e.getX(), e.getY()));
	}
	private void handleClick(double x, double y) {
		// stop the animation
	}
}
