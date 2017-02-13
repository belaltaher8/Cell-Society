package cs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import cs.configuration.ConfigDoc;

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
	
	public ConfigDoc getConfig() {
		return myConfig;
	}
	
	public void reset() {
		myGrid = new HashMap<Point, Cell>();
		buildGrid();
	}
	
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
	
	public String getContentsAsXML() {
		String contents = "";
		for(Cell c : myGrid.values()) {
			String tag = String.format("INITIAL_STATE_%s_%s", c.getCoords().getX(), c.getCoords().getY());
			String state = Integer.toString(c.getState());
			contents += myConfig.formatWithXMLTags(tag, state);
		}
		return contents;
	}
	
	abstract public Cell placeCell(int initialState, Point point);

	abstract public int getNumStates();
	
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