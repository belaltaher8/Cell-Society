package cellsociety_team09.model;

import java.util.Random;

import cellsociety_team09.configuration.XMLReader;

public class MovingGrid extends Grid {

	public MovingGrid(XMLReader reader){
		super(reader);
	}

	@Override
	public Cell placeCell(int initialState, Point point) {
		return new MovingCell(initialState, point, myRule, this);
	}
}
