package cellsociety_team09.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cellsociety_team09.configuration.XMLReader;

public class Grid {	

	XMLReader myReader;
	Map<Point, Cell> myGrid;
	int gridWidth;
	int gridHeight;
	Rule myRule;

	public Grid(XMLReader reader){
		myReader = reader;
		reset();
	}
	
	public void reset() {
		gridWidth = myReader.getGridHeight();
		gridHeight = myReader.getGridWidth(); 
		myRule = myReader.getRule();
		
		myGrid = new HashMap<Point, Cell>();
		intializeGrid();
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
	
	private void computeNextGrid(){
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
}
