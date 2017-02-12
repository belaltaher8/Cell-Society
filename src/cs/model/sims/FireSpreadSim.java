package cs.model.sims;

import cs.configuration.configs.FireDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.FireCell;

public class FireSpreadSim extends Simulation {
	public FireSpreadSim(FireDoc config) {
		super(config);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Cell placeCell(int initialState, Point point) {
		return new FireCell(initialState, point, ((FireDoc)this.getConfig()), this);
	}
}
