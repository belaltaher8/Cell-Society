package cellsociety_team09.model;

import java.util.Collection;

public class SharkCell extends FishCell {
	public static final int SHARK_STATE = 2;
	
	private int starveInterval;
	private int starveTimer;
	private boolean alive = true;

	public SharkCell(int initialState, Point coordinates, Rule rule, Grid grid, int breedtime, int starvetime) {
		super(initialState, coordinates, rule, grid, breedtime);
		starveInterval = starvetime;
		starveTimer = 0;
	}
	
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
		return new SharkCell(this.getState(), coords, this.getRule(), this.getGrid(), this.getBreedInterval(), starveInterval);
	}
	
	@Override
	protected void incrementTimer() {
		super.incrementTimer();
		starveTimer++;
	}
	
	private void eat(Cell sharkFood) {
		Cell replacement = new Cell(Cell.EMPTY_STATE, sharkFood.getCoords(), this.getRule(), this.getGrid());
		this.getGrid().replaceCell(sharkFood, replacement);
		starveTimer = 0;
	}
	
	private void starve() {
		if(starveTimer >= starveInterval) {
			Cell empty = new Cell(Cell.EMPTY_STATE, this.getCoords(), this.getRule(), this.getGrid());
			this.getGrid().replaceCell(this, empty);
			alive = false;
		}
	}
}
