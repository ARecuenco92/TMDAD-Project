package es.unizar.tmdad.domain;

import java.util.ArrayList;
import java.util.List;

public class GeoMessage {

	public String screenName;
	public String text;
	public String img;
	public int sharedCount;
	public int likesCount;
	public int relevance;
	public double latitude;
	public double longitude;
	public List<String> mentions;
	
	public GeoMessage(){
		mentions = new ArrayList<String>();
		likesCount = 0;
		sharedCount = 0;
		calculateRelevance();
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
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getSharedCount() {
		return sharedCount;
	}
	
	public void setSharedCount(int sharedCount) {
		this.sharedCount = sharedCount;
		calculateRelevance();
	}
	
	public int getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
		calculateRelevance();
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
	
	private void calculateRelevance(){
		relevance = (sharedCount*2+likesCount)/3 + 1;
	}
}
