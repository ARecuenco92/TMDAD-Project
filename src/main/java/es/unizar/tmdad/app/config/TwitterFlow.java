package es.unizar.tmdad.app.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Tweet;

import es.unizar.tmdad.domain.PoliticalTweet;

@Configuration
@EnableIntegration
@IntegrationComponentScan
@ComponentScan
public class TwitterFlow {

	@Value("${twitter.ciudadanos}")
	private String ciudadanosTwitter;

	@Value("${twitter.podemos}")
	private String podemosTwitter;

	@Value("${twitter.pp}")
	private String ppTwitter;

	@Value("${twitter.psoe}")
	private String psoeTwitter;

	@Bean
	public DirectChannel requestChannel() {
		return new DirectChannel();
	}

	@Bean
	public IntegrationFlow sendTweet() {
		return IntegrationFlows.from(requestChannel())
				.filter(object -> object instanceof Tweet)
				.transform(transformToPoliticalTweet())
				.split(PoliticalTweet.class,  splitPoliticalTweets())
				.handle("twitterStreamService", "sendTweet")
				.get();
	}

	// Transforms the specified Tweet to a new TargetedTweet including the queries contained in the Tweet
	private GenericTransformer<Tweet, PoliticalTweet> transformToPoliticalTweet(){	
		return tweet -> {
			PoliticalTweet polTweet = new PoliticalTweet();
			polTweet.setTweet(tweet);
			return polTweet;
		};
	}

	private Function<PoliticalTweet, ?> splitPoliticalTweets(){
		return polTweet ->{  
			List<PoliticalTweet> list = new ArrayList<PoliticalTweet>();
			String text = polTweet.getTweet().getText().toLowerCase();

			PoliticalTweet tweet;
			if(text.contains(podemosTwitter)){
				tweet = new PoliticalTweet();
				tweet.setPoliticalParty("podemos");
				tweet.setTweet(polTweet.getTweet());
				list.add(tweet);
			}

			if(text.contains(ppTwitter)){
				tweet = new PoliticalTweet();
				tweet.setPoliticalParty("pp");
				tweet.setTweet(polTweet.getTweet());
				list.add(tweet);
			}

			if(text.contains(psoeTwitter)){
				tweet = new PoliticalTweet();
				tweet.setPoliticalParty("psoe");
				tweet.setTweet(polTweet.getTweet());
				list.add(tweet);
			}

			if(text.contains(ciudadanosTwitter)){
				tweet = new PoliticalTweet();
				tweet.setPoliticalParty("ciudadanos");
				tweet.setTweet(polTweet.getTweet());
				list.add(tweet);
			}
			return list;
		};
	}

}

@MessagingGateway(name = "integrationStreamListener", defaultRequestChannel = "requestChannel")
interface MyStreamListener extends StreamListener {

}
