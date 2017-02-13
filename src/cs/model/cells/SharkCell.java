package cs.model.cells;

import java.util.Collection;

import cs.configuration.ConfigDoc;
import cs.configuration.configs.PredatorPreyDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;

/**
 * @author jaydoherty
 * This class represents the sharks in Predator Prey Simulation.
 */
public class SharkCell extends FishCell {
	public static final int SHARK_STATE = 2;
	
	private int starveTimer;
	private boolean alive = true;

	public SharkCell(int initialState, Point coordinates, ConfigDoc config, Simulation sim) {
		super(initialState, coordinates, config, sim);
		starveTimer = 0;
	}
	
	/**
	 * This method is overridden to define the cell's behavior
	 */
	@Override
	public void computeNextState(Collection<Integer> neighborStates) {	
		this.reproduce();
		this.starve();
		
		if(alive) {
			Cell fish = findNeighborOfGivenState(FishCell.FISH_STATE);
			if(fish != null) {
				this.eat(fish);
			} else {
				this.move();
			}
		}
		
		incrementTimer();
	}
	
	@Override
	protected Cell getOffspring(Point coords) {
		return new SharkCell(this.getState(), coords, this.getConfig(), this.getSimulation());
	}
	
	@Override
	protected void incrementTimer() {
		super.incrementTimer();
		starveTimer++;
	}
	
	private void eat(Cell sharkFood) {
		Cell replacement = new Cell(Cell.DEFAULT_STATE, sharkFood.getCoords(), this.getConfig(), this.getSimulation());
		this.getSimulation().replaceCell(sharkFood, replacement);
		starveTimer = 0;
	}
	
	private void starve() {
		if(starveTimer >= ((PredatorPreyDoc)this.getConfig()).getSharkStarveInterval()) {
			Cell empty = new Cell(Cell.DEFAULT_STATE, this.getCoords(), this.getConfig(), this.getSimulation());
			this.getSimulation().replaceCell(this, empty);
			alive = false;
		}
	}
}
