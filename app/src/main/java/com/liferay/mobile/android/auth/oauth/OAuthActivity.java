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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;

import com.liferay.mobile.android.task.oauth.AccessTokenAsyncTask;
import com.liferay.mobile.android.task.oauth.RequestTokenAsyncTask;

/**
 * @author Bruno Farache
 */
public class OAuthActivity extends Activity {

	public static final String ACTION_TOKEN = "ACTION_TOKEN";

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

		registerReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
		manager.unregisterReceiver(_receiver);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Uri uri = intent.getData();

		String verifier = uri.getQueryParameter("oauth_verifier");
		_config.setVerifier(verifier);

		AccessTokenAsyncTask task = new AccessTokenAsyncTask(this, _config);
		task.execute();
	}

	protected void registerReceiver() {
		_receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				if (ACTION_TOKEN.equals(action)) {
					OAuthActivity.this.setResult();
				}
			}

		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_TOKEN);

		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
		manager.registerReceiver(_receiver, filter);
	}

	protected void setResult() {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_OAUTH_CONFIG, _config);
		setResult(RESULT_OK, intent);
		finish();
	}

	private OAuthConfig _config;
	private BroadcastReceiver _receiver;

}