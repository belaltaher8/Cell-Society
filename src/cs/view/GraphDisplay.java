package cs.view;

import java.util.ArrayList;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * @author tahiaemran
 * this class creates graph part of the display, which displays the amounts of cells in each state and 
 * 	updates each time the grid is updated 
 * 
 *  dependent upon the simulation class to get aggregate numbers of cells in each state 
 *  
 *  to use: create a new GraphDisplay object in your GUI class, and every time you update the grid call
 *  createGraph on the GraphDisplay object to update the graph
 */

public class GraphDisplay {

	final NumberAxis xAxis; 
	final NumberAxis yAxis;  

	private LineChart<Number,Number> lineChart; 

	private ArrayList<XYChart.Series<Number, Number>> mySeries; 
	private XYChart.Series<Number, Number> stateSeries; 


	public GraphDisplay(GridDisplay myGridDisplay){
		xAxis = new NumberAxis(); 
		yAxis = new NumberAxis();
		xAxis.setLabel("Time");
		yAxis.setLabel("Cell Count");		
		mySeries = new ArrayList<XYChart.Series<Number, Number>>();
		initializeMySeries(myGridDisplay);
		createGraph(myGridDisplay, 0);
	}
	

	private void initializeMySeries(GridDisplay g) {
		Map <Integer, Integer> initialStates = g.getStateMap();
		for (Integer state: initialStates.keySet()){
			XYChart.Series<Number, Number> stateSeries = new XYChart.Series<Number, Number> ();
			stateSeries.setName(Integer.toString(state));
			mySeries.add(stateSeries);
		}

	}

	/**
	 * @param myGridDisplay - the display passed to the Graph by the GUI class 
	 * @param stepCount - this corresponds to the time value for the graph (x coordinate) 
	 * 
	 * This method is used to add points to the series in the graph display every time the grid is updated 
	 * 
	 */
	public void createGraph(GridDisplay myGridDisplay, int stepCount) {
		ArrayList<Series<Number, Number>> mySeriesCopy = new ArrayList<Series<Number, Number>>(mySeries);
		ArrayList<Series<Number, Number>> placeHolder = new ArrayList<Series<Number, Number>>();
		Map <Integer, Integer> currentStates = myGridDisplay.getStateMap();
		for (XYChart.Series<Number, Number> s: mySeriesCopy){
			int state = Integer.parseInt(s.getName());
			int count = currentStates.get(state);
			XYChart.Data<Number, Number> newPoint = new XYChart.Data<Number, Number>(stepCount, count);
			s.getData().add(newPoint);
			placeHolder.add(s);
		}
		mySeries = placeHolder; 
		lineChart = new LineChart<Number, Number>(xAxis, yAxis, FXCollections.observableArrayList(mySeries));
	}

	/**
	 * @return returns the chart associated with the GraphDisplay, required to actually see the graph 
	 * 		when a GraphDisplay object is created
	 */
	public Chart getChart(){
		return lineChart; 
	}



}
