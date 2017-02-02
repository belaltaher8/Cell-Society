package cellsociety_team09.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rule {
	public static final List<String> DATA_FIELDS = Arrays.asList(new String[] {
        "myNumStates",
        "myNextStateMap",
        "myNeighborOffsets",
    });
	
	
	private int myNumStates;
	private List<ArrayList< HashMap<Integer, Integer> >> myNextStateMap;
	private List<Point> myNeighborOffsets;
	
	public Rule(int numStates, List<ArrayList< HashMap<Integer, Integer> >> stateRules, List<Point> neighborRules) {
		myNumStates = numStates;
		myNextStateMap = stateRules;
		myNeighborOffsets = neighborRules;
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
			Map<Integer, Integer> transitionsForStateX = myNextStateMap.get(myState).get(stateX);
			
			if(transitionShouldOccur(transitionsForStateX, numNeighborsWithStateX)) {
				nextState = transitionsForStateX.get(numNeighborsWithStateX);
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
	
	private boolean transitionShouldOccur(Map<Integer, Integer> transitions, int key) {
		return transitions.containsKey(key);
	}
}
