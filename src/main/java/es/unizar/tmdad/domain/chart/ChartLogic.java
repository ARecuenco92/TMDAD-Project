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
			
			for(int i = 0; i < size; i++){
				newData.add(dataSet.get(i)-first);
				newLabels.add(labels.get(i));
			}
			data.setData(newData);
			data.setLabels(newLabels);
			return data;
		};
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
			
			for(int i = 0; i < size; i++){
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
			for(int i = 0; i < size; i++){
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
	
	public Chart getEvolutionParty(String party){
		ChartData charData;
		if(party.equals("podemos")){
			charData = repo.getAdherents("Podemos");
		}
		else if(party.equals("pp")){
			charData = repo.getAdherents("Partido Popular");
		}
		else if(party.equals("psoe")){
			charData = repo.getAdherents("Partido Socialista Obrero Español");
		}
		else{
			charData = repo.getAdherents("ciudadanos");
		}
		Chart chart = new Chart();
		chart.addChatData(charData);
		
		Function<? super ChartData, ? extends ChartData> function = data -> {
			List<Float> newData = new ArrayList<Float>();
			List<String> newLabels = new ArrayList<String>();
			List<Float> dataSet = data.getDataSet();
			List<String> labels = data.getLabels();
			float first = dataSet.get(0);
			int size = dataSet.size();
			
			for(int i = 0; i < size; i++){
				newData.add(dataSet.get(i)-first);
				newLabels.add(labels.get(i));
			}
			data.setData(newData);
			data.setLabels(newLabels);
			return data;
		};
		
		chart.mapChartData(function);
		return chart;
	}
	
	public Chart getComparativeParties(){
		Chart chart = repo.getAdherents();

		Function<? super ChartData, ? extends ChartData> function = data -> {
			List<Float> newData = new ArrayList<Float>();
			List<String> newLabels = new ArrayList<String>();
			List<Float> dataSet = data.getDataSet();
			List<String> labels = data.getLabels();
			
			newData.add(dataSet.get(dataSet.size()-1));
			newLabels.add(labels.get(dataSet.size()-1));
			
			data.setData(newData);
			data.setLabels(newLabels);
			return data;
		};
		
		chart.mapChartData(function);
		return chart;
	}
}
