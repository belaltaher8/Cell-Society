package cs.configuration;

import java.util.Collection;
import java.util.Map;

import cs.model.Cell;
import cs.model.Point;
import cs.model.Rule;
import cs.model.Triple;

public class ConfigDoc {

	private XMLReader myReader;
	private MapMaker myMapMaker;
	
	public ConfigDoc(XMLReader reader) {
		myReader = reader;
		myMapMaker = new MapMaker();
	}
	
	public String getSimulationName() {
    	return myReader.getString("SIM_NAME", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    }
    public String getSimType() {
    	return myReader.getString("SIM_TYPE", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    }
    public String getGridShape() {
    	return myReader.getString("GRID_SHAPE", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    }
    public int getGridWidth() {
    	return myReader.getInt("GRID_WIDTH", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    }
    public int getGridHeight() {
    	return myReader.getInt("GRID_HEIGHT", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    }
    
    public int getInitialStateAt(Point point) {
    	int state;
    	try {
    		state = myReader.getInt(String.format("INITIAL_STATE_%s_%s", point.getX(), point.getY()), 0);
    	} catch (XMLException e) {
    		state = Cell.DEFAULT_STATE;
    	}
    	return state;
    }
    
    public Rule getRule() {
    	int myNumStates = myReader.getInt("NUM_STATES", XMLReader.FIRST_OCCURRENCE_IN_FILE);
    	int gridWidth = getGridWidth();
    	int gridHeight = getGridHeight();
    	
    	Map<Triple,Integer> myNextStateMap = myMapMaker.getNextStateMap(myReader.getString("nextStateMap", XMLReader.FIRST_OCCURRENCE_IN_FILE));
    	Collection<Point> myNeighborOffsets = myMapMaker.getNeighborOffsets(myReader.getString("neighborOffsets", XMLReader.FIRST_OCCURRENCE_IN_FILE));

    	Map<Integer, Double> transitionProbabilities = null;
    	if(myReader.getString("transitionProbabilitiesMap", XMLReader.FIRST_OCCURRENCE_IN_FILE) != null) {
    		transitionProbabilities = myMapMaker.getProbabilitiesMap(myReader.getString("transitionProbabilitiesMap", XMLReader.FIRST_OCCURRENCE_IN_FILE));
    	} 
    	
    	return new Rule(myNumStates, gridWidth, gridHeight, myNeighborOffsets, transitionProbabilities, myNextStateMap);
    }
}
