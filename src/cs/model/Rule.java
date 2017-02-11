package cs.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cs.configuration.ConfigDoc;

public class Rule {
	private Random myRNG;
	private ConfigDoc myConfig;
	
	private Collection<Point> myNeighborOffsets;
	private Map<Integer, Double> myProbOfTransition;
	private Map<Triple, Integer> myNextStateMap;
	
	public Rule(ConfigDoc config, Collection<Point> neighborRules, Map<Integer, Double> transitionProbabilities, Map<Triple, Integer> stateRules) {
		myRNG = new Random();
		myConfig = config;
		
		myNeighborOffsets = neighborRules;
		myProbOfTransition = transitionProbabilities;
		myNextStateMap = stateRules;
	}
	
	public int getNumStates() {
		return myConfig.getNumStates();
	}
	
	public Collection<Point> getNeighborCoords(Point cellCoords) {
		Collection<Point> neighbors = new ArrayList<Point>();
		for(Point offset : myNeighborOffsets) {
			Point neighbor = new Point(cellCoords.getX() + offset.getX(),
									   cellCoords.getY() + offset.getY());
			
			if(isWithinXBounds(neighbor) && isWithinYBounds(neighbor)) {
				neighbors.add(neighbor);
			} else if(myConfig.getGridEdge().equals(ConfigDoc.GRID_EDGE_TOROIDAL)) {
				neighbors.add(this.wrapPointAroundEdge(neighbor));
			}
		}
		return neighbors;
	}
	
	public int getNextState(int myState, Collection<Integer> neighborStates) {
		if(myNextStateMap == null) {
			return myState;
		}
		
		int nextState = myState;
		
		List<Integer> neighborCounts = getStateCounts(neighborStates);
		
		for(int stateX = 0; stateX < myConfig.getNumStates(); stateX++){
			int numNeighborsWithStateX = neighborCounts.get(stateX);
			Triple condition = new Triple(myState, stateX, numNeighborsWithStateX);
			
			if(transitionShouldOccur(myNextStateMap, condition)) {
				nextState = myNextStateMap.get(condition);
				break;
			}
		}

		return nextState;
	}
	
	/**
	 * Given a collection of states, counts the number of times each state appears, and
	 * returns the result as a List. (stateCounts[0] is the the number of times 
	 * '0' appears in the collection)
	 * 
	 * @param states : collection of cell states
	 * @return array whose entries are the number of times each state appears 
	 */
	private List<Integer> getStateCounts(Collection<Integer> states) {
		List<Integer> stateCounts = new ArrayList<Integer>(Collections.nCopies(myConfig.getNumStates(), 0));
		for(Integer st : states) {
			//Increments the counter for each state
			stateCounts.set(st, (stateCounts.get(st) + 1));
		}
		return stateCounts;
	}
	
	private boolean transitionShouldOccur(Map<Triple, Integer> transitionRules, Triple key) {
		return transitionRules.containsKey(key) && 
			   transitionSucceeds(transitionRules.get(key));
	}
	
	private boolean transitionSucceeds(int newState) {
		return (myProbOfTransition == null || 
				myRNG.nextDouble() <= myProbOfTransition.get(newState));
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
