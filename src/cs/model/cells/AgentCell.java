package cs.model.cells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.sims.SugarScapeSim;

/**
 * @author jaydoherty
 * This class represents the Agent in Sugar Scape Simulation.
 */
public class AgentCell extends Cell {
	public static final int AGENT_CELL_STATE = 0;
	//public static final int SUGAR_MIN = 5;

	private Random myRand;
	private int mySugar;
	private int myVision;
	private int myMetabolism;
	
	public AgentCell(int initialState, Point coordinates, ConfigDoc config, Simulation simulation) {
		super(initialState, coordinates, config, simulation);
		myRand = new Random();
		mySugar = 5 + myRand.nextInt(21);
		myVision = 1 + myRand.nextInt(6);
		myMetabolism = 1 + myRand.nextInt(4);
	}
	
	
	/**
	 * This method is overridden to define the agent's behavior
	 */
	@Override
	public void computeNextState(Collection<Integer> neighborStates) {		
		this.move();
		this.take();
		this.consumeSugar();
	}
	
	private void consumeSugar() {
		mySugar -= myMetabolism;
		if(mySugar <= 0) {
			this.getSimulation().replaceCell(this, null);
		}
	}
	
	private void take() {
		mySugar += this.getMyGround().takeSugar();
	}
	
	private void move() {
		List<SugarCell> placesToGo = getSugarInVision();
		placesToGo = removeOccupied(placesToGo);
		
		if(placesToGo.size() == 0) {
			return;
		}
		
		SugarCell maxSugarCell = placesToGo.get(placesToGo.size() - 1);
		for(int i = placesToGo.size() - 2; i > 0; i--) {
			SugarCell current = placesToGo.get(i);
			if(current.getSugar() >= maxSugarCell.getSugar()) {
				maxSugarCell = current;
			}
		}
		
		this.getSimulation().requestSpecificSwap(maxSugarCell, this);
	}
	
	private SugarCell getMyGround() {
		SugarScapeSim mySim = ((SugarScapeSim)this.getSimulation());
		return mySim.getBackgroundCellAtPoint(this.getCoords());
	}
	
	private List<SugarCell> removeOccupied(List<SugarCell> placesToGo) {
		SugarScapeSim mySim = ((SugarScapeSim)this.getSimulation());
		
		List<SugarCell> occupied = new ArrayList<SugarCell>();
		for(SugarCell c : placesToGo) {
			if(mySim.getCellAtPoint(c.getCoords()) != null) {
				occupied.add(c);
			}
		}
		
		placesToGo.removeAll(occupied);
		
		return placesToGo;
	}
	
	private List<SugarCell> getSugarInVision() {
		SugarScapeSim mySim = ((SugarScapeSim)this.getSimulation());
		List<SugarCell> possibilities = new ArrayList<SugarCell>();
		
		for(int i = 1; i <= myVision; i++) {
			Point p1 = new Point(this.getCoords().getX() + i, this.getCoords().getY());
			Point p2 = new Point(this.getCoords().getX() - i, this.getCoords().getY());
			Point p3 = new Point(this.getCoords().getX(), this.getCoords().getY() + i);
			Point p4 = new Point(this.getCoords().getX(), this.getCoords().getY() - i);

			SugarCell c1 = mySim.getBackgroundCellAtPoint(p1);
			SugarCell c2 = mySim.getBackgroundCellAtPoint(p2);
			SugarCell c3 = mySim.getBackgroundCellAtPoint(p3);
			SugarCell c4 = mySim.getBackgroundCellAtPoint(p4);
			
			if(c1 != null) {
				possibilities.add(c1);
			}
			if(c2 != null) {
				possibilities.add(c2);
			}
			if(c3 != null) {
				possibilities.add(c3);
			}
			if(c4 != null) {
				possibilities.add(c4);
			}
		}
		return possibilities;
	}
}
