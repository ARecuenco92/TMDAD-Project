package es.unizar.tmdad.app.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import twitter4j.Twitter;


public abstract class TwitterService {
	protected static final int MAX_COUNT = 100;

	@Value("${twitter.ciudadanos}")
	protected String ciudadanosTwitter;

	@Value("${twitter.podemos}")
	protected String podemosTwitter;

	@Value("${twitter.pp}")
	protected String ppTwitter;

	@Value("${twitter.psoe}")
	protected String psoeTwitter;
	
	@Autowired
	protected TwitterTemplate twitterTemplate;

	@Autowired
	protected Twitter twitter;
}
