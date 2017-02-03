package cellsociety_team09.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

import cellsociety_team09.configuration.XMLReader;

public class Grid {	

	Cell[][] myGrid;
	int amountOfRows;
	int amountOfCols;
	Rule myRule;

	public Grid(XMLReader myReader){
		//uses XML reader to store important information
		amountOfRows = myReader.getHeight();
		amountOfCols = myReader.getWidth(); 
		myRule = myReader.getRule();
		myGrid = new Cell[amountOfCols][amountOfRows];
		Integer[][] initialStates = myReader.getInitialGrid();
		//creates cells based on info from XMLreader
		for(int col = 0; col < amountOfCols; col++){
			for(int row = 0; row < amountOfRows; row++){
				Integer initialState = initialStates[col][row];
				Point myPoint = new Point(col, row);
				myGrid[col][row] = new Cell(initialState, myPoint, myRule);
			}
		}
	}
			
	
	public Cell getCellAtPoint(Point myPoint){
		//finds x and y of given point and locates the cell in the grid 
		//at that point
		int myX = myPoint.getX();
		int myY = myPoint.getY();
		return myGrid[myY][myX];
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
				Cell myCell = myGrid[currentCol][currentRow];
				//gets coordinates of currentCell
				List<Point> myCellNeighborsCoords = (List<Point>) myCell.getNeighborCoords();	//TODO: iterate over this as a Collection
				//points of all neighbors
				List<Point>  myPoints = new ArrayList<Point>(); //TODO: just use myCellNeighborsCoords (you don't need this list too) 
				//creates list to store all neighbors
				List<Cell> myNeighbors = new ArrayList<Cell>();	//TODO: this could be removed by combining some of the loops below
				//creates list to store all neighbor states
				List<Integer> myNeighborStates = new ArrayList<Integer>();
				//Stores points of all neighbors
				for(int k = 0 ; k < myCellNeighborsCoords.size(); k++){
					myPoints.add(myCellNeighborsCoords.get(k));
				}
				//locates all neighbors by stored points
				for(int k = 0; k < myPoints.size(); k++){
					Point currentPoint = myPoints.get(k);
					myNeighbors.add(getCellAtPoint(currentPoint));
				}
				//get states of all neighbors
				for(int k = 0; k < myNeighbors.size(); k++){
					myNeighborStates.add(myNeighbors.get(k).getState());
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
				myGrid[currentCol][currentRow].advanceState();
			}
		}
	}
	
	public Cell[][] getGrid(){
		return myGrid;
	}
	
}
