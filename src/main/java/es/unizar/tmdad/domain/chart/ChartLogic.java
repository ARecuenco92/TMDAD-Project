package es.unizar.tmdad.domain.chart;

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
			List<Float> dataSet = data.getDataSet();
			float first = dataSet.get(0);
			int size = dataSet.size();
			for(int i = 1; i < size; i++){
				dataSet.set(i, (dataSet.get(i)-first));
			}
			data.getDataSet().remove(0);
			data.getLabels().remove(0);
			return data;
		};
		chart.mapChartData(function);
		return chart;
	}
	
	public Chart getAdherentsEvolutionPercentage(){
		Chart chart = repo.getAdherents();
		Function<? super ChartData, ? extends ChartData> function = data -> { 
			List<Float> dataSet = data.getDataSet();
			float first = dataSet.get(0);
			int size = dataSet.size();
			for(int i = 1; i < size; i++){
				dataSet.set(i, (dataSet.get(i)/first-1)*100);
			}
			data.getDataSet().remove(0);
			data.getLabels().remove(0);
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
