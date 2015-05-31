package es.unizar.tmdad.domain;

import java.util.List;

public class Filter {

	List<String> politicalParties;
	String user;
	String keyWords;
	String sortBy;
	int radius;
	double latitude;
	double longitude;
	
	public List<String> getPoliticalParties() {
		return politicalParties;
	}
	
	public void setPoliticalParties(List<String> politicalParties) {
		this.politicalParties = politicalParties;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getKeyWords() {
		return keyWords;
	}
	
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	public String getSortBy() {
		return sortBy;
	}
	
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
