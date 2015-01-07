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

package com.liferay.mobile.android.oauth.test;

import android.content.Context;

import android.test.AndroidTestCase;

import com.liferay.mobile.android.auth.Authentication;
import com.liferay.mobile.android.oauth.OAuth;
import com.liferay.mobile.android.oauth.R;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;
import com.liferay.mobile.android.util.Validator;
import com.liferay.mobile.android.v62.group.GroupService;

import org.json.JSONArray;

/**
 * @author Bruno Farache
 */
public class OAuthTest extends AndroidTestCase {

	public void testGetUserSites() throws Exception {
		Context context = getContext();

		String server = context.getString(R.string.oauth_server);
		String consumerKey = context.getString(R.string.oauth_consumer_key);
		String consumerSecret = context.getString(
			R.string.oauth_consumer_secret);

		String token = context.getString(R.string.oauth_token);
		String tokenSecret = context.getString(R.string.oauth_token_secret);

		if (Validator.isNull(server) || Validator.isNull(consumerKey) ||
			Validator.isNull(consumerSecret) || Validator.isNull(token) ||
			Validator.isNull(tokenSecret)) {

			fail("oauth.xml is not properly configured.");

			return;
		}

		Authentication auth = new OAuth(
			consumerKey, consumerSecret, token, tokenSecret);

		Session session = new SessionImpl(server, auth);
		GroupService service = new GroupService(session);

		JSONArray sites = service.getUserSites();

		assertNotNull(sites);
		assertTrue(sites.length() > 0);
	}

}