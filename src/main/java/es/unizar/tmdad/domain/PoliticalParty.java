package es.unizar.tmdad.domain;


public class PoliticalParty {
	
	// Profile attributes
	public String name;
	public String color;
	public String logo;
	
	// Contact information
	public String webpage;
	
	public PoliticalParty(String name, String color, String logo, String webpage){
		this.name = name;
		this.color = color;
		this.logo = logo;
		this.webpage = webpage;
	}
	
}
