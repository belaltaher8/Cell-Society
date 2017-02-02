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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
    		NodeList attribute = rootElement.getElementsByTagName(field);
        	if(attribute != null && attribute.getLength() > 0) {
        		
        	}
    	}
    	*/
    	
    	int myNumStates = getInt("NUM_STATES", 0);
    	Map<Triple,Integer> myNextStateMap = new HashMap<Triple,Integer>();
    	myNextStateMap.put(new Triple(0,1,3), 1);
    	myNextStateMap.put(new Triple(1,1,0), 0);
    	myNextStateMap.put(new Triple(1,1,1), 0);
    	myNextStateMap.put(new Triple(1,1,4), 0);
    	myNextStateMap.put(new Triple(1,1,5), 0);
    	myNextStateMap.put(new Triple(1,1,6), 0);
    	myNextStateMap.put(new Triple(1,1,7), 0);
    	myNextStateMap.put(new Triple(1,1,8), 0);
    	List<Point> myNeighborOffsets = new ArrayList<Point>();
    	myNeighborOffsets.add(new Point(1,1));
    	myNeighborOffsets.add(new Point(1,0));
    	myNeighborOffsets.add(new Point(1,-1));
    	myNeighborOffsets.add(new Point(0,1));
    	myNeighborOffsets.add(new Point(0,-1));
    	myNeighborOffsets.add(new Point(-1,1));
    	myNeighborOffsets.add(new Point(-1,0));
    	myNeighborOffsets.add(new Point(-1,-1));
    	
    	return new Rule(myNumStates, myNextStateMap, myNeighborOffsets);
    }
    
    public int getWidth() {
    	return getInt("GRID_WIDTH", 0);
    }
    
    public int getHeight() {
    	return getInt("GRID_HEIGHT", 0);
    }
    
    public int[][] getInitialGrid() {
    	//TODO
    	return null;
    }
    
    private double getDouble(String field, int index) {
    	NodeList myDouble = rootElement.getElementsByTagName(field);
    	return Double.parseDouble(myDouble.item(index).getTextContent());
    }
    
    private int getInt(String field, int index) {
    	NodeList myInt = rootElement.getElementsByTagName(field);
    	return Integer.parseInt(myInt.item(index).getTextContent());
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
