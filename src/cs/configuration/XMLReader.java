package cs.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cs.model.Cell;
import cs.model.Point;
import cs.model.Rule;
import cs.model.Triple;

public class XMLReader {
	public static final int FIRST_OCCURRENCE_IN_FILE = 0;
	
    private static final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();
    private Element rootElement;
    private MapMaker myMapMaker;
    private ConfigDoc myConfigDoc;
    
    public XMLReader(File file) {
    	loadNewFile(file);
    	myMapMaker = new MapMaker();
    	myConfigDoc = new ConfigDoc(this);
    }
    
    public void loadNewFile(File file) {
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
    
    public ConfigDoc getConfigParameters() {
    	return myConfigDoc;
    }
    
    public void setConfigType(String name) {
    	//TODO: if-statement that changes myConfigDoc to a subclass
    }
    
    public String getSimulationName() {
    	return getString("SIM_NAME", FIRST_OCCURRENCE_IN_FILE);
    }
    public String getSimType() {
    	return getString("SIM_TYPE", FIRST_OCCURRENCE_IN_FILE);
    }
   
    public int getGridWidth() {
    	return getInt("GRID_WIDTH", FIRST_OCCURRENCE_IN_FILE);
    }
    public int getGridHeight() {
    	return getInt("GRID_HEIGHT", FIRST_OCCURRENCE_IN_FILE);
    }
    
    public Rule getRule() {
    	int myNumStates = getInt("NUM_STATES", FIRST_OCCURRENCE_IN_FILE);
    	int gridWidth = getGridWidth();
    	int gridHeight = getGridHeight();
    	
    	Map<Triple,Integer> myNextStateMap = myMapMaker.getNextStateMap(getTextByTag("nextStateMap", FIRST_OCCURRENCE_IN_FILE));
    	Collection<Point> myNeighborOffsets = myMapMaker.getNeighborOffsets(getTextByTag("neighborOffsets", FIRST_OCCURRENCE_IN_FILE));

    	Map<Integer, Double> transitionProbabilities = null;
    	if(getTextByTag("transitionProbabilitiesMap", FIRST_OCCURRENCE_IN_FILE) != null) {
    		transitionProbabilities = myMapMaker.getProbabilitiesMap(getTextByTag("transitionProbabilitiesMap", FIRST_OCCURRENCE_IN_FILE));
    	} 
    	
    	return new Rule(myNumStates, gridWidth, gridHeight, myNeighborOffsets, transitionProbabilities, myNextStateMap);
    }
    
    //TODO: this is poor design. Requires simulation to know xml tags
    public int getIntParameter(String name) {
    	return getInt(name, FIRST_OCCURRENCE_IN_FILE);
    }
    public double getDoubleParameter(String name) {
    	return getDouble(name, FIRST_OCCURRENCE_IN_FILE);
    }
    
    public int getInitialStateAt(Point point) {
    	int state;
    	try {
    		state = getInt(String.format("INITIAL_STATE_%s_%s", point.getX(), point.getY()), 0);
    	} catch (XMLException e) {
    		state = Cell.DEFAULT_STATE;
    	}
    	return state;
    }
    
    public String getString(String tag, int index) throws XMLException {
    	String myString = getTextByTag(tag, index);
    	if(myString == null) {
    		throw new XMLException("XMLReader could not find: getString(%s, %d)", tag, index);
    	}
    	return myString;
    }
    
    public int getInt(String tag, int index) throws XMLException {
    	String myInt = getTextByTag(tag, index);
    	if(myInt == null) {
    		throw new XMLException("XMLReader could not find: getInt(%s, %d)", tag, index);
    	}
    	return Integer.parseInt(myInt);
    }
    
    public double getDouble(String tag, int index) throws XMLException {
    	String myDouble = getTextByTag(tag, index);
    	if(myDouble == null) {
    		throw new XMLException("XMLReader could not find: getDouble(%s, %d)", tag, index);
    	}
    	return Double.parseDouble(myDouble);
    }
    
    private String getTextByTag(String tag, int index) {
    	NodeList nodeList = rootElement.getElementsByTagName(tag);
    	if (nodeList != null && nodeList.getLength() > index) {
            return nodeList.item(index).getTextContent();
        }
    	return null;
    }

    private boolean isValidDataFile(Element root) {
    	return root.getAttribute("type").equals("CellSociety");
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
