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

	private ArrayList<Series<Number, Number>> mySeries; 


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
		Map <Integer, Integer> currentStates = myGridDisplay.getStateMap();
		for (XYChart.Series<Number, Number> s: mySeriesCopy){
			int state = Integer.parseInt(s.getName());
			int count = currentStates.get(state);
			XYChart.Data<Number, Number> newPoint = new XYChart.Data<Number, Number>(stepCount, count);
			s.getData().add(newPoint);
			mySeries.remove(s);
			mySeries.add(s);
			
		}
	 
		/*TESTER
		 * for (int i=0;i<mySeries.size();i++){
			System.out.println(mySeries.get(i).getData());
		}
		System.out.println("BREAK");
		*/
		
		lineChart = new LineChart<Number, Number>(xAxis, yAxis, FXCollections.observableArrayList(mySeries));
	}

	public Chart getChart(){
		return lineChart; 
	}



}
