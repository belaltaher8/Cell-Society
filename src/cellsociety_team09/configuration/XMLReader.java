package cellsociety_team09.configuration;

import cellsociety_team09.model.Rule;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLReader {
    private static final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();
    private File myFile;
    //private Document myDoc
    
    public XMLReader(File file) {
    	loadNewFile(file);
    }
    
    public void loadNewFile(File file) {
    	try {
            DOCUMENT_BUILDER.reset();
            Document xmlDocument = DOCUMENT_BUILDER.parse(file);
            myFile = file;
            //myDoc = xmlDocument;
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }
    
    public Rule getRule() {
    	//TODO:
    	return null;
    }
    
    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }
}
