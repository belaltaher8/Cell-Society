package cs.model.cells;

import java.util.Collection;

import cs.model.Cell;
import cs.model.Point;
import cs.model.Rule;
import cs.model.Simulation;

public class MovingCell extends Cell {
	
	public MovingCell(int initialState, Point coordinates, Rule rule, Simulation grid) {
		super(initialState, coordinates, rule, grid);
	}

	@Override
	public void computeNextState(Collection<Integer> neighborStates) {
		int nextState = this.getRule().getNextState(this.getState(), neighborStates);
		
		if(nextState != this.getState()){
			this.getGrid().requestRandomSwap(this, Cell.DEFAULT_STATE);
		}
		
		//Note: a MovingCell doesn't ever actually change state, it just uses the
		//next state computation to determine if it is happy or not in its location.
	}
}
