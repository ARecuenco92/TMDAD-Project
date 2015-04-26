package es.unizar.tmdad.domain.chart;


public interface ChartRepository {

	public ChartData getFollowers(String politicalParty);
	
	public ChartData getLikes(String politicalParty);
	
	public Chart getEvolution();
	
	public ChartData getEvolution (String politicalParty);
}
