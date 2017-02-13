package cs.model.sims;

import cs.configuration.configs.SegregationDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.SegregationCell;

/**
 * @author jaydoherty
 * This class defines the Segregation simulation
 */
public class SegregationSim extends Simulation {
	public static final int NUM_STATES = 3;
	
	public SegregationSim(SegregationDoc config){
		super(config);
	}
	
	/**
	 * This method defines SegregationCell as the cell to populate the grid
	 */
	@Override
	public Cell placeCell(int initialState, Point point) {
		return new SegregationCell(initialState, point, ((SegregationDoc)this.getConfig()), this);
	}
	
	/**
	 * @return number of states
	 */
	@Override
	public int getNumStates() {
		return SegregationSim.NUM_STATES;
	}
}
