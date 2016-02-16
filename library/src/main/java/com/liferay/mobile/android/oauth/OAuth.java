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

import com.liferay.mobile.android.auth.Authentication;
import com.liferay.mobile.android.http.Request;

import oauth.signpost.OAuthConsumer;

/**
 * @author Bruno Farache
 */
public class OAuth implements Authentication {

	public OAuth(OAuthConfig config) {
		_config = config;
	}

	public OAuth(
		String consumerKey, String consumerSecret, String token,
		String tokenSecret) {

		this(new OAuthConfig(consumerKey, consumerSecret, token, tokenSecret));
	}

	@Override
	public void authenticate(Request request) throws Exception {
		OAuthConsumer consumer = _config.getConsumer();

		consumer.setTokenWithSecret(
			_config.getToken(), _config.getTokenSecret());

		consumer.sign(request);
	}

	public OAuthConfig getConfig() {
		return _config;
	}

	private OAuthConfig _config;

}