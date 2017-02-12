package cs.view;

import java.util.ArrayList;

import cs.model.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.util.Duration;

public class GraphDisplay {
	
	final NumberAxis xAxis; 
	final NumberAxis yAxis;  
	
	final LineChart<Number,Number> lineChart;
	
	private ArrayList<Series<Number, Number>> mySeries; 
	
	private Simulation mySimulation; 

	
	private Timeline graphAnimation; 
	private static final double ANIMATION_SPEED = 250; 
	private int stepCount; 
	
	public GraphDisplay(Simulation sim){
		xAxis = new NumberAxis(); 
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		xAxis.setLabel("Time");
		yAxis.setLabel("Cell Count");
		stepCount = 0; 
		
		mySimulation = sim;
		
		mySeries = new ArrayList<XYChart.Series<Number, Number>>();
		
		initializeSeries();
		animateGraph(); 
		
	}
	
	public Chart getChart(){
		return lineChart; 
	}
	
	private void animateGraph() {
		graphAnimation = new Timeline();
		graphAnimation.getKeyFrames().clear();
		graphAnimation.setCycleCount(Timeline.INDEFINITE);
		KeyFrame frame = new KeyFrame(Duration.millis(ANIMATION_SPEED), e->stepAnimation()); //could set to update in realtime
		graphAnimation.getKeyFrames().add(frame);
		
	}
	
	private void stepAnimation() {
		stepCount++; 
		for (XYChart.Series<Number, Number> s: mySeries){
			lineChart.getData().remove(s);
			int state = Integer.parseInt(s.getName()); 
			s.getData().add(new XYChart.Data<Number, Number>(stepCount, mySimulation.calculateNumInState(state)));
			lineChart.getData().add(s);
		}
	}
	/**
	 * initializes one time series for every state in the simulation  
	 * adds the initial number of cells in that state (at time 0) to the graph
	 */
	private void initializeSeries() {
		int numSeries = mySimulation.getConfig().getNumStates(); 
		for (int i=0; i<numSeries;i++){ // states correspond to i??
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(Integer.toString(i)); // will want to get labels from corresponding resource bundle 
			int numOfState =mySimulation.calculateNumInState(i);
			series.getData().add(new XYChart.Data<Number, Number>(0,numOfState));
			lineChart.getData().add(series);
			mySeries.add(series);
		}	
	}


}
