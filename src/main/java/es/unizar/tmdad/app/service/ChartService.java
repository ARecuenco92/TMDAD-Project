package es.unizar.tmdad.app.service;

import org.springframework.stereotype.Service;

import es.unizar.tmdad.domain.chart.Chart;
import es.unizar.tmdad.domain.chart.ChartLogic;

@Service
public class ChartService {
	
	private ChartLogic logic;
	
	public ChartService(){
		logic = new ChartLogic();
	}
	
	public Chart getAdherentsEvolution(){
		return logic.getAdherentsEvolution();
	}

	public Chart getAdherentsEvolutionPercentage(){
		return logic.getAdherentsEvolutionPercentage();
	}
	
	public Chart getNewAdherentsDiary(){
		return logic.getNewAdherentsDiary();
	}
	
	public Chart getEvolutionParty(String party){
		return logic.getEvolutionParty(party);
	}
	
	public Chart getComparativeParties(){
		return logic.getComparativeParties();
	}
}
