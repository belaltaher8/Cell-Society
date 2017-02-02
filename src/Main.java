import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;

import cellsociety_team09.configuration.XMLReader;

public class Main extends Application {
	public static final String DATA_FILE_EXTENSION = "*.xml";
	
	@Override
	public void start(Stage primaryStage) throws Exception {		
		FileChooser myChooser = new FileChooser();
		myChooser.setTitle("Open Data File");
		myChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		myChooser.getExtensionFilters().setAll(new ExtensionFilter("Text Files", DATA_FILE_EXTENSION));
		File dataFile = myChooser.showOpenDialog(primaryStage);
		
		if(dataFile != null) {
			XMLReader fileReader = new XMLReader(dataFile);
		}
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}
