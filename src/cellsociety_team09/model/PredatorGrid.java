package cellsociety_team09.model;

import cellsociety_team09.configuration.XMLReader;

public class PredatorGrid extends Grid {
	public static final double FISH_POPULATION = 0.75;
	public static final double SHARK_POPULATION = 0.1;
	
	public PredatorGrid(XMLReader reader) {
		super(reader);
	}

	@Override
	protected void intializeGrid() {
		for(int x = 0; x < this.getWidth(); x++) {
			for(int y = 0; y < this.getHeight(); y++) {
				Point point = new Point(x, y);
				int initialState = Cell.EMPTY_STATE;
				double rand = this.getRand().nextDouble();
				if(rand <= FISH_POPULATION) {
					initialState = FishCell.FISH_STATE;
				} else if(rand <= (SHARK_POPULATION + FISH_POPULATION)) {
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
			return new FishCell(initialState, point, this.getRule(), this, FishCell.BREED_INTERVAL);
		} else if(initialState == SharkCell.SHARK_STATE) {
			return new SharkCell(initialState, point, this.getRule(), this, SharkCell.BREED_INTERVAL, SharkCell.STARVE_INTERVAL);
		} else {
			return new Cell(initialState, point, this.getRule(), this);
		}
	}
}
