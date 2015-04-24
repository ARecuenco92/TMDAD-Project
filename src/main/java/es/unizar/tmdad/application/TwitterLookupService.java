package es.unizar.tmdad.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import es.unizar.tmdad.domain.Filter;

@Service
public class TwitterLookupService {
	@Value("${twitter.consumerKey}")
	private String consumerKey;
	
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;
	
	@Value("${twitter.accessToken}")
	private String accessToken;
	
	@Value("${twitter.accessTokenSecret}")
	private String accessTokenSecret;
	
	public SearchResults search() {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        SearchParameters params = new  SearchParameters("@psoe OR @ahorapodemos OR @pp OR @ciudadanoscs");
        return twitter.searchOperations().search(params);
    }
	
	public List<Tweet> search(Filter filter){
		Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		List<Tweet> tweets;
		if(filter.getUser().equals("")){
			String query = getQuery(filter);
			SearchParameters params = new  SearchParameters(query);
	        tweets = twitter.searchOperations().search(params).getTweets();
		}
		else{
			tweets = twitter.timelineOperations().getUserTimeline(filter.getUser(),200);
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
				query = query + "OR @"+filter.getPoliticalParties().get(i);
			}
		}
		else{
			query = "@psoe OR @ahorapodemos OR @pp OR @ciudadanoscs";
		}
		if(filter.getKeyWords().contains(" ")){
			String[]keyWords = filter.getKeyWords().split(" ");
			for(int i=0; i< keyWords.length; i++){
				query = query + "AND " + keyWords[i];
			}
		}
		else{
			query = query + "AND " + filter.getKeyWords();
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
				if(texto.contains("@pp")||texto.contains("@psoe")||
						texto.contains("@ahorapodemos")||texto.contains("@ciudadanoscs")){
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
//	Stream<Tweet> s = q.stream();
//    List<Tweet> tweets = s.filter(t -> t.getUnmodifiedText().contains(filter.getKeyWords()))
//	.filter(t-> {
//    	boolean pasa = false;
//    	if(filter.getPoliticalParties()==null){
//    		return true;
//    	}
//    	for(int i = 0; i<filter.getPoliticalParties().size() && !pasa;i++){
//    	 if(t.getUnmodifiedText().contains(filter.getPoliticalParties().get(i))){
//    		 pasa = true;
//    	 }
//    	}
//    	return pasa;}).collect(Collectors.toList());
}

