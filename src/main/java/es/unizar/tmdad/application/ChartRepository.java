package es.unizar.tmdad.application;

import java.util.List;

import es.unizar.tmdad.domain.Amount;

public interface ChartRepository {

	public List<Amount> getFollowers(String politicalParty);
	
	public List<Amount> getLikes(String politicalParty);
	
	public List<Amount> getEvolution (String politicalParty);
}
