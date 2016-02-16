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

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.AbstractOAuthProvider;

/**
 * @author Bruno Farache
 */
public class AccessTokenAsyncTask extends AsyncTask<Object, Void, Void> {

	public AccessTokenAsyncTask(OAuthConfig config) {
		_config = config;
	}

	@Override
	protected Void doInBackground(Object... params) {
		try {
			AbstractOAuthProvider provider = _config.getProvider();
			AbstractOAuthConsumer consumer = _config.getConsumer();
			String verifier = _config.getVerifier();

			provider.retrieveAccessToken(consumer, verifier);

			_config.setToken(consumer.getToken());
			_config.setTokenSecret(consumer.getTokenSecret());
		}
		catch (Exception e) {
			Log.e(_TAG, "Could not retrieve access token.", e);
			_exception = e;
			cancel(true);
		}

		return null;
	}

	@Override
	protected void onCancelled() {
		BusUtil.post(_exception);
	}

	@Override
	protected void onPostExecute(Void result) {
		BusUtil.post(_config);
	}

	private static final String _TAG =
		AccessTokenAsyncTask.class.getSimpleName();

	private OAuthConfig _config;
	private Exception _exception;

}