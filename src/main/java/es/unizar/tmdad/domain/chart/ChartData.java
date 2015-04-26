package es.unizar.tmdad.domain.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartData {
	
	public List<String> label;
	public List<Float> dataSet;
	
	public ChartData(){
		label = new ArrayList<String>();
		dataSet = new ArrayList<Float>();
	}
	
	public void addData(String label, float data){
		this.label.add(label);
		this.dataSet.add(data);
	}
}
