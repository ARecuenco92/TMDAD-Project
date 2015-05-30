package es.unizar.tmdad.domain;

public class LocationMapa {
	
	public String country;
	public String city;
	public String street;
	public String zip;

	public LocationMapa(String city, String country, String street, String zip){
		this.country = country;
		this.city = city;
		this.street = street;
		this.zip = zip;
	}
}
