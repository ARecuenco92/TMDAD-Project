package es.unizar.tmdad.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.TwitterException;
import es.unizar.tmdad.app.service.ChartService;
import es.unizar.tmdad.app.service.FacebookLookupService;
import es.unizar.tmdad.app.service.TwitterLookupService;
import es.unizar.tmdad.domain.Filter;
import es.unizar.tmdad.domain.FullPoliticalParty;
import es.unizar.tmdad.domain.GeoMessage;
import es.unizar.tmdad.domain.PoliticalParty;
import es.unizar.tmdad.domain.chart.Chart;
import facebook4j.FacebookException;

@Controller
public class RestController {

	@Autowired
	TwitterLookupService twitter;

	@Autowired
	FacebookLookupService facebook;

	@RequestMapping(value= "/", method=RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/chart", method=RequestMethod.GET)
	public String chart() {
		return "chart";
	}
	

	@RequestMapping(value = "/search", method=RequestMethod.GET)
	public String search() {
		return "search";
	}
	
	@RequestMapping(value = "/polparty", method=RequestMethod.GET)
	public String polparty() {
		return "polparty";
	}

	@RequestMapping(value = "/search", headers = {"Content-type=application/json"}, method=RequestMethod.POST)
	@ResponseBody
	public List<Tweet> performSearch(@RequestBody Filter filter) {
		return twitter.search(filter);
	}

	@RequestMapping(value = "/resumeParties", method = RequestMethod.GET)
	@ResponseBody
	public List<PoliticalParty> getPoliticalParties(){
		return facebook.getPoliticalParties();
	}
	
	@RequestMapping(value = "/twitter/timeline", method = RequestMethod.GET)
	@ResponseBody
	public SearchResults twitterTimeline() {
		return twitter.search();
	}
	
	@RequestMapping(value = "/twitter/timeline/{party}", method = RequestMethod.GET)
	@ResponseBody
	public SearchResults twitterPartyTimeline(@PathVariable(value="party") String party) {
		return twitter.search(party);
	}

	@RequestMapping(value = "/twitter/geolocalize", method = RequestMethod.GET)
	@ResponseBody
	public List<GeoMessage> twitterGeolocalize() throws TwitterException {
		return twitter.geolocalize();
	}
	
	@RequestMapping(value = "/facebook/geolocalize", method = RequestMethod.GET)
	@ResponseBody
	public List<GeoMessage> facebookGeolocalize() throws FacebookException {
		return facebook.geolocalize();
	}

	@RequestMapping(value = "/facebook/timeline", method = RequestMethod.GET)
	@ResponseBody
	public List<Post> facebookTimeline(){
		return facebook.search();
	}
	
	@RequestMapping(value = "/facebook/timeline/{party}", method = RequestMethod.GET)
	@ResponseBody
	public List<Post> facebookParyTimeline(@PathVariable(value="party") String party){
		return facebook.search(party);
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
	
	@RequestMapping(value = "/polparty/fullParty/{party}",  method=RequestMethod.GET)
	@ResponseBody
	public FullPoliticalParty getFullParty(@PathVariable(value="party") String party){
		return facebook.getFullPoliticalParty(party); 
				
	}
}
