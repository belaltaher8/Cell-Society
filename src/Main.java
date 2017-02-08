import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

import cs.configuration.XMLReader;
import cs.model.Simulation;
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
