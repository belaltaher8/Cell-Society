package cs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import cs.configuration.ConfigDoc;

/**
 * @author jaydoherty
 * This class contains the model for the cell society grid. It contains methods to initialize
 * and track the state of the grid as it advances. It contains methods to present the state of
 * the grid to the front-end.
 * Its subclasses must override placeCell, which tells the simulation what kind of cells it should
 * use to populate the grid.
 */
public abstract class Simulation {	
	private Map<Point, Cell> myGrid;
	private ConfigDoc myConfig;
	private Random myRand;
	private Collection<Cell[]> swapPairs;

	public Simulation(ConfigDoc config){
		myConfig = config;
		myRand = new Random();
		myGrid = new HashMap<Point, Cell>();
		swapPairs = new ArrayList<Cell[]>();
		reset();
	}
	
	
	/**
	 * @return the ConfigDoc for this simulation
	 */
	public ConfigDoc getConfig() {
		return myConfig;
	}
	
	/**
	 * Resets the grid.
	 */
	public void reset() {
		myGrid = new HashMap<Point, Cell>();
		buildGrid();
	}
	
	/**
	 * Builds the grid based on rules from the xml file. If the grid is already built,
	 * then it just replaces them. It also leverages placeCell to pick the right subclasses
	 * of Cell to fill the grid with.
	 */
	public void buildGrid() {
		for(int x = 0; x < myConfig.getGridWidth(); x++) {
			for(int y = 0; y < myConfig.getGridHeight(); y++) {
				Point point = new Point(x, y);
				int initialState = determineInitialStateAt(point);
				Cell cell = placeCell(initialState, point);
				myGrid.put(point, cell);
			}
		}
	}
	
	/**
	 * Fill the grid with random cells.
	 */
	public void randomizeGrid() {
		for(int x = 0; x < myConfig.getGridWidth(); x++) {
			for(int y = 0; y < myConfig.getGridHeight(); y++) {
				Point point = new Point(x, y);
				int randomState = myRand.nextInt(this.getNumStates());
				Cell cell = placeCell(randomState, point);
				myGrid.put(point, cell);
			}
		}
	}

	/**
	 * Returns the Cell in the grid for use by the front-end classes.
	 * @param myPoint : the grid location to check
	 * @return the cell in the grid at that point
	 */
	public Cell getCellAtPoint(Point myPoint){
		if(!myGrid.containsKey(myPoint)) {
			return null;
		}
		return myGrid.get(myPoint);
	}
	
	/**
	 * Replaces an old cell with a new one in the grid
	 * @param old : cell to replace
	 * @param replacement : replacement cell
	 */
	public void replaceCell(Cell old, Cell replacement) {
		Point coords = old.getCoords();
		myGrid.remove(coords);
		myGrid.put(coords, replacement);
	}
	
	/**
	 * Advances the grid one cycle by first updating the grid, then advancing all the states and
	 * applying all of the movement requests.
	 */
	public void stepGrid(){
		computeNextGrid();
		advanceGrid();
		applyAllSwaps();
	}
	
	/**
	 * Allows cells to request a swap with a random cell of a particular state.
	 * @param swapper : cell that wants to move
	 * @param desiredSwappee : state of the cell it wants to swap with
	 */
	public void requestRandomSwap(Cell swapper, int desiredSwappee) {
		ArrayList<Cell> swapCandidates = new ArrayList<Cell>(myGrid.values());
		Collections.shuffle(swapCandidates);
		Cell swappee = null;
							
		for(Cell c : swapCandidates) {
			if(c.getState() == desiredSwappee) {
				swappee = c;
				break;
			}
		}
		
		if(swappee != null) {
			this.requestSpecificSwap(swapper, swappee);
		} 
	}
	
	/**
	 * Allows cells to request a swap with another cell. The swap will be applied at the
	 * next update.
	 * @param a : 1st cell to swap
	 * @param b : 2nd cell to swap
	 */
	public void requestSpecificSwap(Cell a, Cell b) {
		swapPairs.add(new Cell[] {a,b});
	}
	
