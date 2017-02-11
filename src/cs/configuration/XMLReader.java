package cs.configuration;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLReader {
	public static final int FIRST_OCCURRENCE_IN_FILE = 0;
	
    private static final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();
    private Element rootElement;
    private String myConfigType;
    
    public XMLReader(File file) throws XMLException {
    	this.loadNewFile(file);
    	myConfigType = "Default";
    }
    
    public void loadNewFile(File file) throws XMLException {
    	try {
            DOCUMENT_BUILDER.reset();
            Document xmlDocument = DOCUMENT_BUILDER.parse(file);
            rootElement = xmlDocument.getDocumentElement();
            if(!isValidDataFile(rootElement)) {
            	throw new XMLException("Invalid input file for Cell Society.");
            }
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }
    
    public ConfigDoc getConfigParameters() throws XMLException {
    	//TODO: ugly if-statement to choose what kind of ConfigDoc to return
    	if(myConfigType.equals("BlahBlahBlah")){
    		return new ConfigDoc(this);
    	} else {
    		return new ConfigDoc(this);
    	}
    }
    
    public void setConfigType(String type) {
    	myConfigType = type;
    }
    
    public String getString(String tag, int index) throws XMLException {
    	return getTextByTag(tag, index);
    }
    
    public int getInt(String tag, int index) throws XMLException {
    	return Integer.parseInt(getTextByTag(tag, index));
    }
    
    public double getDouble(String tag, int index) throws XMLException {
    	return Double.parseDouble(getTextByTag(tag, index));
    }
    
    private String getTextByTag(String tag, int index) throws XMLException {
    	NodeList nodeList = rootElement.getElementsByTagName(tag);
    	if (nodeList != null && nodeList.getLength() > index) {
            return nodeList.item(index).getTextContent();
        }
    	throw new XMLException("XMLReader could not find the simulation parameter: %s", tag);
    }

    private boolean isValidDataFile(Element root) {
    	return root.getAttribute("type").equals("CellSociety");
    }
    
    private static DocumentBuilder getDocumentBuilder() throws RuntimeException {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
        	throw new RuntimeException(e);
        }
    }
}
