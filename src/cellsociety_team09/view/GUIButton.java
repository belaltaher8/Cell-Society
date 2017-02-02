package cellsociety_team09.view;

import javafx.scene.control.Button;

public class GUIButton extends Button{
	Button btn; 
	public GUIButton (String text, int x, int y){
		btn = new Button(text);
		btn.setLayoutX(x);
		btn.setLayoutY(y);
		btn.setOnMouseClicked(e->handleClick(e.getX(), e.getY()));
	}
	public Button getButton(){
		return btn; 
	}
	private void handleClick(double x, double y){
		
	}
}
