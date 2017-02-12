package cs.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import cs.model.Cell;
import cs.model.Point;

public class ConfigDoc {
	public static final String SIM_TYPE_GAME_OF_LIFE = "GameOfLifeSim";
	public static final String SIM_TYPE_FIRE_SPREAD = "FireSpreadSim";
	public static final String SIM_TYPE_SEGREGATION = "SegregationSim";
	public static final String SIM_TYPE_PRED_PREY = "PredatorPreySim";
	
	public static final String GRID_SHAPE_SQUARE = "Square";
	public static final String GRID_SHAPE_TRIANGLE = "Triangle";
	public static final String GRID_SHAPE_HEXAGON = "Hexagon";
	
	public static final String GRID_EDGE_FINITE = "Finite";
	public static final String GRID_EDGE_TOROIDAL = "Toroidal";
	public static final String GRID_EDGE_INFINITE = "Infinite";
	
	public static final String INIT_STYLE_SPECIFIC = "Specific";
	public static final String INIT_STYLE_RAND = "Random";
	public static final String INIT_STYLE_PROB = "Probability";
	
	private XMLReader myReader;
	private XMLHelper myHelper;
	
	private String mySimName;
	private String myGridShape;
	private String myGridEdge;
	private String myInitializationStyle;
	private int myNumStates;
	private int myGridWidth;
	private int myGridHeight;
	
	private Collection<Point> myNeighborOffsets;
	private List<Double> myInitialDensities;
	
	public ConfigDoc(XMLReader reader) throws XMLException {
		myReader = reader;
		myHelper = new XMLHelper();
		
		this.initParams();
		this.initDensities();
		this.initNeighbors();
	}
	
	protected XMLReader getReader() {
		return myReader;
	}
	
	protected void initParams() throws XMLException {
		try {
			myNumStates = myReader.getInt("NUM_STATES", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			myNumStates = 0;
			throw new XMLException("Unspecified number of cell states for this simulation in the XML file.", e);
		}
			
		try {
			mySimName = myReader.getString("SIM_NAME", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			mySimName = "Cell Society";
		}
		
		try {
			myGridShape = myReader.getString("GRID_SHAPE", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			myGridShape = GRID_SHAPE_SQUARE;
		}
		
		try {
			myGridEdge = myReader.getString("GRID_EDGE", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			myGridEdge = GRID_EDGE_FINITE;
		}
		
		try {
			myInitializationStyle = myReader.getString("INIT_STYLE", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			myInitializationStyle = INIT_STYLE_RAND;
		}
		
		try {
			myGridWidth = myReader.getInt("GRID_WIDTH", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			myGridWidth = 50;
		}
		
		try {
			myGridHeight = myReader.getInt("GRID_HEIGHT", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			myGridHeight = 50;
		}
	}
	
	private void initDensities() throws XMLException {
		myInitialDensities = new ArrayList<Double>();
		for(int state = 0; state < myNumStates; state++) {
			try {
				myInitialDensities.add(myReader.getDouble(String.format("DENSITY_STATE_%d", state), XMLReader.FIRST_OCCURRENCE_IN_FILE));
			} catch(XMLException | NumberFormatException e) {
				if(myInitializationStyle.equals("Probability")) {
					throw new XMLException("Unspecified population densities in the XML file.",e);
				} else {
					myInitialDensities.add(0.0);
				}
			}
		}
	}
	
	private void initNeighbors() {
    	try {
    		String fullText = myReader.getString("neighborOffsets", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    		myNeighborOffsets = myHelper.getNeighborOffsets(fullText);
    	} catch(XMLException e) {
    		//TODO: fix this
    		Point[] defaultNeighbors = new Point[]{new Point(0,1), new Point(0,-1), new Point(1,0), new Point(-1,0)};
    		myNeighborOffsets = Arrays.asList(defaultNeighbors);
    	}
	}
	
	public String getSimulationName() {
    	return mySimName;
    }
    public String getSimType() {
    	return myReader.getSimType();
    }
    public int getNumStates() {
    	return myNumStates;
    }
    public String getGridShape() {
    	return myGridShape;
    }
    public String getGridEdge() {
    	return myGridEdge;
    }
    public String getInitializationStyle() {
    	return myInitializationStyle;
    }
    public int getGridWidth() {
    	return myGridWidth;
    }
    public int getGridHeight() {
    	return myGridHeight;
    }
    public Collection<Point> getNeighborOffsets() {
    	return myNeighborOffsets;
    }
    public Double getInitialStateDensity(int state) {
    	return myInitialDensities.get(state);
    }
    
    public void setGridWidth(int width) {
    	myGridWidth = width;
    }
    public void setGridHeight(int height) {
    	myGridHeight = height;
    }
    public void setGridEdge(String type) {
    	myGridEdge = type;
    }
    public void setGridShape(String shape) {
    	myGridShape = shape;
    }
    
    public int getInitialStateAt(Point point) {
    	int state;
    	try {
    		state = myReader.getInt(String.format("INITIAL_STATE_%s_%s", point.getX(), point.getY()), 0);
    	} catch (XMLException e) {
    		state = Cell.DEFAULT_STATE;
    	}
    	
    	if(state < Cell.DEFAULT_STATE || state >= myNumStates) {
    		state = Cell.DEFAULT_STATE;
    	}
    	
    	return state;
    }
    
    public String getParamsAsXML() {
    	String params = "";
    	params += formatWithXMLTags("SIM_NAME", mySimName);
    	params += formatWithXMLTags("SIM_TYPE", this.getSimType());
    	params += formatWithXMLTags("NUM_STATES", Integer.toString(myNumStates));
    	params += formatWithXMLTags("GRID_SHAPE", myGridShape);
    	params += formatWithXMLTags("GRID_EDGE", myGridEdge);
    	params += formatWithXMLTags("INIT_STYLE", INIT_STYLE_SPECIFIC);
    	params += formatWithXMLTags("GRID_WIDTH", Integer.toString(myGridWidth));
    	params += formatWithXMLTags("GRID_HEIGHT", Integer.toString(myGridHeight));
    	return params;
    }
    
    public String getNeighborsAsXML() {
    	String result = "\t<neighborOffsets>\n";
    	try {
    		String fullText = myReader.getString("neighborOffsets", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    		String[] lines = fullText.trim().replaceAll("[\t]", "").split("[\n]");
    		for(String s : lines) {
    			result += formatWithXMLTags("nbr",s);
    		}
    	} catch(XMLException e) {
    		//TODO: make neighbors from list
    	}
    	result += "\t</neighborOffsets>\n";
    	return result;
    }
    
    public String formatWithXMLTags(String tag, String content) {
    	return String.format("<%s>%s</%s>\n", tag, content, tag);
    }
}
