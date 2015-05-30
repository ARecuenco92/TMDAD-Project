package es.unizar.tmdad.domain;


public class FullPoliticalParty extends PoliticalParty {

	public String phone;
	public LocationMapa location;
	
	public FullPoliticalParty(String name, String color, String logo, String webpage) {
		super(name, color, logo, webpage);
	}

	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public void setLocation(LocationMapa location){
		this.location = location;
	}
}
