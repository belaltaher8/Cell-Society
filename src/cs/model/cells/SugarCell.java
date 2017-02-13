package cs.model.cells;

import java.util.Collection;
import java.util.Random;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;

/**
 * @author jaydoherty
 * This class represents the Sugar in Sugar Scape Simulation.
 */
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

	/**
	 * This method defines the cell's behavior at each time step.
	 */
	@Override
	public void computeNextState(Collection<Integer> neighborStates) {		
		if(mySugar < maxSugarCapacity &&
			this.getSimulation().getCellAtPoint(this.getCoords()) == null) {
			mySugar += SUGAR_GROWTH_AMOUNT;
		}
	}
	
	/**
	 * This method returns a modified state to display the sugar on the GridDisplay
	 */
	@Override
	public int getState() {
		return super.getState() + mySugar;
	}
	
	/**
	 * @return the amount of sugar in this cell
	 */
	public int getSugar() {
		return mySugar;
	}
	
	/**
	 * This method is called by agents to eat the sugar.
	 * @return the amount of sugar taken
	 */
	public int takeSugar() {
		int sugar = mySugar;
		mySugar = 0;
		return sugar;
	}
}
