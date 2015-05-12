package es.unizar.tmdad.domain.chart;


public interface ChartRepository {

	public Chart getFollowers();
	
	public ChartData getFollowers(String politicalParty);
	
	public Chart getLikes();
	
	public ChartData getLikes(String politicalParty);
	
	public Chart getAdherents();
	
	public ChartData getAdherents(String politicalParty);
	
}
