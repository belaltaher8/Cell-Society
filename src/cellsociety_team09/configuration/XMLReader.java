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
    private static final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();
    private Element rootElement;
    
    public XMLReader(File file) {
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
    
    public Rule getRule() {
    	/*
    	for (String field : Rule.DATA_FIELDS) {

    	}
    	*/
    	
    	int myNumStates = getInt("NUM_STATES", 0);
    	int gridWidth = getWidth();
    	int gridHeight = getHeight();
    	Map<Triple,Integer> myNextStateMap = new HashMap<Triple,Integer>();
    	
    	myNextStateMap.put(new Triple(0,1,3), 1);
    	myNextStateMap.put(new Triple(1,1,0), 0);
    	myNextStateMap.put(new Triple(1,1,1), 0);
    	myNextStateMap.put(new Triple(1,1,4), 0);
    	myNextStateMap.put(new Triple(1,1,5), 0);
    	myNextStateMap.put(new Triple(1,1,6), 0);
    	myNextStateMap.put(new Triple(1,1,7), 0);
    	myNextStateMap.put(new Triple(1,1,8), 0);
    	Collection<Point> myNeighborOffsets = new ArrayList<Point>();
    	myNeighborOffsets.add(new Point(1,1));
    	myNeighborOffsets.add(new Point(1,0));
    	myNeighborOffsets.add(new Point(1,-1));
    	myNeighborOffsets.add(new Point(0,1));
    	myNeighborOffsets.add(new Point(0,-1));
    	myNeighborOffsets.add(new Point(-1,1));
    	myNeighborOffsets.add(new Point(-1,0));
    	myNeighborOffsets.add(new Point(-1,-1));
    	Map<Integer, Double> transitionProbabilities = new HashMap<Integer, Double>();
    	transitionProbabilities.put(0, 1.0);
    	transitionProbabilities.put(1, 1.0);
    	
    	return new Rule(myNumStates, gridWidth, gridHeight, myNeighborOffsets, transitionProbabilities, myNextStateMap);
    }
    
    public int getWidth() {
    	return getInt("GRID_WIDTH", 0);
    }
    
    public int getHeight() {
    	return getInt("GRID_HEIGHT", 0);
    }
    
    public int getInitialState(Point point) {
    	String state = getText(String.format("INITIAL_STATE_%s_%s", point.getX(), point.getY()), 0);
    	if(state != null) {
    		return Integer.parseInt(state);
    	} else {
    		return 0;
    	}
    }
    
    private Map<Triple, Integer> getNextStateMap() {
    	NodeList mappings = getNodes("nextStateMap");
    	Map<Triple, Integer> result = new HashMap<Triple, Integer>();
    	for(int i = 0; i < mappings.getLength(); i++) {
    		String entry = mappings.item(i).getTextContent();
    		String[] pair = getKeyValuePair(entry);
    		Triple key = parseTriple(pair[0]);
    		Integer val = Integer.parseInt(pair[1]);
    		result.put(key, val);
    	}
    	return result;
    }
    
    private String[] getKeyValuePair(String entry) {
    	entry = entry.replaceAll("\\s", "");
    	return entry.split("->");
    }
    
    private Triple parseTriple(String xyz) {
    	String[] vals = xyz.split(",");
    	int x = Integer.parseInt(vals[0]);
    	int y = Integer.parseInt(vals[1]);
    	int z = Integer.parseInt(vals[2]);
    	return new Triple(x,y,z);
    }
    
    private double getDouble(String tag, int index) {
    	String myDouble = getText(tag, index);
    	if(myDouble == null) {
    		throw new XMLException("XMLReader could not find: getDouble(%s, %d)", tag, index);
    	}
    	return Double.parseDouble(myDouble);
    }
    
    private int getInt(String tag, int index) {
    	String myInt = getText(tag, index);
    	if(myInt == null) {
    		throw new XMLException("XMLReader could not find: getInt(%s, %d)", tag, index);
    	}
    	return Integer.parseInt(myInt);
    }
    
    private String getText(String tag, int index) {
    	NodeList nodeList = getNodes(tag);
    	if (nodeList != null && nodeList.getLength() > index) {
            return nodeList.item(index).getTextContent();
        }
    	return null;
    }
    
    private NodeList getNodes(String tag) {
    	return rootElement.getElementsByTagName(tag);
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
