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
    
    
    /**
     * This method loads a new file for parsing.
     * @param file : a new xml file to read
     * @throws XMLException : if the input file is not formatted properly
     */
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
    
    
    /**
     * This method tries to initialize a ConfigDoc with all of the initial parameters for the
     * simulation. It determines which type of ConfigDoc to make from the XML file.
     * @return ConfigDoc for the simulation
     * @throws XMLException : if the ConfigDoc cannot be initialized for any reason
     */
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
    
    
    /**
     * @return the simulation being run according to the XML file
     */
    public String getSimType() {
    	return mySimType;
    }
    
   
    /**
     * This method looks through the XML file for the element specified by its tag. If successful,
     * it returns the content as a String.
     * @param tag : the xml tag to look for in the file
     * @param index : specifies the i-th occurrence of this tag in the file
     * @return the String content of the tag, if found
     * @throws XMLException : if no tag match is found
     */
    public String getString(String tag, int index) throws XMLException {
    	return getTextByTag(tag, index);
    }
    
    /**
     * This method looks through the XML file for the element specified by its tag. If successful,
     * it returns the content casted to an Integer.
     * @param tag : the xml tag to look for in the file
     * @param index : specifies the i-th occurrence of this tag in the file
     * @return the Integer content of the tag, if found
     * @throws XMLException : if no tag match is found
     */
    public int getInt(String tag, int index) throws XMLException {
    	return Integer.parseInt(getTextByTag(tag, index));
    }
    
    /**
     * This method looks through the XML file for the element specified by its tag. If successful,
     * it returns the content as a Double.
     * @param tag : the xml tag to look for in the file
     * @param index : specifies the i-th occurrence of this tag in the file
     * @return the Double content of the tag, if found
     * @throws XMLException : if no tag match is found
     */
    public double getDouble(String tag, int index) throws XMLException {
    	return Double.parseDouble(getTextByTag(tag, index));
    }
    
    /**
     * This method looks through the XML file for the element specified by its tag. If successful,
     * it returns the content as a String.
     * @param tag : the xml tag to look for in the file
     * @param index : specifies the i-th occurrence of this tag in the file
     * @return the String content of the tag, if found
     * @throws XMLException : if no tag match is found
     */
    private String getTextByTag(String tag, int index) throws XMLException {
    	NodeList nodeList = rootElement.getElementsByTagName(tag);
    	if (nodeList != null && nodeList.getLength() > index) {
            return nodeList.item(index).getTextContent();
        }
    	throw new XMLException(String.format(myResources.getString("ErrorTagNotFound"), tag));
    }
    
    
    /**
     * This method takes several properly formatted strings, concatenates them, and writes them to a file.
     * @param params : This is the current state of the simulation parameters, obtained from ConfigDoc
     * @param neighbors : This is the list of neighbor offsets
     * @param states : This is a complete specification of the current state of every cell in the grid.
     * @throws XMLException : if it encounters any I/O errors
     */
    public void writeToFile(String params, String neighbors, String states) throws XMLException {
    	Writer writer = null;
    	try  {
    		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + "/data/snapshot.xml"), "utf-8"));
    		writer.write(formatAsXMLDoc(params, neighbors, states));
    	} catch(IOException e) {
    		throw new XMLException(e);
    	} finally {
    		if(writer != null) {
    			try {writer.close();} catch(IOException e) {/*can ignore?*/}
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
