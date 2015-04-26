package es.unizar.tmdad.domain;


public class FullPoliticalParty extends PoliticalParty {

	private String telephone;
	private Location location;
	
	public FullPoliticalParty(String name, String color, String logo, String webpage) {
		super(name, color, logo, webpage);
	}

}
