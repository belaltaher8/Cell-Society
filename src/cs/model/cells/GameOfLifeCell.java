package cs.model.cells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;

public class GameOfLifeCell extends Cell {
	public static final int DEAD_STATE = 0;
	public static final int ALIVE_STATE = 1;
	
	public static final int LIVE_CONDITION = 3;
	public static final int LONELY_CONDITION = 1;
	public static final int CROWDED_CONDITION = 4;
	
	public GameOfLifeCell(int initialState, Point coordinates, ConfigDoc config, Simulation sim) {
		super(initialState, coordinates, config, sim);
	}

	@Override
	public void computeNextState(Collection<Integer> neighborStates) {
		int nextState = this.getState();
		
		List<Integer> neighborCounts = getStateCounts(neighborStates);
		
		if(this.getState() == DEAD_STATE) {
			if(neighborCounts.get(ALIVE_STATE) == LIVE_CONDITION) {
				nextState = ALIVE_STATE;
			}
		} else {
			if(neighborCounts.get(ALIVE_STATE) <= LONELY_CONDITION || 
			   neighborCounts.get(ALIVE_STATE) >= CROWDED_CONDITION) {
				nextState = DEAD_STATE;
			}
		}
		
		this.setNextState(nextState);
	}
}
