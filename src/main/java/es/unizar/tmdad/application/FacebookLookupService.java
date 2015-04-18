package es.unizar.tmdad.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import es.unizar.tmdad.domain.PoliticalParty;

@Service
public class FacebookLookupService {

	@Value("${facebook.appId}")
	private String appId;

	@Value("${facebook.appSecret}")
	private String appSecret;

	@Value("${facebook.accesToken}")
	private String accesToken;

	public PagedList<Post> search() {
		Facebook facebook = new FacebookTemplate(accesToken);
		PagedList<Post> list = facebook.feedOperations().getFeed("psoe");
		list.addAll(facebook.feedOperations().getFeed("pp"));
		list.addAll(facebook.feedOperations().getFeed("269212336568846"));
		list.addAll(facebook.feedOperations().getFeed("Cs.Ciudadanos"));
		Collections.sort(list, (Post p1, Post p2) -> p2.getCreatedTime().compareTo(p1.getCreatedTime()));

		return list;
	}

	public List<PoliticalParty> getPoliticalParties(){
		Facebook facebook = new FacebookTemplate(accesToken);

		List<PoliticalParty> parties = new ArrayList<PoliticalParty>();
		PoliticalParty p = getParty(facebook,"pp","blue");
		parties.add(p);
		p = getParty(facebook,"psoe","red");
		parties.add(p);
		p = getParty(facebook,"Cs.Ciudadanos","orange");
		parties.add(p);
		p = getParty(facebook,"269212336568846","purple");
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
