package es.unizar.tmdad.application;

import es.unizar.tmdad.domain.chart.Chart;
import es.unizar.tmdad.domain.chart.ChartLogic;

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
}
