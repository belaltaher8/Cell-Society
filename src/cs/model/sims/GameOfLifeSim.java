package cs.model.sims;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.GameOfLifeCell;

public class GameOfLifeSim extends Simulation {

	public GameOfLifeSim(ConfigDoc config) {
		super(config);
	}

	@Override
	protected Cell placeCell(int initialState, Point point) {
		return new GameOfLifeCell(initialState, point, this.getConfig(), this);
	}
}
