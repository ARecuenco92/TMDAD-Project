package es.unizar.tmdad.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import es.unizar.tmdad.domain.PoliticalParty;

@Service
public class FacebookLookupService {

	private static final int MAXIMUM_POSTS = 25;

	@Autowired
	private FacebookTemplate facebookTemplate;

	public List<Post> search() {
		PagedList<Post> list = facebookTemplate.feedOperations().getFeed("psoe");
		list.addAll(facebookTemplate.feedOperations().getFeed("pp"));
		list.addAll(facebookTemplate.feedOperations().getFeed("269212336568846"));
		list.addAll(facebookTemplate.feedOperations().getFeed("Cs.Ciudadanos"));
		Collections.sort(list, (Post p1, Post p2) -> p2.getCreatedTime().compareTo(p1.getCreatedTime()));

		return list.subList(0, MAXIMUM_POSTS);
	}

	public List<PoliticalParty> getPoliticalParties(){
		List<PoliticalParty> parties = new ArrayList<PoliticalParty>();
		PoliticalParty p = getParty(facebookTemplate,"pp","blue");
		parties.add(p);
		p = getParty(facebookTemplate,"psoe","red");
		parties.add(p);
		p = getParty(facebookTemplate,"Cs.Ciudadanos","orange");
		parties.add(p);
		p = getParty(facebookTemplate,"269212336568846","purple");
		parties.add(p);

		return parties;
	}

	public PoliticalParty getParty(Facebook facebook, String id,String color){
		FacebookProfile profile = facebook.userOperations().getUserProfile(id);
		String name = profile.getName();
		String webside = profile.getWebsite();
		String logo = "http://graph.facebook.com/"+id+"/picture";
		PoliticalParty p = new PoliticalParty(name,color,logo,webside);
		return p;
	}

}
