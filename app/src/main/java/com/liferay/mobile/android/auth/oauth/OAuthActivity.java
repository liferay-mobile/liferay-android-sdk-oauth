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

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;

import com.liferay.mobile.android.task.oauth.AccessTokenAsyncTask;
import com.liferay.mobile.android.task.oauth.RequestTokenAsyncTask;

/**
 * @author Bruno Farache
 */
public class OAuthActivity extends Activity {

	public OAuthConfig getConfig() {
		if (_config == null) {
			_config = new OAuthConfig(
				"http://192.168.56.1:8080",
				"abb49e76-aafb-405a-8619-76be986e6752",
				"525041f5b3f8f248643c31dd384637ed");
		}

		return _config;
	}

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);

		Uri uri = getIntent().getData();

		if (uri == null) {
			AsyncTask task = new RequestTokenAsyncTask(this, getConfig());
			task.execute();

			return;
		}

		OAuthConfig config = getConfig();
		String verifier = uri.getQueryParameter("oauth_verifier");
		config.setVerifier(verifier);

		AccessTokenAsyncTask task = new AccessTokenAsyncTask(config);
		task.execute();
	}

	private static OAuthConfig _config;

}