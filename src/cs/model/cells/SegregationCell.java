package cs.model.cells;

import java.util.Collection;
import java.util.List;

import cs.configuration.configs.SegregationDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;

/**
 * @author jaydoherty
 * This class defines the behavior of the cells in a Segregation simulation.
 */
public class SegregationCell extends Cell {
	public static final int EMPTY_STATE = 0;
	public static final int STATE_1 = 1;
	public static final int STATE_2 = 2;
	
	public SegregationCell(int initialState, Point coordinates, SegregationDoc config, Simulation sim) {
		super(initialState, coordinates, config, sim);
	}

	/**
	 * This method is overridden to define the behavior.
	 */
	@Override
	public void computeNextState(Collection<Integer> neighborStates) {		
		List<Integer> neighborCounts = getStateCounts(neighborStates);
		
		if(isUnhappy(neighborStates.size(), neighborCounts)) {
			this.getSimulation().requestRandomSwap(this, EMPTY_STATE);
		}
	}
	
	private boolean isUnhappy(int numNeighbors, List<Integer> neighborCounts) {
		double fractionDifferent;
		if(this.getState() == STATE_1) {
			fractionDifferent = ((double)neighborCounts.get(STATE_2))/numNeighbors;
		} else if(this.getState() == STATE_2) {
			fractionDifferent = ((double)neighborCounts.get(STATE_1))/numNeighbors;
		} else {
			fractionDifferent = 0.0;
		}
		return fractionDifferent > ((SegregationDoc)this.getConfig()).getPercentTolerance();
	}
}
