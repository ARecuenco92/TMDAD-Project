package es.unizar.tmdad.domain.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import es.unizar.tmdad.infraestructure.MySQLRepository;

public class ChartLogic {

	private ChartRepository repo;

	public ChartLogic(){
		repo = new MySQLRepository();
	}
	
	public Chart getAdherentsEvolution(){
		Chart chart = repo.getAdherents();
		Function<? super ChartData, ? extends ChartData> function = data -> {
			List<Float> newData = new ArrayList<Float>();
			List<String> newLabels = new ArrayList<String>();
			List<Float> dataSet = data.getDataSet();
			List<String> labels = data.getLabels();
			float first = dataSet.get(0);
			int size = dataSet.size();
			
			for(int i = 7; i < size; i+=7){
				newData.add(dataSet.get(i)-first);
				newLabels.add(labels.get(i));
			}
			data.setData(newData);
			data.setLabels(newLabels);
			return data;
		};
//		Function<? super ChartData, ? extends ChartData> function = data -> {
//			List<Float> dataSet = data.getDataSet();
//			float first = dataSet.get(0);
//			int size = dataSet.size();
//			for(int i = 1; i < size; i++){
//				dataSet.set(i, (dataSet.get(i)-first));
//			}
//			data.getDataSet().remove(0);
//			data.getLabels().remove(0);
//			return data;
//		};
		chart.mapChartData(function);
		return chart;
	}
	
	public Chart getAdherentsEvolutionPercentage(){
		Chart chart = repo.getAdherents();
		Function<? super ChartData, ? extends ChartData> function = data -> { 
			List<Float> newData = new ArrayList<Float>();
			List<String> newLabels = new ArrayList<String>();
			List<Float> dataSet = data.getDataSet();
			List<String> labels = data.getLabels();
			float first = dataSet.get(0);
			int size = dataSet.size();
			
			for(int i = 7; i < size; i+=7){
				newData.add((dataSet.get(i)/first-1)*100);
				newLabels.add(labels.get(i));
			}
			
			data.setData(newData);
			data.setLabels(newLabels);
			return data;
		};
		chart.mapChartData(function);
		return chart;
	}
	
	public Chart getNewAdherentsDiary(){
		Chart chart = repo.getAdherents();
		Function<? super ChartData, ? extends ChartData> function = data -> { 
			List<Float> dataSet = data.getDataSet();
			float lastDay = dataSet.get(0);
			int size = dataSet.size();
			for(int i = size-1; i > 0; i--){
				lastDay = dataSet.get(i-1);
				dataSet.set(i, (dataSet.get(i) - lastDay));
			}
			data.getDataSet().remove(0);
			data.getLabels().remove(0);
			return data;
		};
		chart.mapChartData(function);
		return chart;
	}
}
