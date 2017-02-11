package cs.model.sims;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLReader;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.MovingCell;

public class MovingSim extends Simulation {

	public MovingSim(ConfigDoc config){
		super(config);
	}
	
	@Override
	protected Cell placeCell(int initialState, Point point) {
		return new MovingCell(initialState, point, this.getRule(), this);
	}
}
