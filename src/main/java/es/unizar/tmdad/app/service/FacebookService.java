package es.unizar.tmdad.app.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import facebook4j.Facebook;

public abstract class FacebookService {
	protected static final int MAXIMUM_POSTS = 25;

	@Value("${facebook.ciudadanos}")
	protected String ciudadanosFacebook;

	@Value("${facebook.podemos}")
	protected String podemosFacebook;

	@Value("${facebook.pp}")
	protected String ppFacebook;

	@Value("${facebook.psoe}")
	protected String psoeFacebook;
	
	@Autowired
	protected FacebookTemplate facebookTemplate;
	
	@Autowired
	protected Facebook facebook;
	
	protected static Date lastPost;
}
