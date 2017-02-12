package cs.model.sims;

import cs.configuration.configs.PredatorPreyDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.FishCell;
import cs.model.cells.SharkCell;

public class PredatorPreySim extends Simulation {
	public static final int NUM_STATES = 3;
	
	public PredatorPreySim(PredatorPreyDoc config) {
		super(config);
	}
	
	@Override
	public Cell placeCell(int initialState, Point point) {
		if(initialState == FishCell.FISH_STATE) {
			return new FishCell(initialState, point, this.getConfig(), this);
		} else if(initialState == SharkCell.SHARK_STATE) {
			return new SharkCell(initialState, point, this.getConfig(), this);
		} else {
			return new Cell(initialState, point, this.getConfig(), this);
		}
	}
	
	@Override
	public int getNumStates() {
		return PredatorPreySim.NUM_STATES;
	}
}
