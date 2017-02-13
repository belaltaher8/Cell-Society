package cs.model.sims;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.GameOfLifeCell;

/**
 * @author jaydoherty
 * This class defines the Game of Life simulation
 */
public class GameOfLifeSim extends Simulation {
	public static final int NUM_STATES = 2;

	public GameOfLifeSim(ConfigDoc config) {
		super(config);
	}

	/**
	 * This method defines GameOfLifeCell as the cell to populate the grid
	 */
	@Override
	public Cell placeCell(int initialState, Point point) {
		return new GameOfLifeCell(initialState, point, this.getConfig(), this);
	}
	
	/**
	 * @return number of states
	 */
	@Override
	public int getNumStates() {
		return GameOfLifeSim.NUM_STATES;
	}
}
