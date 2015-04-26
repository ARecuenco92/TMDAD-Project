package es.unizar.tmdad.application;

import org.springframework.beans.factory.annotation.Autowired;

import es.unizar.tmdad.domain.chart.Chart;
import es.unizar.tmdad.domain.chart.ChartRepository;
import es.unizar.tmdad.infraestructure.MySQLRepository;

public class ChartService {
	
	@Autowired
	ChartRepository repository;
	
	public ChartService(){
		repository = new MySQLRepository();
	}
	
	public Chart evolution(){
		return repository.getEvolution();
	}

}
