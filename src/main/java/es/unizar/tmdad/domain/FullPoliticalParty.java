package es.unizar.tmdad.domain;


public class FullPoliticalParty extends PoliticalParty {

	public String email;
	public LocationMapa location;
	
	public FullPoliticalParty(String name, String color, String logo, String webpage) {
		super(name, color, logo, webpage);
	}

	public void setEmail(String email){
		this.email = email;
	}
	
	public void setLocation(LocationMapa location){
		this.location = location;
	}
}
