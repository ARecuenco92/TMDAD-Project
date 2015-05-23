package es.unizar.tmdad.app.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import es.unizar.tmdad.domain.Filter;

@Service
public class TwitterLookupService {
	@Value("${twitter.ciudadanos}")
	private String ciudadanosTwitter;

	@Value("${twitter.podemos}")
	private String podemosTwitter;

	@Value("${twitter.pp}")
	private String ppTwitter;

	@Value("${twitter.psoe}")
	private String psoeTwitter;
	
	@Autowired
	private TwitterTemplate twitterTemplate;

	@Autowired
	private Twitter twitter;
	
	public SearchResults search() {
		String query = ciudadanosTwitter+" OR "+podemosTwitter+" OR "+ppTwitter+" OR "+psoeTwitter;
		SearchParameters params = new  SearchParameters(query);
		return twitterTemplate.searchOperations().search(params);
	}

	public List<Tweet> search(Filter filter){
		List<Tweet> tweets;
		if(filter.getUser().equals("")){
			String query = getQuery(filter);
			SearchParameters params = new  SearchParameters(query);
			tweets = twitterTemplate.searchOperations().search(params).getTweets();
		}
		else{
			tweets = twitterTemplate.timelineOperations().getUserTimeline(filter.getUser(),200);
			tweets = filtrarTweets(tweets,filter);
		}

		if(filter.getSortBy().equals("retweets")){
			Comparator<Tweet> byRetweets = (Tweet e1, Tweet e2) -> Integer
					.compare(e1.getRetweetCount(), e2.getRetweetCount());
			tweets = tweets.stream().sorted(byRetweets.reversed()).collect(Collectors.toList());
		}
		else if(filter.getSortBy().equals("favorites")){
			Comparator<Tweet> byFav= (Tweet e1, Tweet e2) -> Integer
					.compare(e1.getFavoriteCount(), e2.getFavoriteCount());
			tweets = tweets.stream().sorted(byFav.reversed()).collect(Collectors.toList());
		}
		return tweets;
	}

	public String getQuery(Filter filter){
		String query = "";
		if(filter.getPoliticalParties()!=null){
			query = query + "@"+filter.getPoliticalParties().get(0);
			for(int i = 1; i< filter.getPoliticalParties().size();i++){
				query = query + " OR @"+filter.getPoliticalParties().get(i);
			}
		}
		else{
			query = ciudadanosTwitter+" OR "+podemosTwitter+" OR "+ppTwitter+" OR "+psoeTwitter;
		}
		if(filter.getKeyWords().contains(" ")){
			String[]keyWords = filter.getKeyWords().split(" ");
			for(int i=0; i< keyWords.length; i++){
				query = query + " AND " + keyWords[i];
			}
		}
		else{
			query = query + " AND " + filter.getKeyWords();
		}
		return query;
	}

	public List<Tweet> filtrarTweets(List<Tweet> tweets,Filter filter){
		tweets = tweets.stream().filter(t -> {
			String texto = t.getUnmodifiedText();
			boolean pasa = true;
			if(filter.getPoliticalParties()!=null){
				for(int i=0; i<filter.getPoliticalParties().size();i++){
					if(!texto.contains("@"+filter.getPoliticalParties().get(i))){
						return false;
					}
				}
			}
			else{
				if(texto.contains(ppTwitter)||texto.contains(psoeTwitter)||
						texto.contains(podemosTwitter)||texto.contains(ciudadanosTwitter)){
					return true;
				}
				else
					return false;
			}
			return pasa;
		}).filter(t->{
			if(filter.getKeyWords().contains(" ")){
				String[] words = filter.getKeyWords().split(" ");
				for(int i = 0; i<words.length;i++){
					if(!t.getUnmodifiedText().contains(words[i])){
						return false;
					}
				}
				return true;
			}
			else{
				if(!t.getUnmodifiedText().contains(filter.getKeyWords())){
					return false;
				}
				return true;
			}
		}).collect(Collectors.toList());
		return tweets;
	}

	public List<Status> geolocalize() throws TwitterException{	
		String query = ciudadanosTwitter+" OR "+podemosTwitter+" OR "+ppTwitter+" OR "+psoeTwitter;
		Query searchQuery = new Query();
		searchQuery.setQuery(query);
		GeoLocation geo = new GeoLocation(40.400, 3.683);
		searchQuery.setGeoCode(geo, 1000, Query.Unit.km);
		searchQuery.setCount(100);
		
		return twitter.search(searchQuery).getTweets()
			.stream()
			.filter(status -> status.getGeoLocation() != null)
			.collect(Collectors.toList());
	}
}

