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

package com.liferay.mobile.android.oauth.task;

import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;

import android.support.v4.content.LocalBroadcastManager;

import android.util.Log;

import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.oauth.activity.OAuthActivity;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

/**
 * @author Bruno Farache
 */
public class RequestTokenAsyncTask extends AsyncTask<Object, Void, String> {

	public RequestTokenAsyncTask(Context context, OAuthConfig config) {
		_context = context.getApplicationContext();
		_config = config;
	}

	@Override
	protected String doInBackground(Object... params) {
		String URL = "";

		try {
			OAuthProvider provider = _config.getProvider();
			OAuthConsumer consumer = _config.getConsumer();
			String callbackURL = _config.getCallbackURL();

			URL = provider.retrieveRequestToken(consumer, callbackURL);
		}
		catch (Exception e) {
			Log.e(_TAG, "Could not retrieve request token.", e);
			_exception = e;
			cancel(true);
		}

		return URL;
	}

	@Override
	protected void onCancelled() {
		Intent intent = new Intent(OAuthActivity.ACTION_FAILURE);
		intent.putExtra(OAuthActivity.EXTRA_EXCEPTION, _exception);
		_getLocalBroadcastManager().sendBroadcast(intent);
	}

	@Override
	protected void onPostExecute(String URL) {
		Intent intent = new Intent(OAuthActivity.ACTION_OPEN_BROWSER);
		intent.putExtra(OAuthActivity.EXTRA_URL, URL);
		_getLocalBroadcastManager().sendBroadcast(intent);
	}

	private LocalBroadcastManager _getLocalBroadcastManager() {
		return LocalBroadcastManager.getInstance(_context);
	}

	private static final String _TAG =
		RequestTokenAsyncTask.class.getSimpleName();

	private OAuthConfig _config;
	private Context _context;
	private Exception _exception;

}