package cellsociety_team09.model;

import cellsociety_team09.configuration.XMLReader;

public class PredatorGrid extends Grid {

	public PredatorGrid(XMLReader reader) {
		super(reader);
	}

	@Override
	protected void intializeGrid() {
		this.randomizeGrid();
	}
	
	@Override
	protected Cell placeCell(int initialState, Point point) {
		if(initialState == FishCell.FISH_STATE) {
			return new FishCell(initialState, point, myRule, this, FishCell.BREED_INTERVAL);
		} else if(initialState == SharkCell.SHARK_STATE) {
			return new SharkCell(initialState, point, myRule, this, SharkCell.BREED_INTERVAL, SharkCell.STARVE_INTERVAL);
		} else {
			return new Cell(initialState, point, myRule, this);
		}
	}
}
