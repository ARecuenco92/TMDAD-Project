package es.unizar.tmdad.domain.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Chart {

	public List<ChartData> data;

	public Chart(){
		data = new ArrayList<ChartData>();
	}
	
	public void addChatData(ChartData chartData){
		data.add(chartData);
	}
	
	public void mapChartData(Function<? super ChartData, ? extends ChartData> function){
		data = data.stream().map(function).collect(Collectors.toList());
	}

}	

