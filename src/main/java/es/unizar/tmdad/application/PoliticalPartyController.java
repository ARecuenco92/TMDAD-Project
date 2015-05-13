package es.unizar.tmdad.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.Status;
import twitter4j.TwitterException;
import es.unizar.tmdad.domain.Filter;
import es.unizar.tmdad.domain.PoliticalParty;
import es.unizar.tmdad.domain.chart.Chart;

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

	@RequestMapping(value = "/search", headers = {"Content-type=application/json"}, method=RequestMethod.POST)
	@ResponseBody
	public List<Tweet> performSearch(@RequestBody Filter filter) {
		return twitter.search(filter);
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

	@RequestMapping(value = "/geolocalize", method = RequestMethod.GET)
	@ResponseBody
	public List<Status> geolocalize() throws TwitterException {
		return twitter.geolocalize();
	}

	@RequestMapping(value = "/timelineFacebook", method = RequestMethod.GET)
	@ResponseBody
	public List<Post> searchFace(){
		return facebook.search();
	}
	
	@RequestMapping(value = "/chart/evolution/percentage", method = RequestMethod.GET)
	@ResponseBody
	public Chart percentageEvolutonChart(){
		ChartService service = new ChartService();
		return service.getAdherentsEvolutionPercentage();
	}
	
	@RequestMapping(value = "/chart/evolution/absolute", method = RequestMethod.GET)
	@ResponseBody
	public Chart absoluteEvolutonChart(){
		ChartService service = new ChartService();
		return service.getAdherentsEvolution();
	}
}
