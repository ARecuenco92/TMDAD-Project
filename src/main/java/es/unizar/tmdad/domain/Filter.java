package es.unizar.tmdad.domain;

import java.util.List;

public class Filter {

	List<String> politicalParties;
	String user;
	String keyWords;
	String sortBy;
	
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
	
}
