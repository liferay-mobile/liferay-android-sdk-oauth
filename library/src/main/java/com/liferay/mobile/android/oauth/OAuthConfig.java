/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.mobile.android.oauth;

import android.net.Uri;

import com.liferay.mobile.android.oauth.signpost.RequestOAuthConsumer;
import com.liferay.mobile.android.oauth.signpost.RequestOAuthProvider;

import java.io.Serializable;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.AbstractOAuthProvider;

/**
 * @author Bruno Farache
 */
public class OAuthConfig implements Serializable {

	public static final String ACCESS_URL = "/c/portal/oauth/access_token";

	public static final String AUTHORIZE_URL = "/c/portal/oauth/authorize";

	public static final String CALLBACK_URL = "liferay://callback";

	public static final String REQUEST_URL = "/c/portal/oauth/request_token";

	public OAuthConfig(
		String server, String consumerKey, String consumerSecret) {

		this(server, consumerKey, consumerSecret, null, null, CALLBACK_URL);
	}

	public OAuthConfig(
		String consumerKey, String consumerSecret, String token,
		String tokenSecret) {

		this(null, consumerKey, consumerSecret, token, tokenSecret, null);
	}

	public String getCallbackURL() {
		return _callbackURL;
	}

	public AbstractOAuthConsumer getConsumer() {
		if (_consumer == null) {
			_consumer = new RequestOAuthConsumer(_consumerKey, _consumerSecret);
		}

		return _consumer;
	}

	public String getConsumerKey() {
		return _consumerKey;
	}

	public String getConsumerSecret() {
		return _consumerSecret;
	}

	public AbstractOAuthProvider getProvider() {
		if (_provider == null) {
			String requestURL = getRequestURL();
			String accessURL = getAccessURL();
			String authorizeURL = getAuthorizeURL();

			_provider = new RequestOAuthProvider(
				requestURL, accessURL, authorizeURL) {
			};
		}

		return _provider;
	}

	public String getToken() {
		return _token;
	}

	public String getTokenSecret() {
		return _tokenSecret;
	}

	public String getVerifier() {
		return _verifier;
	}

	public void setToken(String token) {
		_token = token;
	}

	public void setTokenSecret(String tokenSecret) {
		_tokenSecret = tokenSecret;
	}

	public void setVerifier(Uri callbackURL) {
		_verifier = callbackURL.getQueryParameter("oauth_verifier");
	}

	protected OAuthConfig(
		String server, String consumerKey, String consumerSecret, String token,
		String tokenSecret, String callbackURL) {

		_server = server;
		_consumerKey = consumerKey;
		_consumerSecret = consumerSecret;
		_token = token;
		_tokenSecret = tokenSecret;
		_callbackURL = callbackURL;
	}

	protected String getAccessURL() {
		StringBuilder sb = new StringBuilder(_server);
		sb.append(ACCESS_URL);

		return sb.toString();
	}

	protected String getAuthorizeURL() {
		StringBuilder sb = new StringBuilder(_server);
		sb.append(AUTHORIZE_URL);

		return sb.toString();
	}

	protected String getRequestURL() {
		StringBuilder sb = new StringBuilder(_server);
		sb.append(REQUEST_URL);

		return sb.toString();
	}

	private String _callbackURL;
	private AbstractOAuthConsumer _consumer;
	private String _consumerKey;
	private String _consumerSecret;
	private AbstractOAuthProvider _provider;
	private String _server;
	private String _token;
	private String _tokenSecret;
	private String _verifier;

}