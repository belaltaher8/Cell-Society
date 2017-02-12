package cs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import cs.configuration.ConfigDoc;

public class Simulation {	
	private Map<Point, Cell> myGrid;
	private ConfigDoc myConfig;
	
	private Random myRand;
	private Collection<Cell[]> swapPairs;

	public Simulation(ConfigDoc config){
		myConfig = config;
		myRand = new Random();
		swapPairs = new ArrayList<Cell[]>();
		reset();
	}
	
	public ConfigDoc getConfig() {
		return myConfig;
	}
	
	public void reset() {
		myGrid = new HashMap<Point, Cell>();
		intializeGrid();
	}
	
	public void randomizeGrid() {
		for(int x = 0; x < myConfig.getGridWidth(); x++) {
			for(int y = 0; y < myConfig.getGridHeight(); y++) {
				Point point = new Point(x, y);
				int randomState = myRand.nextInt(myConfig.getNumStates());
				Cell cell = placeCell(randomState, point);
				myGrid.put(point, cell);
			}
		}
	}

	public Cell getCellAtPoint(Point myPoint){
		if(!myGrid.containsKey(myPoint)) {
			return null;
		}
		return myGrid.get(myPoint);
	}
	
	public void replaceCell(Cell old, Cell replacement) {
		Point coords = old.getCoords();
		myGrid.remove(coords);
		myGrid.put(coords, replacement);
	}
	
	public void stepGrid(){
		computeNextGrid();
		advanceGrid();
		applyAllSwaps();
	}
	
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
	
	public void requestSpecificSwap(Cell a, Cell b) {
		swapPairs.add(new Cell[] {a,b});
	}
	
	public String getContentsAsXML() {
		String contents = "";
		for(Cell c : myGrid.values()) {
			String tag = String.format("INITIAL_STATE_%s_%s", c.getCoords().getX(), c.getCoords().getY());
			String state = Integer.toString(c.getState());
			contents += myConfig.formatWithXMLTags(tag, state);
		}
		return contents;
	}
	
	private void swapCells(Cell a, Cell b) {
		Point pointA = a.getCoords();
		Point pointB = b.getCoords();
		
		myGrid.remove(pointA);
		myGrid.remove(pointB);
		
		a.setCoords(pointB);
		b.setCoords(pointA);
		
		myGrid.put(pointB, a);
		myGrid.put(pointA, b);
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
				
				Collection<Point> myCellNeighborsCoords = myCell.getNeighborCoords();
				List<Integer> myNeighborStates = new ArrayList<Integer>();
				
				for(Point currentPoint : myCellNeighborsCoords) {
					Cell cell = getCellAtPoint(currentPoint);
					myNeighborStates.add(cell.getState());
				}
				
				myCell.computeNextState(myNeighborStates);
			}
		}
	}
	
	private void advanceGrid(){
		for(Cell c : myGrid.values()) {
			c.advanceState();
		}
	}
	
	private void intializeGrid() {
		for(int x = 0; x < myConfig.getGridWidth(); x++) {
			for(int y = 0; y < myConfig.getGridHeight(); y++) {
				Point point = new Point(x, y);
				int initialState = determineInitialStateAt(point);
				Cell cell = placeCell(initialState, point);
				myGrid.put(point, cell);
			}
		}
	}
	
	private int determineInitialStateAt(Point point) {
		if(myConfig.getInitializationStyle().equals(ConfigDoc.INIT_STYLE_SPECIFIC)) {
			return myConfig.getInitialStateAt(point);
		} else if(myConfig.getInitializationStyle().equals(ConfigDoc.INIT_STYLE_PROB)) {
			int result = Cell.DEFAULT_STATE;
			double rand = myRand.nextDouble();
			double threshold = 0.0;
			for(int state = 0; state < myConfig.getNumStates(); state++) {
				threshold += myConfig.getInitialStateDensity(state);
				if(rand <= threshold) {
					result = state;
					break;
				} 
			}
			return result;
		} else {
			return myRand.nextInt(myConfig.getNumStates());
		}
	}
	
	protected Cell placeCell(int initialState, Point point) {
		return new Cell(initialState, point, myConfig.getRule(), this);
	}
}
