package cellsociety_team09;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rule {
	private int myNumStates;
	private Map<Integer, Integer>[][] myNextStateMap;
	private List<Point> myNeighborOffsets;
	
	public Rule(int numStates, Map<Integer, Integer>[][] stateRules, List<Point> neighborRules) {
		myNumStates = numStates;
		myNextStateMap = stateRules;
		myNeighborOffsets = neighborRules;
	}
	
	public List<Point> getNeighbors(Point coords) {
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
		int[] neighborCounts = getStateCounts(neighborStates);
		
		int nextState = myState;
		for(int neighborState = 0; neighborState < myNumStates; neighborState++){
			if(myNextStateMap[myState][neighborState].containsKey(neighborCounts[neighborState])) {
				nextState = myNextStateMap[myState][neighborState].get(neighborCounts[neighborState]);
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
}
