package com.liferay.mobile.android.oauth.signpost.adapter;

import com.liferay.mobile.android.http.Request;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;

/**
 * @author Javier Gamarra
 */
public class RequestOAuthConsumer extends AbstractOAuthConsumer {

	public RequestOAuthConsumer(String consumerKey, String consumerSecret) {
		super(consumerKey, consumerSecret);
	}

	@Override
	protected HttpRequest wrap(Object request) {
		return new HttpRequestAdapter((Request) request);
	}

}
