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
		//uses XML reader to store important information
		myReader = reader;
		
		gridWidth = myReader.getHeight();
		gridHeight = myReader.getWidth(); 
		myRule = myReader.getRule();
		
		myGrid = new HashMap<Point, Cell>();
		
		intialize();
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
		//computes next states of cells
		computeNextGrid();
		//advances cells to their next state
		advanceGrid();
	}
	
	public void computeNextGrid(){
		for(int x = 0; x < gridWidth; x++){
			for(int y = 0; y < gridHeight; y++){
				Cell myCell = myGrid.get(new Point(x, y));
				
				Collection<Point> myCellNeighborsCoords = myCell.getNeighborCoords();
				List<Integer> myNeighborStates = new ArrayList<Integer>();
				
				//locates all neighbors by stored points
				for(Point currentPoint : myCellNeighborsCoords) {
					Cell cell = getCellAtPoint(currentPoint);
					myNeighborStates.add(cell.getState());
				}
				
				//computes next state of current cell by states of neighbors
				myCell.computeNextState(myNeighborStates);
			}
		}
	}
	
	public void advanceGrid(){
		for(int x = 0; x < gridWidth; x++){
			for(int y = 0; y < gridHeight; y++){
				//advances state of each individual cell
				Point point = new Point(x, y);
				myGrid.get(point).advanceState();
			}
		}
	}
	
	private void intialize() {
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
