package cellsociety_team09.configuration;

import cellsociety_team09.model.Point;
import cellsociety_team09.model.Rule;
import cellsociety_team09.model.Triple;

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

public class XMLReader {
	private static final int FIRST_OCCURRENCE_IN_FILE = 0;
	
    private static final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();
    private Element rootElement;
    private MapMaker myMapMaker;
    
    public XMLReader(File file) {
    	myMapMaker = new MapMaker();
    	loadNewFile(file);
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
    
    public String getSimulationName() {
    	return getString("SIM_NAME", FIRST_OCCURRENCE_IN_FILE);
    }
    
    public String getCellType() {
    	return getString("CELL_TYPE", FIRST_OCCURRENCE_IN_FILE);
    }
    
    public int getGridWidth() {
    	return getInt("GRID_WIDTH", FIRST_OCCURRENCE_IN_FILE);
    }
    
    public int getGridHeight() {
    	return getInt("GRID_HEIGHT", FIRST_OCCURRENCE_IN_FILE);
    }
    
    public int getInitialState(Point point) {
    	String state = getText(String.format("INITIAL_STATE_%s_%s", point.getX(), point.getY()), 0);
    	if(state != null) {
    		return Integer.parseInt(state);
    	} else {
    		return 0;
    	}
    }
    
    public Rule getRule() {
    	int myNumStates = getInt("NUM_STATES", FIRST_OCCURRENCE_IN_FILE);
    	int gridWidth = getGridWidth();
    	int gridHeight = getGridHeight();
    	
    	Map<Triple,Integer> myNextStateMap = myMapMaker.getNextStateMap(getText("nextStateMap", FIRST_OCCURRENCE_IN_FILE));
    	Collection<Point> myNeighborOffsets = myMapMaker.getNeighborOffsets(getText("neighborOffsets", FIRST_OCCURRENCE_IN_FILE));

    	Map<Integer, Double> transitionProbabilities = null;
    	if(getText("transitionProbabilitiesMap", FIRST_OCCURRENCE_IN_FILE) != null) {
    		transitionProbabilities = myMapMaker.getProbabilitiesMap(getText("transitionProbabilitiesMap", FIRST_OCCURRENCE_IN_FILE));
    	} 
    	
    	return new Rule(myNumStates, gridWidth, gridHeight, myNeighborOffsets, transitionProbabilities, myNextStateMap);
    }
    
    private String getString(String tag, int index) {
    	String myString = getText(tag, index);
    	if(myString == null) {
    		throw new XMLException("XMLReader could not find: getString(%s, %d)", tag, index);
    	}
    	return myString;
    }
    
    private int getInt(String tag, int index) {
    	String myInt = getText(tag, index);
    	if(myInt == null) {
    		throw new XMLException("XMLReader could not find: getInt(%s, %d)", tag, index);
    	}
    	return Integer.parseInt(myInt);
    }
    
    private String getText(String tag, int index) {
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
