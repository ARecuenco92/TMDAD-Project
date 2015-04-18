package es.unizar.tmdad.domain;

import java.util.List;

public class FullPoliticalParty extends PoliticalParty {

	private String telephone;
	private Location location;

	// Historical Information
	private List<Amount> likesAmount;
	private List<Amount> followersAmount;
	private List<Amount> tweetsAmount;

	// Latest tweets & Facebook post
//	private List<MyTweet> tweets;
//	private List<MyPost> posts;
	
	public FullPoliticalParty(String name, String color, String logo, String webpage) {
		super(name, color, logo, webpage);
	}

}
