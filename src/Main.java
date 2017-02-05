import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;

import cellsociety_team09.configuration.XMLReader;
import cellsociety_team09.model.Grid;
import cellsociety_team09.view.GUIController;


public class Main extends Application {
	public static final String DATA_FILE_EXTENSION = "*.xml";
	
	@Override
	public void start(Stage primaryStage) throws Exception {		
		FileChooser myChooser = new FileChooser();
		myChooser.setTitle("Open Data File");
		myChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data"));
		myChooser.getExtensionFilters().setAll(new ExtensionFilter("Text Files", DATA_FILE_EXTENSION));
		File dataFile = myChooser.showOpenDialog(primaryStage);
		
		if(dataFile != null) {
			XMLReader fileReader = new XMLReader(dataFile);
			GUIController theController = new GUIController(fileReader);
			theController.setup(primaryStage);
		}
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}
