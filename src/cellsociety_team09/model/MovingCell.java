package cellsociety_team09.model;

import java.util.List;

public class MovingCell extends Cell {

	private Grid myGrid;
	
	public MovingCell(int initialState, Point coordinates, Rule rule, Grid grid) {
		super(initialState, coordinates, rule);
		myGrid = grid; //MovingCells must be self-aware of the Grid they are in
	}

	@Override
	public void computeNextState(List<Integer> neighborStates) {
		int nextState = this.getRule().getNextState(this.getState(), neighborStates);
		
		if(nextState != this.getState()){
			myGrid.requestSwap(this, 0);
		}
		
		//Note: a MovingCell doesn't ever actually change state, it just uses the
		//next state computation to determine if it is happy or not in its location.
	}
}
