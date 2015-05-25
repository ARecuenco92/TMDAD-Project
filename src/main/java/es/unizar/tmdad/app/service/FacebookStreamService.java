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
	
	public FacebookStreamService(){
		podemosLastPost = new Date();
		ppLastPost = new Date();
		psoeLastPost = new Date();
		ciudadanosLastPost = new Date();
	}
	
	public void sendPost() {
		podemosLastPost = sendPost("podemos", podemosFacebook, podemosLastPost);
		ppLastPost = sendPost("pp", ppFacebook, ppLastPost);
		psoeLastPost = sendPost("psoe", psoeFacebook, psoeLastPost);
		ciudadanosLastPost = sendPost("ciudadanos", ciudadanosFacebook, ciudadanosLastPost);
	}
	
	private Date sendPost(String party, String facebook, Date lastDate){
		List<Post> posts = facebookTemplate.feedOperations()
				.getFeed(facebook)
				.stream()
				.filter(post ->{
					return post.getCreatedTime()!= null && post.getCreatedTime().after(lastDate);
				})
				.collect(Collectors.toList());
		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		Collections.sort(posts, (Post p1, Post p2) -> p2.getCreatedTime().compareTo(p1.getCreatedTime()));
		
		posts.stream().forEach(post -> ops.convertAndSend("/queue/facebook/"+party, post, map));
		
		if(posts.size()>0){
			return posts.get(0).getCreatedTime();
		}
		
		return lastDate;
	}

}
