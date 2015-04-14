package es.unizar.tmdad.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PoliticalPartyController {

	@Autowired
	TwitterLookupService twitter;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/timeline", method = RequestMethod.GET)
	@ResponseBody
	public SearchResults timeline() {
		return twitter.search();
	}
}
