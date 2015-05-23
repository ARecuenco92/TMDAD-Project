package es.unizar.tmdad.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

@Configuration
public class FacebookConfigTemplate {

	@Value("${facebook.appId}")
	private String appId;

	@Value("${facebook.appSecret}")
	private String appSecret;

	@Value("${facebook.accesToken}")
	private String accesToken;
	
	@Bean
    public FacebookTemplate facebookTemplate() {
		FacebookTemplate facebookOperations = new FacebookTemplate(accesToken);
        return facebookOperations;
    }
}
