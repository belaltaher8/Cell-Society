package cellsociety_team09.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Grid {	

	Cell[][] myGrid;
	int amountOfRows;
	int amountOfCols;


	public Cell getCellAtGrid(Point myPoint){
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
					myNeighbors.add(getCellAtGrid(currentPoint));
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
	
}
