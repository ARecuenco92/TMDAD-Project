package es.unizar.tmdad.domain;

import org.springframework.social.twitter.api.Tweet;

public class PoliticalTweet {

	public String politicalParty;
	public Tweet tweet;
	
	public String getPoliticalParty() {
		return politicalParty;
	}
	
	public void setPoliticalParty(String politicalParty) {
		this.politicalParty = politicalParty;
	}
	
	public Tweet getTweet() {
		return tweet;
	}
	
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}
	
}
