package cs.model.cells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cs.configuration.ConfigDoc;
import cs.configuration.configs.PredatorPreyDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;

public class FishCell extends Cell {
	public static final int FISH_STATE = 1;
	
	private int breedTimer;
	
	public FishCell(int initialState, Point coordinates, ConfigDoc config, Simulation sim) {
		super(initialState, coordinates, config, sim);
		breedTimer = 0;
	}

	@Override
	public void computeNextState(Collection<Integer> neighborStates) {		
		this.reproduce();
		this.move();
		
		incrementTimer();
	}
	
	protected void incrementTimer() {
		breedTimer++;
	}
	
	protected Cell getOffspring(Point coords) {
		return new FishCell(this.getState(), coords, this.getConfig(), this.getSimulation());
	}
	
	protected void move() {
		Cell empty = findNeighborOfGivenState(Cell.DEFAULT_STATE);
		if(empty != null) {
			this.getSimulation().requestSpecificSwap(this, empty);
		}
	}
	
	protected void reproduce() {
		if(breedTimer >= ((PredatorPreyDoc)this.getConfig()).getFishBreedInterval()) {
			Cell empty = findNeighborOfGivenState(Cell.DEFAULT_STATE);
			if(empty != null) {
				Cell replacement = getOffspring(empty.getCoords());
				this.getSimulation().replaceCell(empty, replacement);
				breedTimer = 0;
			}
		}
	}
	
	protected Cell findNeighborOfGivenState(int state) {
		List<Point> neighborCoords = new ArrayList<Point>(this.getNeighborCoords());
		Collections.shuffle(neighborCoords);
		for(Point p : neighborCoords) {
			Cell c = this.getSimulation().getCellAtPoint(p);
			if(c.getState() == state) {
				return c;
			}
		}
		return null;
	}
}
