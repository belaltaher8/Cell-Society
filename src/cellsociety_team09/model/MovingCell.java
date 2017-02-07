package cellsociety_team09.model;

import java.util.Collection;

public class MovingCell extends Cell {
	
	public MovingCell(int initialState, Point coordinates, Rule rule, Grid grid) {
		super(initialState, coordinates, rule, grid);
	}

	@Override
	public void computeNextState(Collection<Integer> neighborStates) {
		int nextState = this.getRule().getNextState(this.getState(), neighborStates);
		
		if(nextState != this.getState()){
			this.getGrid().requestRandomSwap(this, Cell.EMPTY_STATE);
		}
		
		//Note: a MovingCell doesn't ever actually change state, it just uses the
		//next state computation to determine if it is happy or not in its location.
	}
}
