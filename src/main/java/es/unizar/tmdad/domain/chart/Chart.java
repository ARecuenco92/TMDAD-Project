package es.unizar.tmdad.domain.chart;

import java.util.ArrayList;
import java.util.List;

public class Chart {

	public List<ChartData> data;

	public Chart(){
		data = new ArrayList<ChartData>();
	}
	
	public void addChatData(ChartData chartData){
		data.add(chartData);
	}

}	