	private void swapCells(Cell a, Cell b) {
		Point pointA = a.getCoords();
		Point pointB = b.getCoords();
		
		a = myGrid.remove(pointA);
		b = myGrid.remove(pointB);
		
		if(a != null) {
			a.setCoords(pointB);
			myGrid.put(pointB, a);
		}
		
		if(b != null) {
			b.setCoords(pointA);
			myGrid.put(pointA, b);
		}
	}
	
	private void applyAllSwaps() {
		for(Cell[] pair : swapPairs) {
			swapCells(pair[0], pair[1]);
		}
		swapPairs.clear();
	}
	
	private void computeNextGrid() {
		for(int x = 0; x < myConfig.getGridWidth(); x++){
			for(int y = 0; y < myConfig.getGridHeight(); y++){
				Cell myCell = myGrid.get(new Point(x, y));
				if(myCell != null) {
					Collection<Point> myCellNeighborsCoords = myCell.getNeighborCoords();
					List<Integer> myNeighborStates = new ArrayList<Integer>();
					
					for(Point currentPoint : myCellNeighborsCoords) {
						Cell cell = getCellAtPoint(currentPoint);
						if(cell != null) {
							myNeighborStates.add(cell.getState());
						}
					}
					
					myCell.computeNextState(myNeighborStates);
				}
			}
		}
	}
	
	private void advanceGrid(){
		for(Cell c : myGrid.values()) {
			if(c != null) {
				c.advanceState();
			}
		}
	}
	
	private int determineInitialStateAt(Point point) {
		if(myGrid.containsKey(point) && myGrid.get(point) != null){
			return myGrid.get(point).getState();
		}
		
		if(myConfig.getInitializationStyle().equals(ConfigDoc.INIT_STYLE_SPECIFIC)) {
			return myConfig.getInitialStateAt(point, this.getNumStates());
		} else if(myConfig.getInitializationStyle().equals(ConfigDoc.INIT_STYLE_PROB)) {
			int result = Cell.DEFAULT_STATE;
			double rand = myRand.nextDouble();
			double threshold = 0.0;
			for(int state = 0; state < this.getNumStates(); state++) {
				threshold += myConfig.getInitialStateDensity(state);
				if(rand <= threshold) {
					result = state;
					break;
				} 
			}
			return result;
		} else {
			return myRand.nextInt(this.getNumStates());
		}
	}
	
	
	/**
	 * @return a single long string specifying the current state of every grid cell in xml format.
	 * Used for writing the current state to a file. Provides a full "snapshot" of the grid.
	 */
	public String getContentsAsXML() {
		String contents = "";
		for(Cell c : myGrid.values()) {
			String tag = String.format("INITIAL_STATE_%s_%s", c.getCoords().getX(), c.getCoords().getY());
			String state = Integer.toString(c.getState());
			contents += myConfig.formatWithXMLTags(tag, state);
		}
		return contents;
	}
	
	
	/**
	 * Simulations override this method to specify how they want to populate their simulations.
	 * @param initialState : state of the cell to place
	 * @param point : coords of the cell to place
	 * @return a Cell or subclass-Cell to put into the grid
	 */
	abstract public Cell placeCell(int initialState, Point point);

	/**
	 * This is simply a convenience method that simulations have to specify so the rest of the
	 * back-end knows how many states there are.
	 * @return the total number of cell states in this simulation
	 */
	abstract public int getNumStates();
	
	
	/**
	 * This is used for graphing population over time. This method returns the total number of cells
	 * at a particular state.
	 * @param i : state to count
	 * @return the total number of cells in the grid at a given state
	 */
	public int calculateNumInState(int i) {
		int count = 0; 
		for (Point p: myGrid.keySet()){
			if (myGrid.get(p).getState() == i){
				count++;
			}
		}
		return count; 
	}
}