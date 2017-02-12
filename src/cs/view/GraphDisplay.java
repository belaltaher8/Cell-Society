package cs.view;

import java.util.ArrayList;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class GraphDisplay {

	final NumberAxis xAxis; 
	final NumberAxis yAxis;  

	private LineChart<Number,Number> lineChart; 

	private ArrayList<XYChart.Series<Number, Number>> mySeries; 
	private XYChart.Series<Number, Number> stateSeries; 


	public GraphDisplay(GridDisplay myGridDisplay){

		// initialize axes + graph title 
		xAxis = new NumberAxis(); 
		yAxis = new NumberAxis();
		xAxis.setLabel("Time");
		yAxis.setLabel("Cell Count");		
		// initialize myseries 
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

	public Chart getChart(){
		return lineChart; 
	}



}
