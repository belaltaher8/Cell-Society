/**
 * @author jaydoherty
 * This class holds the main method that makes an instance of the GUI and gets everything started.
 */

import javafx.application.Application;
import javafx.stage.Stage;
import cs.view.GUIController;

public class Main extends Application {
	public static final String DATA_FILE_EXTENSION = "*.xml";
	
	@Override
	public void start(Stage primaryStage) throws Exception {		
		GUIController theController = new GUIController(primaryStage);
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}
