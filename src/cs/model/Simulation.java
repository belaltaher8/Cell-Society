package cs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cs.configuration.XMLReader;

public class Simulation {	

	private Map<Point, Cell> myGrid;
	private XMLReader myReader;
	private Rule myRule;
	
	private int gridWidth;
	private int gridHeight;
	private Random myRand;
	private Collection<Cell[]> swapPairs;

	public Simulation(XMLReader reader){
		myReader = reader;
		myRand = new Random();
		swapPairs = new ArrayList<Cell[]>();
		reset();
	}
	
	public void reset() {
		gridWidth = myReader.getGridWidth(); 
		gridHeight = myReader.getGridHeight();
		myRule = myReader.getRule();
		myGrid = new HashMap<Point, Cell>();
		intializeGrid();
	}
	
	public void randomizeGrid() {
		for(int x = 0; x < gridWidth; x++) {
			for(int y = 0; y < gridHeight; y++) {
				Point point = new Point(x, y);
				int randomState = myRand.nextInt(myRule.getNumStates());
				Cell cell = placeCell(randomState, point);
				myGrid.put(point, cell);
			}
		}
	}
			
	public int getWidth(){
		return gridWidth; 
	}
	public int getHeight(){
		return gridHeight;
	}
	protected Random getRand() {
		return myRand;
	}
	protected Rule getRule() {
		return myRule;
	}
	protected XMLReader getReader() {
		return myReader;
	}
	protected Map<Point, Cell> getGrid() {
		return myGrid;
	}

	public Cell getCellAtPoint(Point myPoint){
		if(!myGrid.containsKey(myPoint)) {
			//TODO: add exceptions
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
	
	public void swapCells(Cell a, Cell b) {
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
		for(int x = 0; x < gridWidth; x++){
			for(int y = 0; y < gridHeight; y++){
				Cell myCell = myGrid.get(new Point(x, y));
				
				Collection<Point> myCellNeighborsCoords = myCell.getNeighborCoords();
				List<Integer> myNeighborStates = new ArrayList<Integer>();
				
				//gets neighbor states from their locations
				for(Point currentPoint : myCellNeighborsCoords) {
					Cell cell = getCellAtPoint(currentPoint);
					myNeighborStates.add(cell.getState());
				}
				
				myCell.computeNextState(myNeighborStates);
			}
		}
	}
	
	private void advanceGrid(){
		for(int x = 0; x < gridWidth; x++){
			for(int y = 0; y < gridHeight; y++){
				Point point = new Point(x, y);
				myGrid.get(point).advanceState();
			}
		}
	}
	
	protected void intializeGrid() {
		for(int x = 0; x < gridWidth; x++) {
			for(int y = 0; y < gridHeight; y++) {
				Point point = new Point(x, y);
				int initialState = myReader.getInitialStateAt(point);
				Cell cell = placeCell(initialState, point);
				myGrid.put(point, cell);
			}
		}
	}
	
	protected Cell placeCell(int initialState, Point point) {
		return new Cell(initialState, point, myRule, this);
	}
}
