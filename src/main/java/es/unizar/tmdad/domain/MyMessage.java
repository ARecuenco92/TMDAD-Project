package es.unizar.tmdad.domain;

import java.util.ArrayList;
import java.util.List;

public class MyMessage {

	public String screenName;
	public String text;
	public int relevance;
	public double latitude;
	public double longitude;
	public List<String> mentions;
	
	public MyMessage(){
		mentions = new ArrayList<String>();
	}
	
	public String getScreenName() {
		return screenName;
	}
	
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getRelevance() {
		return relevance;
	}
	
	public void setRelevance(int relevance) {
		this.relevance = relevance;
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
	
	public void addMention(String mention){
		mentions.add(mention);	
	}
}
