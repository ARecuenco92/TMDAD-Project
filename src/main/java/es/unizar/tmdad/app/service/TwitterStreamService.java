package es.unizar.tmdad.app.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import es.unizar.tmdad.domain.PoliticalTweet;

@Service
public class TwitterStreamService {
	@Value("${twitter.ciudadanos}")
	private String ciudadanosTwitter;

	@Value("${twitter.podemos}")
	private String podemosTwitter;

	@Value("${twitter.pp}")
	private String ppTwitter;

	@Value("${twitter.psoe}")
	private String psoeTwitter;
	
	@Autowired
	private SimpMessageSendingOperations ops;
	
	@Autowired
	private TwitterTemplate twitterTemplate;
	
	private Stream stream;

	@Autowired
	private StreamListener integrationStreamListener;

	@PostConstruct
	public void initialize() {
		FilterStreamParameters params = new FilterStreamParameters();
		params.track(podemosTwitter);
		params.track(ppTwitter);
		params.track(psoeTwitter);
		params.track(ciudadanosTwitter);
		
		stream = twitterTemplate.streamingOperations().filter(params, Arrays.asList(integrationStreamListener));		
	}
	
	public void sendTweet(PoliticalTweet tweet) {
		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		ops.convertAndSend("/queue/search/"+tweet.getPoliticalParty(), tweet.getTweet(), map);
	}

	public Stream getStream() {
		return stream;
	}

}