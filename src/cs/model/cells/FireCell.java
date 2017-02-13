package cs.model.cells;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import cs.configuration.configs.FireDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;

/**
 * @author jaydoherty
 * This class defines the behavior for all of the cells in the Fire Spreading Simulation.
 */
public class FireCell extends Cell {
	public static final int LIVE_STATE = 0;
	public static final int BURN_STATE = 1;
	public static final int DEAD_STATE = 2;
	
	private Random myRand;
	
	public FireCell(int initialState, Point coordinates, FireDoc config, Simulation simulation) {
		super(initialState, coordinates, config, simulation);
		myRand = new Random();
	}
	
	/**
	 * This method is overridden to define the fire simulation cell's behavior
	 */
	@Override
	public void computeNextState(Collection<Integer> neighborStates) {
		int nextState = this.getState();
		
		List<Integer> neighborCounts = getStateCounts(neighborStates);
		
		if(this.getState() == LIVE_STATE) {
			if(neighborCounts.get(BURN_STATE) > 0 && catchesFire()) {
				nextState = BURN_STATE;
			}
		} else if(this.getState() == BURN_STATE) {
			nextState = DEAD_STATE;
		}
		
		this.setNextState(nextState);
	}
	
	private boolean catchesFire() {
		return (myRand.nextDouble() <= ((FireDoc)this.getConfig()).getProbCatch());
	}
}
