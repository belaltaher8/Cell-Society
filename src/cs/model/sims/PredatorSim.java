package cs.model.sims;

import cs.configuration.XMLReader;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.FishCell;
import cs.model.cells.SharkCell;

public class PredatorSim extends Simulation {
	private double FISH_INITIAL_POPULATION;
	private double SHARK_INITIAL_POPULATION;
	private int FISH_BREED_INTERVAL;
	private int SHARK_BREED_INTERVAL;
	private int SHARK_STARVE_INTERVAL;
	
	public PredatorSim(XMLReader reader) {
		super(reader);
		FISH_BREED_INTERVAL = reader.getIntParameter("FISH_BREED_INTERVAL");
		SHARK_BREED_INTERVAL = reader.getIntParameter("SHARK_BREED_INTERVAL");
		SHARK_STARVE_INTERVAL = reader.getIntParameter("SHARK_STARVE_INTERVAL");
		FISH_INITIAL_POPULATION = reader.getDoubleParameter("FISH_INITIAL_POPULATION");
		SHARK_INITIAL_POPULATION = reader.getDoubleParameter("SHARK_INITIAL_POPULATION");
		this.reset();
	}

	@Override
	protected void intializeGrid() {
		for(int x = 0; x < this.getWidth(); x++) {
			for(int y = 0; y < this.getHeight(); y++) {
				Point point = new Point(x, y);
				int initialState = Cell.DEFAULT_STATE;
				double rand = this.getRand().nextDouble();
				if(rand <= FISH_INITIAL_POPULATION) {
					initialState = FishCell.FISH_STATE;
				} else if(rand <= (SHARK_INITIAL_POPULATION + FISH_INITIAL_POPULATION)) {
					initialState = SharkCell.SHARK_STATE;
				}
				Cell cell = placeCell(initialState, point);
				this.getGrid().put(point, cell);
			}
		}
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
