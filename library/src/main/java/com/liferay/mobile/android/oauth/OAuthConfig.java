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

package com.liferay.mobile.android.oauth;

import android.net.Uri;

import java.io.Serializable;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

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

		this(server, consumerKey, consumerSecret, CALLBACK_URL);
	}

	public OAuthConfig(
		String server, String consumerKey, String consumerSecret,
		String callbackURL) {

		_server = server;
		_consumerKey = consumerKey;
		_consumerSecret = consumerSecret;
		_callbackURL = callbackURL;
	}

	public String getCallbackURL() {
		return _callbackURL;
	}

	public OAuthConsumer getConsumer() {
		if (_consumer == null) {
			_consumer = new CommonsHttpOAuthConsumer(
				_consumerKey, _consumerSecret);
		}

		return _consumer;
	}

	public String getConsumerKey() {
		return _consumerKey;
	}

	public String getConsumerSecret() {
		return _consumerSecret;
	}

	public OAuthProvider getProvider() {
		if (_provider == null) {
			String requestURL = getRequestURL();
			String accessURL = getAccessURL();
			String authorizeURL = getAuthorizeURL();

			_provider = new CommonsHttpOAuthProvider(
				requestURL, accessURL, authorizeURL);
		}

		return _provider;
	}

	public String getToken() {
		if (_consumer == null) {
			return null;
		}

		return _consumer.getToken();
	}

	public String getTokenSecret() {
		if (_consumer == null) {
			return null;
		}

		return _consumer.getTokenSecret();
	}

	public String getVerifier() {
		return _verifier;
	}

	public void setVerifier(Uri callbackURL) {
		_verifier = callbackURL.getQueryParameter("oauth_verifier");
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
	private OAuthConsumer _consumer;
	private String _consumerKey;
	private String _consumerSecret;
	private OAuthProvider _provider;
	private String _server;
	private String _verifier;

}