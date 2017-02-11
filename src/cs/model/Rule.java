package cs.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Rule {
	private Random myRNG;
	private int myNumStates;
	private int myGridWidth;
	private int myGridHeight;
	
	private Collection<Point> myNeighborOffsets;
	private Map<Integer, Double> myProbOfTransition;
	private Map<Triple, Integer> myNextStateMap;
	
	public Rule(int numStates, int gridWidth, int gridHeight, Collection<Point> neighborRules, Map<Integer, Double> transitionProbabilities, Map<Triple, Integer> stateRules) {
		myRNG = new Random();
		myNumStates = numStates;
		myGridWidth = gridWidth;
		myGridHeight = gridHeight;
		
		myNeighborOffsets = neighborRules;
		myProbOfTransition = transitionProbabilities;
		myNextStateMap = stateRules;
	}
	
	public int getNumStates() {
		return myNumStates;
	}
	
	public Collection<Point> getNeighborCoords(Point cellCoords) {
		Collection<Point> neighbors = new ArrayList<Point>();
		for(Point offset : myNeighborOffsets) {
			Point neighbor = new Point(cellCoords.getX() + offset.getX(),
									   cellCoords.getY() + offset.getY());
			
			if(isWithinBounds(neighbor)) {
				neighbors.add(neighbor);
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
		
		for(int stateX = 0; stateX < myNumStates; stateX++){
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
		List<Integer> stateCounts = new ArrayList<Integer>(Collections.nCopies(myNumStates, 0));
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
	
	private boolean isWithinBounds(Point point) {
		return (point.getX() >= 0 && point.getX() < myGridWidth &&
				point.getY() >= 0 && point.getY() < myGridHeight);
	}
}
