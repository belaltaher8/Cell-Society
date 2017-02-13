package cs.configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cs.configuration.configs.FireDoc;
import cs.configuration.configs.PredatorPreyDoc;
import cs.configuration.configs.SegregationDoc;
import cs.view.GUIController;


/**
 * @author jaydoherty
 * This class takes an XML file and starts the process of parsing it. It contains methods for getting 
 * Strings, integers, and doubles from the file. It throws XMLExceptions if any of these methods are unable
 * to find the tag in the XML file.
 */
public class XMLReader {
	public static final int FIRST_OCCURRENCE_IN_FILE = 0;
	
	private ResourceBundle myResources;
    private static final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();
    
    private Element rootElement;
    private String mySimType;
    
    public XMLReader(File file) throws XMLException {
    	myResources = ResourceBundle.getBundle(GUIController.DEFAULT_RESOURCE_PACKAGE + "GUI");	
    	this.loadNewFile(file);
    	try {
    		mySimType = getString("SIM_TYPE", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    	} catch(XMLException e) {
			mySimType = "";
			throw new XMLException(myResources.getString("ErrorNoSim"), e);
		}
    }
    
    
    public void loadNewFile(File file) throws XMLException {
    	try {
            DOCUMENT_BUILDER.reset();
            Document xmlDocument = DOCUMENT_BUILDER.parse(file);
            rootElement = xmlDocument.getDocumentElement();
            if(!isValidDataFile(rootElement)) {
            	throw new XMLException(myResources.getString("ErrorInvalidFile"));
            }
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }
    
    public ConfigDoc getConfigDoc() throws XMLException {
    	//ugly if-statement to choose what kind of ConfigDoc to return
    	if(mySimType.equals(ConfigDoc.SIM_TYPE_PRED_PREY)){
    		return new PredatorPreyDoc(this);
    	} else if(mySimType.equals(ConfigDoc.SIM_TYPE_FIRE_SPREAD)) {
    		return new FireDoc(this);
    	} else if(mySimType.equals(ConfigDoc.SIM_TYPE_SEGREGATION)) {
    		return new SegregationDoc(this);
    	} else {
    		return new ConfigDoc(this);
    	}
    }
    
    public String getSimType() {
    	return mySimType;
    }
    public void setSimType(String type) {
    	mySimType = type;
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
    	throw new XMLException(String.format(myResources.getString("ErrorTagNotFound"), tag));
    }
    
    public void writeToFile(String params, String neighbors, String states) throws XMLException {
    	Writer writer = null;
    	try  {
    		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + "/data/snapshot.xml"), "utf-8"));
    		writer.write(formatAsXMLDoc(params, neighbors, states));
    	} catch(IOException e) {
    		throw new XMLException(e);
    	} finally {
    		if(writer != null) {
    			try {writer.close();} catch(IOException e) {/*TODO:can ignore?*/}
    		}
    	}
    }
    
    private String formatAsXMLDoc(String params, String rule, String gridStates) {
    	String result = "";
    	String prefix = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
    				  + "<data type=\"CellSociety\">\n";
    	String suffix = "</data>";
    	
    	result += prefix;
    	result += formatAsXMLElement("parameters", params);
    	result += formatAsXMLElement("Rule", rule);
    	result += formatAsXMLElement("initialGridStates", gridStates);
    	result += suffix;
    	return result;
    }
    
    private String formatAsXMLElement(String tag, String contents) {
    	return String.format("\n\t<%s>\n%s\t</%s>\n\n", tag, contents, tag);
    }
    
    private boolean isValidDataFile(Element root) {
    	return root.getAttribute("type").equals(myResources.getString("ValidDataFile"));
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
