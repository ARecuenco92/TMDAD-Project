package es.unizar.tmdad.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.unizar.tmdad.domain.PoliticalParty;

@Controller
public class PoliticalPartyController {

	@Autowired
	TwitterLookupService twitter;
	
	@Autowired
	FacebookLookupService facebook;
	
	@RequestMapping(value= "/", method=RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/search", method=RequestMethod.GET)
	public String search() {
		return "search";
	}
	
	@RequestMapping(value = "/search", method=RequestMethod.POST)
	@ResponseBody
	public String performSearch(@RequestBody String json) {
		return "search";
	}
	
	@RequestMapping(value = "/timeline", method = RequestMethod.GET)
	@ResponseBody
	public SearchResults timeline() {
		return twitter.search();
	}
	
	@RequestMapping(value = "/resumeParties", method = RequestMethod.GET)
	@ResponseBody
	public List<PoliticalParty> getPoliticalParties(){
		return facebook.getPoliticalParties();
	}
	
	@RequestMapping(value = "/timelineFacebook", method = RequestMethod.GET)
	@ResponseBody
	public List<Post> searchFace(){
		return facebook.search();
	}
}
