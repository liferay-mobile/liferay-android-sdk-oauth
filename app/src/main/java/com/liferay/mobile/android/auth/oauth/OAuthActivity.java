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

import android.app.Activity;

import android.content.Intent;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;

import com.liferay.mobile.android.task.oauth.AccessTokenAsyncTask;
import com.liferay.mobile.android.task.oauth.RequestTokenAsyncTask;

/**
 * @author Bruno Farache
 */
public class OAuthActivity extends Activity {

	public static final String EXTRA_OAUTH_CONFIG = "EXTRA_OAUTH_CONFIG";

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

		AsyncTask task = new RequestTokenAsyncTask(this, _config);
		task.execute();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Uri uri = intent.getData();

		String verifier = uri.getQueryParameter("oauth_verifier");
		_config.setVerifier(verifier);

		AccessTokenAsyncTask task = new AccessTokenAsyncTask(_config);
		task.execute();
	}

	private OAuthConfig _config;

}