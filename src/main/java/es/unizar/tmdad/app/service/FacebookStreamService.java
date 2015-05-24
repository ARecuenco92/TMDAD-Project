package es.unizar.tmdad.app.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.facebook.api.Post;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class FacebookStreamService extends FacebookService{
	
	private Date podemosLastPost;
	private Date ppLastPost;
	private Date psoeLastPost;
	private Date ciudadanosLastPost;
	
	@Autowired
	private SimpMessageSendingOperations ops;
	
	public void sendPost() {
		sendPost(podemosFacebook);
	}
	
	private void sendPost(String party){
		podemosLastPost = podemosLastPost != null? podemosLastPost : lastPost; 
		List<Post> posts = facebookTemplate.feedOperations()
				.getFeed(party)
				.stream()
				.filter(post ->{
					return post.getCreatedTime()!= null && podemosLastPost!= null && post.getCreatedTime().after(podemosLastPost);
				})
				.collect(Collectors.toList());
		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		Collections.sort(posts, (Post p1, Post p2) -> p2.getCreatedTime().compareTo(p1.getCreatedTime()));
		
		if(posts.size()>0){
			podemosLastPost = posts.get(0).getCreatedTime();
		}
		
		posts.stream().forEach(post -> ops.convertAndSend("/queue/facebook/"+party, posts.get(0), map));
	}

}
