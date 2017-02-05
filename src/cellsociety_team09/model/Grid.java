package cellsociety_team09.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cellsociety_team09.configuration.XMLReader;

public class Grid {	

	Map<Point, Cell> myGrid;
	XMLReader myReader;
	Rule myRule;
	
	int gridWidth;
	int gridHeight;
	Random myRand;

	public Grid(XMLReader reader){
		myReader = reader;
		myRand = new Random();
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
				Cell cell = new Cell(randomState, point, myRule);
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
	
	public Cell getCellAtPoint(Point myPoint){
		if(!myGrid.containsKey(myPoint)) {
			//TODO: add exceptions
			return null;
		}
		return myGrid.get(myPoint);
	}
	
	public void stepGrid(){
		computeNextGrid();
		advanceGrid();
	}
	
	public void requestSwap(Cell swapper, int desiredSwappee) {
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
			this.swapCells(swapper, swappee);
		} 
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
	
	private void intializeGrid() {
		for(int x = 0; x < gridWidth; x++) {
			for(int y = 0; y < gridHeight; y++) {
				Point point = new Point(x, y);
				int initialState = myReader.getInitialState(point);
				Cell cell = new Cell(initialState, point, myRule);
				myGrid.put(point, cell);
			}
		}
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
}
