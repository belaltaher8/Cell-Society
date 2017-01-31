package cellsociety_team09.model;

import java.util.List;

public class Cell {
	private int currentState;
	private int nextState;
	private Point myCoords;
	private Rule myRule;
	
	public Cell(int initialState, Point coordinates, Rule rule) {
		currentState = initialState;
		nextState = initialState;
		myCoords = coordinates;
		myRule = rule;
	}
	
	public void advanceState() {
		currentState = nextState;
	}
	
	public void computeNextState(List<Integer> neighborStates) {
		nextState = myRule.getNextState(currentState, neighborStates);
	}
	
	public List<Point> getNeighborCoords() {
		return myRule.getNeighbors(myCoords);
	}
	
	public int getState() {
		return currentState;
	}
	
	public Point getCoords() {
		return myCoords;
	}
}
