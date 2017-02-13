package cs.model.sims;

import java.util.HashMap;
import java.util.Map;

import cs.configuration.ConfigDoc;
import cs.model.Cell;
import cs.model.Point;
import cs.model.Simulation;
import cs.model.cells.AgentCell;
import cs.model.cells.SugarCell;

/**
 * @author jaydoherty
 * This class defines the Segregation simulation
 */
public class SugarScapeSim extends Simulation {
	private Map<Point, SugarCell> myBackgroundGrid;
	
	public SugarScapeSim(ConfigDoc config) {
		super(config);
	}

	/**
	 * This method resets the normal grid and the background grid
	 */
	@Override
	public void reset() {
		super.reset();
		this.buildGrid();
	}
	
	/**
	 * Overrides this method to build both grids
	 */
	@Override
	public void buildGrid() {
		super.buildGrid();
		if(myBackgroundGrid == null) {
			myBackgroundGrid = new HashMap<Point, SugarCell>();
		}
		for(int x = 0; x < this.getConfig().getGridWidth(); x++) {
			for(int y = 0; y < this.getConfig().getGridHeight(); y++) {
				Point point = new Point(x, y);
				int initialState = SugarCell.SUGAR_CELL_STATE;
				SugarCell cell = ((SugarCell)placeCell(initialState, point));
				myBackgroundGrid.put(point, cell);
			}
		}
	}
	
	/**
	 * @return cell at a point in the background grid
	 */
	public SugarCell getBackgroundCellAtPoint(Point myPoint){
		if(!myBackgroundGrid.containsKey(myPoint)) {
			return null;
		}
		return myBackgroundGrid.get(myPoint);
	}
	
	/**
	 * This method defines AgentCell and SugarCell as the cells to use in this simulation
	 */
	@Override
	public Cell placeCell(int initialState, Point point) {
		if(initialState == AgentCell.AGENT_CELL_STATE) {
			return new AgentCell(initialState, point, this.getConfig(), this);
		} else {
			return new SugarCell(SugarCell.SUGAR_CELL_STATE, point, this.getConfig(), this);
		} 
	}

	/**
	 * @return number of states (number of colors to show in the grid)
	 */
	@Override
	public int getNumStates() {
		return 7;
	}

}
