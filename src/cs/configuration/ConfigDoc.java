package cs.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cs.model.Cell;
import cs.model.Point;
import cs.model.Rule;
import cs.model.Triple;

public class ConfigDoc {
	public static final String SIM_TYPE_DEFAULT = "Default";
	public static final String SIM_TYPE_MOVING = "MovingSim";
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
	private MapMaker myMapMaker;
	
	private String mySimName;
	private String myGridShape;
	private String myGridEdge;
	private String myInitializationStyle;
	private int myNumStates;
	private int myGridWidth;
	private int myGridHeight;
	
	private Rule myRule;
	private List<Double> myInitialDensities;
	
	public ConfigDoc(XMLReader reader) throws XMLException {
		myReader = reader;
		myMapMaker = new MapMaker();
		
		this.initParams();
		this.initDensities();
		this.initRule();
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
	
	private void initRule() throws XMLException {
    	Map<Triple,Integer> myNextStateMap;
    	Map<Integer, Double> transitionProbabilities;
    	Collection<Point> myNeighborOffsets;
    	
    	try {
    		String nsmFullText = myReader.getString("nextStateMap", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    		myNextStateMap = myMapMaker.getNextStateMap(nsmFullText);
    	} catch(XMLException e) {
    		myNextStateMap = null;
    	}
    	
    	try {
        	String probFullText = myReader.getString("transitionProbabilitiesMap", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    		transitionProbabilities = myMapMaker.getProbabilitiesMap(probFullText);
    	} catch(XMLException e) {
    		transitionProbabilities = null;
    	}
    	
    	try {
    		String noFullText = myReader.getString("neighborOffsets", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    		myNeighborOffsets = myMapMaker.getNeighborOffsets(noFullText);
    	} catch(XMLException e) {
    		//TODO: fix this
    		Point[] defaultNeighbors = new Point[]{new Point(0,1), new Point(0,-1), new Point(1,0), new Point(-1,0)};
    		myNeighborOffsets = Arrays.asList(defaultNeighbors);
    	}
    	
    	myRule = new Rule(this, myNeighborOffsets, transitionProbabilities, myNextStateMap);
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
    public Rule getRule() {
    	return myRule;
    }
    public Double getInitialStateDensity(int state) {
    	return myInitialDensities.get(state);
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
}
