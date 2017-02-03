package cellsociety_team09.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import cellsociety_team09.configuration.XMLReader;

public class Grid {	

	XMLReader myReader;
	Map<Point, Cell> myGrid;
	int amountOfRows;
	int amountOfCols;
	Rule myRule;

	public Grid(XMLReader reader){
		//uses XML reader to store important information
		myReader = reader;
		
		amountOfRows = myReader.getHeight();
		amountOfCols = myReader.getWidth(); 
		myRule = myReader.getRule();
		myGrid = new HashMap<Point, Cell>();
		
		intialize();
	}
			
	public int getNumRows(){
		return amountOfRows; 
	}
	public int getNumCols(){
		return amountOfCols;
	}
	public Cell getCellAtPoint(Point myPoint){
		if(!myGrid.containsKey(myPoint)) {
			//TODO: add exceptions
			return null;
		}
		return myGrid.get(myPoint);
	}
	
	public void stepGrid(){
		//computes next states of cells
		computeNextGrid();
		//advances cells to their next state
		advanceGrid();
	}
	
	public void computeNextGrid(){
		for(int currentCol = 0; currentCol < amountOfCols; currentCol++){
			for(int currentRow = 0; currentRow < amountOfRows; currentRow++){
				//records current cell
				Point point = new Point(currentRow, currentCol);
				Cell myCell = myGrid.get(point);
				//gets coordinates of currentCell
				List<Point> myCellNeighborsCoords = (List<Point>) myCell.getNeighborCoords();	//TODO: iterate over this as a Collection
				//points of all neighbors
				
				//creates list to store all neighbor states
				List<Integer> myNeighborStates = new ArrayList<Integer>();
				
				//locates all neighbors by stored points
				for(int k = 0; k < myCellNeighborsCoords.size(); k++){
					Point currentPoint = myCellNeighborsCoords.get(k);
					Cell cell = getCellAtPoint(currentPoint);
					myNeighborStates.add(cell.getState());
				}
				
				//computes next state of current cell by states of neighbors
				myCell.computeNextState(myNeighborStates);
			}
		}
	}
	
	public void advanceGrid(){
		for(int currentCol = 0; currentCol < amountOfCols; currentCol++){
			for(int currentRow = 0; currentRow < amountOfRows; currentRow++){
				//advances state of each individual cell
				Point point = new Point(currentRow, currentCol);
				myGrid.get(point).advanceState();
			}
		}
	}
	
	private void intialize() {
		for(int r = 0; r < amountOfRows; r++) {
			for(int c = 0; c < amountOfCols; c++) {
				Point point = new Point(r,c);
				int initialState = myReader.getInitialState(point);
				Cell cell = new Cell(initialState, point, myRule);
				myGrid.put(new Point(r,c), cell);
			}
		}
	}
}
