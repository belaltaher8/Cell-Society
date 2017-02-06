package cellsociety_team09.model;

import cellsociety_team09.configuration.XMLReader;

public class MovingGrid extends Grid {

	public MovingGrid(XMLReader reader){
		super(reader);
	}

	@Override
	protected void intializeGrid() {
		this.randomizeGrid();
	}
	
	@Override
	protected Cell placeCell(int initialState, Point point) {
		return new MovingCell(initialState, point, myRule, this);
	}
}
