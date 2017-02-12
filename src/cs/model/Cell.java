package cs.model;

import java.util.Collection;
import java.util.List;

public class Cell {
	public static final int DEFAULT_STATE = 0;
	
	private int currentState;
	private int nextState;
	private Point myCoords;
	private Rule myRule;
	private Simulation mySim;
	
	public Cell(int initialState, Point coordinates, Rule rule, Simulation simulation) {
		currentState = initialState;
		nextState = initialState;
		myCoords = coordinates;
		myRule = rule;
		mySim = simulation;
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
	
	protected Simulation getSimulation() {
		return mySim;
	}
}
