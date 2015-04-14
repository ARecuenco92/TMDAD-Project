package es.unizar.tmdad.domain;

import java.util.List;

public class PoliticalParty {
	
	// Profile attributes
	private String name;
	private String profile;
	private String logo;
	
	// Contact information
	private String webpage;
	private String telephone;
	private Location location;

	// Historical Information
	private List<Amount> likesAmount;
	private List<Amount> followersAmount;
	private List<Amount> tweetsAmount;

	// Latest tweets & Facebook post
//	private List<MyTweet> tweets;
//	private List<MyPost> posts;
}
