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

package com.liferay.mobile.android.oauth.activity;

import android.app.Activity;

import android.content.Intent;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;

import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.oauth.bus.BusUtil;
import com.liferay.mobile.android.oauth.task.AccessTokenAsyncTask;
import com.liferay.mobile.android.oauth.task.RequestTokenAsyncTask;

import com.squareup.otto.Subscribe;

/**
 * @author Bruno Farache
 */
public class OAuthActivity extends Activity {

	public static final String EXTRA_EXCEPTION = "EXTRA_EXCEPTION";

	public static final String EXTRA_OAUTH_CONFIG = "EXTRA_OAUTH_CONFIG";

	@Subscribe
	public void onAuthorizeURL(String authorizeURL) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizeURL));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);

		Intent intent = getIntent();

		OAuthConfig config = (OAuthConfig)intent.getSerializableExtra(
			EXTRA_OAUTH_CONFIG);

		if (config == null) {
			finish();

			return;
		}

		_config = config;

		BusUtil.register(this);

		AsyncTask task = new RequestTokenAsyncTask(_config);
		task.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		BusUtil.unregister(this);
	}

	@Subscribe
	public void onFailure(Exception exception) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXCEPTION, exception);

		intent.setAction(EXTRA_EXCEPTION);
		sendBroadcast(intent);

		setResult(RESULT_CANCELED, intent);

		finish();
	}

	@Override
	public void onNewIntent(Intent intent) {
		Uri callbackURL = intent.getData();
		_config.setVerifier(callbackURL);

		AccessTokenAsyncTask task = new AccessTokenAsyncTask(_config);
		task.execute();
	}

	@Subscribe
	public void onSuccess(OAuthConfig config) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_OAUTH_CONFIG, config);

		intent.setAction(EXTRA_OAUTH_CONFIG);
		sendBroadcast(intent);

		setResult(RESULT_OK, intent);

		finish();
	}

	private OAuthConfig _config;

}