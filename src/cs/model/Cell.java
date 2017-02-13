package cs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import cs.configuration.ConfigDoc;


/**
 * @author jaydoherty
 * This is the base class for the cells inside the grid. Cells have a state and an ability to transition between
 * states in a multi-step process that can be synchronized across the gird. Subclasses can overwrite computeNextState
 * to modify behavior. They also contain methods to find their neighbors based on info from the ConfigDoc. 
 */
public class Cell {
	public static final int DEFAULT_STATE = 0;
	
	private int currentState;
	private int nextState;
	private Point myCoords;
	private ConfigDoc myConfig;
	private Simulation mySim;
		
	public Cell(int initialState, Point coordinates, ConfigDoc config, Simulation simulation) {
		currentState = initialState;
		nextState = initialState;
		myCoords = coordinates;
		myConfig = config;
		mySim = simulation;
	}
	
	/**
	 * Switch from current state to calculated state.
	 */
	public void advanceState() {
		currentState = nextState;
	}
	
	
	/**
	 * Computes next state. Default Cells do not change state.
	 * @param neighborStates : collection of the states of all of this cell's neighbors
	 */
	public void computeNextState(Collection<Integer> neighborStates) {
		nextState = currentState;
	}
	
	
	/**
	 * @return : returns a collection of points that represents this Cell's neighborhood
	 */
	public Collection<Point> getNeighborCoords() {
		Collection<Point> neighbors = new ArrayList<Point>();
		for(Point offset : myConfig.getNeighborOffsets()) {
			Point neighbor = this.applyOffset(offset);
			
			if(isWithinXBounds(neighbor) && isWithinYBounds(neighbor)) {
				neighbors.add(neighbor);
			} else if(myConfig.getGridEdge().equals(ConfigDoc.GRID_EDGE_TOROIDAL)) {
				neighbors.add(this.wrapPointAroundEdge(neighbor));
			}
		}
		return neighbors;
	}
	
	/**
	 * @return the state of this cell
	 */
	public int getState() {
		return currentState;
	}
	
	/**
	 * Allows sub-classes to modify their states
	 * @param state : new state
	 */
	protected void setNextState(int state) {
		nextState = state;
	}
	
	/**
	 * @return the location of this cell in the grid
	 */
	public Point getCoords() {
		return myCoords;
	}
	
	/**
	 * @param newCoords : new location of this cell in the grid
	 */
	public void setCoords(Point newCoords) {
		myCoords = newCoords;
	}
	
	protected ConfigDoc getConfig() {
		return myConfig;
	}
	
	protected Simulation getSimulation() {
		return mySim;
	}
	
	private Point applyOffset(Point offset) {
		if(myConfig.getGridShape().equals(ConfigDoc.GRID_SHAPE_TRIANGLE)) {
			if(((myCoords.getX() + myCoords.getY()) % 2) == 0) {
				return new Point(myCoords.getX() + offset.getX(),myCoords.getY() - offset.getY());
			} else {
				return new Point(myCoords.getX() + offset.getX(),myCoords.getY() + offset.getY());
			}
		} else {
			return new Point(myCoords.getX() + offset.getX(),myCoords.getY() - offset.getY());
		}
	}
	
	/**
	 * Given a collection of states, counts the number of times each state appears, and
	 * returns the result as a List. (stateCounts[0] is the the number of times 
	 * '0' appears in the collection)
	 * 
	 * @param states : collection of cell states
	 * @return array whose entries are the number of times each state appears 
	 */
	protected List<Integer> getStateCounts(Collection<Integer> states) {
		List<Integer> stateCounts = new ArrayList<Integer>(Collections.nCopies(mySim.getNumStates(), 0));
		for(Integer st : states) {
			//Increments the counter for each state
			stateCounts.set(st, (stateCounts.get(st) + 1));
		}
		return stateCounts;
	}
	
	private boolean isWithinXBounds(Point point) {
		return (point.getX() >= 0 && point.getX() < myConfig.getGridWidth());
	}
	
	private boolean isWithinYBounds(Point point) {
		return (point.getY() >= 0 && point.getY() < myConfig.getGridHeight());
	}
	
	private Point wrapPointAroundEdge(Point point) {
		int wrappedX = wrapCoordAroundEdge(point.getX(), myConfig.getGridWidth());
		int wrappedY = wrapCoordAroundEdge(point.getY(), myConfig.getGridHeight());
		
		return new Point(wrappedX, wrappedY);
	}
	
	private int wrapCoordAroundEdge(int coord, int max) {
		if(coord < 0) {
			return max - 1;
		} else if(coord >= max){
			return 0;
		} else {
			return coord;
		}
	}
}
