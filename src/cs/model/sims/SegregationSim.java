package cs.model.sims;

import cs.configuration.configs.SegregationDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.SegregationCell;

public class SegregationSim extends Simulation {

	public SegregationSim(SegregationDoc config){
		super(config);
	}
	
	@Override
	public Cell placeCell(int initialState, Point point) {
		return new SegregationCell(initialState, point, ((SegregationDoc)this.getConfig()), this);
	}
}
