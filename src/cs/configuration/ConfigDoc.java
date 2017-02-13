package cs.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import cs.model.Cell;
import cs.model.Point;

/**
 * @author jaydoherty
 * This class is designed to be the central location where all simulation parameters are stored.
 * The idea being that if a parameter gets changed here, both the model and the view will see
 * the change immediately, without having to be reset. This class communicates with the XMLReader
 * to initialize all of the simulation parameters.
 */
/**
 * @author jaydoherty
 *
 */
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
	
	public static final String TURN_OFF_GRID_LINES = "false";
	
	public static final int MIN_GRID_SIZE = 1;
	
	private XMLReader myReader;
	private XMLHelper myHelper;
	
	private String mySimName;
	private String myGridShape;
	private String myGridEdge;
	private String myInitializationStyle;
	private int myGridWidth;
	private int myGridHeight;
	private boolean myGridLinesEnabled;
	
	private Collection<Point> myNeighborOffsets;
	private List<Double> myInitialDensities;
	
	public ConfigDoc(XMLReader reader) throws XMLException {
		myReader = reader;
		myHelper = new XMLHelper();
		
		this.initParams();
		this.initNeighbors();
	}
	
	protected XMLReader getReader() {
		return myReader;
	}
	
	/**
	 * @throws XMLException
	 * This method initializes all of the simulation parameters from the XML file,
	 * and it will try to choose appropriate default values if they are not specified.
	 */
	protected void initParams() throws XMLException {
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
			if(myGridWidth < MIN_GRID_SIZE) {
				myGridHeight = 50;
			}
		} catch(XMLException | NumberFormatException e) {
			myGridWidth = 50;
		}
		
		try {
			myGridHeight = myReader.getInt("GRID_HEIGHT", XMLReader.FIRST_OCCURRENCE_IN_FILE);
			if(myGridHeight < MIN_GRID_SIZE) {
				myGridHeight = 50;
			}
		} catch(XMLException | NumberFormatException e) {
			myGridHeight = 50;
		}
		
		try {
			String gridLines = myReader.getString("GRID_LINES", XMLReader.FIRST_OCCURRENCE_IN_FILE);
			myGridLinesEnabled = !gridLines.equals(TURN_OFF_GRID_LINES);
		} catch(XMLException e) {
			myGridLinesEnabled = true;
		}
	}
	
	
	/**
	 * This method reads the XML file to look for the probability densities of each
	 * state. These values are to be used if the grid is being initialized by the "Probability" option
	 * @param numStates : number of discrete states for this simulation
	 */
	private void initDensities(int numStates) {
		myInitialDensities = new ArrayList<Double>();
		for(int state = 0; state < numStates; state++) {
			try {
				myInitialDensities.add(myReader.getDouble(String.format("DENSITY_STATE_%d", state), XMLReader.FIRST_OCCURRENCE_IN_FILE));
			} catch(XMLException | NumberFormatException e) {
				myInitialDensities.add(0.0);
			}
		}
	}
	
	
	/**
	 * This method initializes the rules for determining neighbors. These rules are specified in the XML
	 * file, and this method makes use of the XMLHelper class to translate the XML text into a list.
	 */
	private void initNeighbors() {
    	try {
    		String fullText = myReader.getString("neighborOffsets", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    		myNeighborOffsets = myHelper.getNeighborOffsets(fullText);
    	} catch(XMLException e) {
    		Point[] defaultNeighbors = new Point[]{new Point(0,1), new Point(0,-1), new Point(1,0), new Point(-1,0)};
    		myNeighborOffsets = Arrays.asList(defaultNeighbors);
    	}
	}
	
	/**
	 * @return the title of the current simulation
	 */
	public String getSimulationName() {
    	return mySimName;
    }
	
    /**
     * @return the type of the current simulation (used to determine Simulation object)
     */
    public String getSimType() {
    	return myReader.getSimType();
    }
    
    /**
     * @return the shape of the grid
     */
    public String getGridShape() {
    	return myGridShape;
    }
    
    /**
     * @return the edge type of the grid (finite, toroidal, etc)
     */
    public String getGridEdge() {
    	return myGridEdge;
    }
    
    /**
     * @return the initialization style (from file, random, or probability-based)
     */
    public String getInitializationStyle() {
    	return myInitializationStyle;
    }
    
    /**
     * @return the width of the grid in cells
     */
    public int getGridWidth() {
    	return myGridWidth;
    }
    
    /**
     * @return the height of the grid in cells
     */
    public int getGridHeight() {
    	return myGridHeight;
    }
    
    /**
     * @return the relative points that should be considered as neighbors
     */
    public Collection<Point> getNeighborOffsets() {
    	return myNeighborOffsets;
    }
    
   
    /**
     * @param state : state to consider
     * @return the initial concentration of that state in the simulation
     */
    public Double getInitialStateDensity(int state) {
    	if(myInitialDensities == null || !myInitialDensities.contains(state)){
    		this.initDensities(state + 1);
    	}
    	return myInitialDensities.get(state);
    }
    
    
    /**
     * @return whether or not the grid lines should display
     */
    public boolean hasGridLines() {
    	return myGridLinesEnabled;
    }
    
    /**
     * @param width of the simulation in cells
     */
    public void setGridWidth(int width) {
    	myGridWidth = width;
    }
    
    /**
     * @param height of the simulation in cells
     */
    public void setGridHeight(int height) {
    	myGridHeight = height;
    }
    
    /**
     * @param type : edge type of the simulation (finite, toroidal)
     */
    public void setGridEdge(String type) {
    	myGridEdge = type;
    }
    
    /**
     * @param shape : display shape of the simulation
     */
    public void setGridShape(String shape) {
    	myGridShape = shape;
    }
    
    /**
     * @param show : enable or disable grid lines on the simulation
     */
    public void setGridLines(boolean show) {
    	myGridLinesEnabled = show;
    }
    
    
    /**
     * @param point : coords at which we want to find the initial state
     * @param numStates : total number of states in this simulation
     * @return the initial state of the point
     */
    public int getInitialStateAt(Point point, int numStates) {
    	int state;
    	try {
    		state = myReader.getInt(String.format("INITIAL_STATE_%s_%s", point.getX(), point.getY()), 0);
    	} catch (XMLException e) {
    		state = Cell.DEFAULT_STATE;
    	}
    	
    	if(state < Cell.DEFAULT_STATE || state >= numStates) {
    		state = Cell.DEFAULT_STATE;
    	}
    	
    	return state;
    }
    
    
    /**
     * @return the contents of this ConfigDoc as XML elements. This is used to write the
     * current configuration to a file.
     */
    public String getParamsAsXML() {
    	String params = "";
    	params += formatWithXMLTags("SIM_NAME", mySimName);
    	params += formatWithXMLTags("SIM_TYPE", this.getSimType());
    	params += formatWithXMLTags("GRID_SHAPE", myGridShape);
    	params += formatWithXMLTags("GRID_EDGE", myGridEdge);
    	params += formatWithXMLTags("GRID_LINES", Boolean.toString(myGridLinesEnabled));
    	params += formatWithXMLTags("INIT_STYLE", INIT_STYLE_SPECIFIC);
    	params += formatWithXMLTags("GRID_WIDTH", Integer.toString(myGridWidth));
    	params += formatWithXMLTags("GRID_HEIGHT", Integer.toString(myGridHeight));
    	return params;
    }
    
    /**
     * @return the neighbor rules as XML elements. This is used to write the
     * current configuration to a file.
     */
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
    
    
    /**
     * @param tag : xml tag
     * @param content : xml value
     * @return wraps the content in xml tags
     */
    public String formatWithXMLTags(String tag, String content) {
    	return String.format("<%s>%s</%s>\n", tag, content, tag);
    }
}
