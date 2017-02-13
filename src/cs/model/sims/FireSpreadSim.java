package cs.model.sims;

import cs.configuration.configs.FireDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.FireCell;

/**
 * @author jaydoherty
 * This class defines the Fire Spreading simulation.
 */
public class FireSpreadSim extends Simulation {
	public static final int NUM_STATES = 3;
	
	public FireSpreadSim(FireDoc config) {
		super(config);
	}

	/**
	 * This method defines FireCell as the cell to populate the grid
	 */
	@Override
	public Cell placeCell(int initialState, Point point) {
		return new FireCell(initialState, point, ((FireDoc)this.getConfig()), this);
	}
	
	/**
	 * @return number of states
	 */
	@Override
	public int getNumStates() {
		return FireSpreadSim.NUM_STATES;
	}
}
