package cellsociety_team09.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Rule {
	private Random myRNG;
	private int myNumStates;
	
	private List<Point> myNeighborOffsets;
	private List<Double> myProbOfTransition;
	private Map<Triple, Integer> myNextStateMap;
	
	
	public Rule(int numStates, List<Point> neighborRules, List<Double> transitionProbabilities, Map<Triple, Integer> stateRules) {
		myRNG = new Random();
		myNumStates = numStates;
		
		myNeighborOffsets = neighborRules;
		myProbOfTransition = transitionProbabilities;
		myNextStateMap = stateRules;
	}
	
	public List<Point> getNeighborCoords(Point coords) {
		List<Point> neighbors = new ArrayList<Point>();
		for(int i = 0; i < myNeighborOffsets.size(); i++) {
			Point neighbor = new Point(coords.getX() + myNeighborOffsets.get(i).getX(),
									   coords.getY() + myNeighborOffsets.get(i).getY());
			//TODO: do bounds checking
			neighbors.add(neighbor);
		}
		return neighbors;
	}
	
	public int getNextState(int myState, List<Integer> neighborStates) {
		int nextState = myState;
		
		int[] neighborCounts = getStateCounts(neighborStates);
		
		for(int stateX = 0; stateX < myNumStates; stateX++){
			int numNeighborsWithStateX = neighborCounts[stateX];
			Triple condition = new Triple(myState, stateX, numNeighborsWithStateX);
			
			if(transitionCanOccur(myNextStateMap, condition)) {
				nextState = myNextStateMap.get(condition);
				if(!transitionSucceeds(nextState)) {
					nextState = myState;
				}
				break;
			}
		}

		return nextState;
	}
	
	/**
	 * Given a List of states, counts the number of times each state appears, and
	 * returns the result as an array. (stateCounts[0] is the the number of times 
	 * '0' appears in the List)
	 * 
	 * @param states : List of cell states
	 * @return array whose entries are the number of times each state appears 
	 */
	private int[] getStateCounts(List<Integer> states) {
		int[] stateCounts = new int[myNumStates];
		for(int i = 0; i < states.size(); i++) {
			stateCounts[states.get(i)]++;
		}
		return stateCounts;
	}
	
	private boolean transitionCanOccur(Map<Triple, Integer> transitions, Triple key) {
		return transitions.containsKey(key);
	}
	
	private boolean transitionSucceeds(int state) {
		return (myRNG.nextDouble() <= myProbOfTransition.get(state));
	}
}
