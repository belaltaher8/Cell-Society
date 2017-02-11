package cs.model.sims;

import cs.configuration.PredatorPreyDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.FishCell;
import cs.model.cells.SharkCell;

public class PredatorPreySim extends Simulation {
	private int FISH_BREED_INTERVAL;
	private int SHARK_BREED_INTERVAL;
	private int SHARK_STARVE_INTERVAL;
	
	public PredatorPreySim(PredatorPreyDoc config) {
		super(config);
		FISH_BREED_INTERVAL = config.getFishBreedInterval();
		SHARK_BREED_INTERVAL = config.getSharkBreedInterval();
		SHARK_STARVE_INTERVAL = config.getSharkStarveInterval();
		this.reset();
	}
	
	@Override
	protected Cell placeCell(int initialState, Point point) {
		if(initialState == FishCell.FISH_STATE) {
			return new FishCell(initialState, point, this.getRule(), this, FISH_BREED_INTERVAL);
		} else if(initialState == SharkCell.SHARK_STATE) {
			return new SharkCell(initialState, point, this.getRule(), this, SHARK_BREED_INTERVAL, SHARK_STARVE_INTERVAL);
		} else {
			return new Cell(initialState, point, this.getRule(), this);
		}
	}
}
