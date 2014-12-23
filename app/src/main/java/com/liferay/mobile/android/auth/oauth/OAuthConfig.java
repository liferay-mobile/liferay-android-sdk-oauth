/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.android.auth.oauth;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

/**
 * @author Bruno Farache
 */
public class OAuthConfig {

	public static final String ACCESS_URL = "/c/portal/oauth/access_token";

	public static final String AUTHORIZE_URL = "/c/portal/oauth/authorize";

	public static final String REQUEST_URL = "/c/portal/oauth/request_token";

	public OAuthConfig(
		String server, String consumerKey, String consumerSecret) {

		this.server = server;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public String getAccessURL() {
		StringBuilder sb = new StringBuilder(server);
		sb.append(ACCESS_URL);

		return sb.toString();
	}

	public String getAuthorizeURL() {
		StringBuilder sb = new StringBuilder(server);
		sb.append(AUTHORIZE_URL);

		return sb.toString();
	}

	public String getCallbackURL() {
		return "liferay://callback";
	}

	public OAuthConsumer getConsumer() {
		if (consumer == null) {
			consumer = new CommonsHttpOAuthConsumer(
				getConsumerKey(), getConsumerSecret());
		}

		return consumer;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public OAuthProvider getProvider() {
		if (provider == null) {
			provider = new CommonsHttpOAuthProvider(
				getRequestURL(), getAccessURL(), getAuthorizeURL());
		}

		return provider;
	}

	public String getRequestURL() {
		StringBuilder sb = new StringBuilder(server);
		sb.append(REQUEST_URL);

		return sb.toString();
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	} protected String consumerKey;

	protected OAuthConsumer consumer;
	protected String consumerSecret;
	protected OAuthProvider provider;
	protected String server;
	protected String verifier;

}