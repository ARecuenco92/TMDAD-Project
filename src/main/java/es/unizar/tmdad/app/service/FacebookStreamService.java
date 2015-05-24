package es.unizar.tmdad.app.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class FacebookStreamService {
	
	@Value("${facebook.ciudadanos}")
	private String ciudadanosFacebook;

	@Value("${facebook.podemos}")
	private String podemosFacebook;

	@Value("${facebook.pp}")
	private String ppFacebook;

	@Value("${facebook.psoe}")
	private String psoeFacebook;
	
	@Autowired
	private FacebookTemplate facebookTemplate;
	
	@Autowired
	private SimpMessageSendingOperations ops;
	
	@Scheduled(fixedDelay=5000)
	public void sendPost() {
		sendPost(psoeFacebook);
	}
	
	private void sendPost(String party){
		PagedList<Post> list = facebookTemplate.feedOperations().getFeed(party);
		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		ops.convertAndSend("/queue/facebook/"+party, list.get(0), map);
	}

}
