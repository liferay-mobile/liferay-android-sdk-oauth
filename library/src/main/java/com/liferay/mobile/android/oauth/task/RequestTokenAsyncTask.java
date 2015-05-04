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

package com.liferay.mobile.android.oauth.task;

import android.os.AsyncTask;

import android.util.Log;

import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.oauth.bus.BusUtil;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

/**
 * @author Bruno Farache
 */
public class RequestTokenAsyncTask extends AsyncTask<Object, Void, String> {

	public RequestTokenAsyncTask(OAuthConfig config) {
		_config = config;
	}

	@Override
	protected String doInBackground(Object... params) {
		String authorizeURL = "";

		try {
			OAuthProvider provider = _config.getProvider();
			OAuthConsumer consumer = _config.getConsumer();
			String callbackURL = _config.getCallbackURL();

			authorizeURL = provider.retrieveRequestToken(consumer, callbackURL);
		}
		catch (Exception e) {
			Log.e(_TAG, "Could not retrieve request token.", e);
			_exception = e;
			cancel(true);
		}

		return authorizeURL;
	}

	@Override
	protected void onCancelled() {
		BusUtil.post(new Exception(_exception.getMessage()));
	}

	@Override
	protected void onPostExecute(String authorizeURL) {
		BusUtil.post(authorizeURL);
	}

	private static final String _TAG =
		RequestTokenAsyncTask.class.getSimpleName();

	private OAuthConfig _config;
	private Exception _exception;

}