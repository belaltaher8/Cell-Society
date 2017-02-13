package cs.model.cells;

import java.util.Collection;
import java.util.Random;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;

public class SugarCell extends Cell {
	public static final int SUGAR_CELL_STATE = 1;
	public static final int SUGAR_GROWTH_AMOUNT = 1;
	
	private Random myRand;
	private int mySugar;
	private int maxSugarCapacity;

	public SugarCell(int initialState, Point coordinates, ConfigDoc config, Simulation simulation) {
		super(initialState, coordinates, config, simulation);
		myRand = new Random();
		maxSugarCapacity = myRand.nextInt(5);
		mySugar = maxSugarCapacity;
	}

	@Override
	public void computeNextState(Collection<Integer> neighborStates) {		
		if(mySugar < maxSugarCapacity &&
			this.getSimulation().getCellAtPoint(this.getCoords()) == null) {
			mySugar += SUGAR_GROWTH_AMOUNT;
		}
	}
	
	@Override
	public int getState() {
		return super.getState() + mySugar;
	}
	
	public int getSugar() {
		return mySugar;
	}
	
	public int takeSugar() {
		int sugar = mySugar;
		mySugar = 0;
		return sugar;
	}
}
