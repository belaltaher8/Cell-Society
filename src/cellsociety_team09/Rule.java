package cellsociety_team09;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	private int[][][] nextStateMap;
	private List<Point> neighborOffsets;
	
	public Rule(int[][][] stateRules, List<Point> neighborRules) {
		nextStateMap = stateRules;
		neighborOffsets = neighborRules;
	}
	
	public List<Point> getNeighbors(Point coords) {
		List<Point> neighbors = new ArrayList<Point>();
		for(int i = 0; i < neighborOffsets.size(); i++) {
			Point neighbor = new Point(coords.getX() + neighborOffsets.get(i).getX(),
									   coords.getY() + neighborOffsets.get(i).getY());
			//TODO: do bounds checking
			neighbors.add(neighbor);
		}
		return neighbors;
	}
	
	public int getNextState(int currentState, List<Integer> neighborStates) {
		return 0;
	}
}
