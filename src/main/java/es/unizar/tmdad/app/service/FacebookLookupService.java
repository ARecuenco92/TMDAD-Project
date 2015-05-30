package es.unizar.tmdad.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Service;

import es.unizar.tmdad.domain.FullPoliticalParty;
import es.unizar.tmdad.domain.GeoMessage;
import es.unizar.tmdad.domain.LocationMapa;
import es.unizar.tmdad.domain.PoliticalParty;
import facebook4j.FacebookException;
import facebook4j.Place.Location;
import facebook4j.ResponseList;

@Service
public class FacebookLookupService extends FacebookService{

	public List<Post> search() {
		PagedList<Post> list = facebookTemplate.feedOperations().getFeed(psoeFacebook);
		list.addAll(facebookTemplate.feedOperations().getFeed(ppFacebook));
		list.addAll(facebookTemplate.feedOperations().getFeed(podemosFacebook));
		list.addAll(facebookTemplate.feedOperations().getFeed(ciudadanosFacebook));
		Collections.sort(list, (Post p1, Post p2) -> p2.getCreatedTime().compareTo(p1.getCreatedTime()));

		List<Post> posts = list.subList(0, MAXIMUM_POSTS);

		return posts;
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

	public FullPoliticalParty getFullPoliticalParty(String nameParty){
		FullPoliticalParty party;
		String id, color;
		LocationMapa location = null;
		if(nameParty.equals("podemos")){
			id = podemosFacebook;
			color = "purple";
			location = new LocationMapa("Spain","Madrid","Calle Zurita, 21","28005");
		}
		else if(nameParty.equals("pp")){
			id = ppFacebook;
			color = "blue";
		}
		else if(nameParty.equals("ciudadanos")){
			id = ciudadanosFacebook;
			color = "orange";
		}
		else{
			id = psoeFacebook;
			color = "red";
		}
		Page profile = facebookTemplate.pageOperations().getPage(id);
		String name = profile.getName();
		String webside = profile.getWebsite();
		String logo = "http://graph.facebook.com/"+id+"/picture";
		party = new FullPoliticalParty(name, color, logo, webside);
		
		String phone = profile.getPhone() == null? "No disponible" :  profile.getPhone();
		party.setPhone(phone);
		org.springframework.social.facebook.api.Location loc = profile.getLocation();
		if(loc != null){
			location = new LocationMapa(loc.getCity(),loc.getCountry(),loc.getStreet(),loc.getZip());
		}
		party.setLocation(location);
		return party;
	}

}
