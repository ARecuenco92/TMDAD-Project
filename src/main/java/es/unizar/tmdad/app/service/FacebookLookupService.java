package es.unizar.tmdad.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import es.unizar.tmdad.domain.GeoMessage;
import es.unizar.tmdad.domain.PoliticalParty;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Place.Location;
import facebook4j.ResponseList;





@Service
public class FacebookLookupService {

	private static final int MAXIMUM_POSTS = 25;

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
	private Facebook facebook;

	public List<Post> search() {
		PagedList<Post> list = facebookTemplate.feedOperations().getFeed(psoeFacebook);
		list.addAll(facebookTemplate.feedOperations().getFeed(ppFacebook));
		list.addAll(facebookTemplate.feedOperations().getFeed(podemosFacebook));
		list.addAll(facebookTemplate.feedOperations().getFeed(ciudadanosFacebook));
		Collections.sort(list, (Post p1, Post p2) -> p2.getCreatedTime().compareTo(p1.getCreatedTime()));

		return list.subList(0, MAXIMUM_POSTS);
	}
	
	public List<Post> search(String party) {
		PagedList<Post> list = null;
		switch(party.toLowerCase()){
		case "podemos":
			list = facebookTemplate.feedOperations().getFeed(podemosFacebook);
			break;
		case "pp":
			list = facebookTemplate.feedOperations().getFeed(ppFacebook);
			break;
		case "psoe":
			list = facebookTemplate.feedOperations().getFeed(psoeFacebook);
			break;
		case "ciudadanos":
			list = facebookTemplate.feedOperations().getFeed(ciudadanosFacebook);
			break;
		}
		if(list != null){
			return list.subList(0, MAXIMUM_POSTS);
		}
		else{
			return new ArrayList<Post>();
		}
	}


	public List<PoliticalParty> getPoliticalParties(){
		List<PoliticalParty> parties = new ArrayList<PoliticalParty>();
		PoliticalParty p = getParty(ppFacebook,"blue");
		parties.add(p);
		p = getParty(psoeFacebook,"red");
		parties.add(p);
		p = getParty(ciudadanosFacebook,"orange");
		parties.add(p);
		p = getParty(podemosFacebook,"purple");
		parties.add(p);

		return parties;
	}

	public PoliticalParty getParty(String id,String color){
		FacebookProfile profile = facebookTemplate.userOperations().getUserProfile(id);
		String name = profile.getName();
		String webside = profile.getWebsite();
		String logo = "http://graph.facebook.com/"+id+"/picture";
		PoliticalParty p = new PoliticalParty(name,color,logo,webside);
		return p;
	}

	public List<GeoMessage> geolocalize() throws FacebookException{	
		ResponseList<facebook4j.Post> list = facebook.posts().getFeed(psoeFacebook);
		list.addAll(facebook.posts().getFeed(ppFacebook));
		list.addAll(facebook.posts().getFeed(podemosFacebook));
		list.addAll(facebook.posts().getFeed(ciudadanosFacebook));

		return list.stream()
				.filter(page -> {
					Location loc =  page.getPlace() != null?  page.getPlace().getLocation() : null;
					return loc != null && loc.getLatitude() != null && loc.getLongitude() != null;
				})
				.map(page -> {
					Location loc =  page.getPlace().getLocation();
					GeoMessage post = new GeoMessage();
					post.setScreenName(page.getFrom().getName());
					post.setText(page.getMessage());
					post.setLatitude(loc.getLatitude());
					post.setLongitude(loc.getLongitude());
					
					return post;
				})
				.collect(Collectors.toList());
	}

}
