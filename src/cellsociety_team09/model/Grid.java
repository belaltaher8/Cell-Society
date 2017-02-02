package cellsociety_team09.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Grid {	

	Cell[][] myGrid;
	int amountOfRows;
	int amountOfCols;
	Rule myRule;

	public Grid(XMLReader myReader){
		amountOfRows = myReader.getHeight();
		amountOfCols = myReader.getWidth(); 
		myRule = myReader.getRule();
		Integer[][] initialStates = myReader.getInitialGrid();
		
		for(int col = 0; col < amountOfCols; col++){
			for(int row = 0; row < amountOfRows; row++){
				Integer initialState = initialStates[col][row];
				Point myPoint = new Point(col, row);
				myGrid[col][row] = new Cell(initialState, myPoint, myRule);
			}
		}
	}
			
	
	public Cell getCellAtPoint(Point myPoint){
		int myX = myPoint.getX();
		int myY = myPoint.getY();
		return myGrid[myY][myX];
	}
	
	public void stepGrid(){
		computeNextGrid();
		advanceGrid();
		
	}
	
	public void computeNextGrid(){
		for(int currentCol = 0; currentCol < amountOfCols; currentCol++){
			for(int currentRow = 0; currentRow < amountOfRows; currentRow++){
				//records current cell
				Cell myCell = myGrid[currentCol][currentRow];
				//gets coordinates of currentCell
				List<Point> myCellNeighborsCoords = myCell.getNeighborCoords();
				//points of all neighbors
				List<Point>  myPoints = new ArrayList<Point>();
				//creates list to store all neighbors
				List<Cell> myNeighbors = new ArrayList<Cell>();
				//creates list to store all neighbor states
				List<Integer> myNeighborStates = new ArrayList<Integer>();
				
				for(int k = 0 ; k < myCellNeighborsCoords.size(); k++){
					myPoints.add(myCellNeighborsCoords.get(k));
				}
				
				for(int k = 0; k < myPoints.size(); k++){
					Point currentPoint = myPoints.get(k);
					myNeighbors.add(getCellAtPoint(currentPoint));
				}
				
				for(int k = 0; k < myNeighbors.size(); k++){
					myNeighborStates.add(myNeighbors.get(k).getState());
				}
				
				myCell.computeNextState(myNeighborStates);
			}
		}
	}
	
	public void advanceGrid(){
		for(int currentCol = 0; currentCol < amountOfCols; currentCol++){
			for(int currentRow = 0; currentRow < amountOfRows; currentRow++){
				myGrid[currentCol][currentRow].advanceState();
			}
		}
	}
	
	public Cell[][] getGrid(){
		return myGrid;
	}
	
}
