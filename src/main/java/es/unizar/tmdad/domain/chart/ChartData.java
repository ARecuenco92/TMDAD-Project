package es.unizar.tmdad.domain.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartData {
	
	public List<String> labels;
	public List<Float> dataSet;
	
	public ChartData(){
		labels = new ArrayList<String>();
		dataSet = new ArrayList<Float>();
	}
	
	public void addData(String label, float data){
		this.labels.add(label);
		this.dataSet.add(data);
	}
	
	public List<String> getLabels(){
		return this.labels;
	}
	
	public List<Float> getDataSet(){
		return this.dataSet;
	}
	
	public void setLabels(List<String> labels){
		this.labels = labels;
	}
	
	public void setData(List<Float> data){
		this.dataSet = data;
	}
}
