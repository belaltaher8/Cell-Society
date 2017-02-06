package cellsociety_team09.model;

import java.util.Collection;
import java.util.List;

public class Cell {
	public static final int EMPTY_STATE = 0;
	
	private int currentState;
	private int nextState;
	private Point myCoords;
	private Rule myRule;
	private Grid myGrid;
	
	public Cell(int initialState, Point coordinates, Rule rule, Grid grid) {
		currentState = initialState;
		nextState = initialState;
		myCoords = coordinates;
		myRule = rule;
		myGrid = grid;
	}
	
	public void advanceState() {
		currentState = nextState;
	}
	
	public void computeNextState(Collection<Integer> neighborStates) {
		nextState = myRule.getNextState(currentState, neighborStates);
	}
	
	public Collection<Point> getNeighborCoords() {
		return myRule.getNeighborCoords(myCoords);
	}
	
	public int getState() {
		return currentState;
	}
	
	public Point getCoords() {
		return myCoords;
	}
	
	public void setCoords(Point newCoords) {
		myCoords = newCoords;
	}
	
	protected Rule getRule() {
		return myRule;
	}
	
	protected Grid getGrid() {
		return myGrid;
	}
}
