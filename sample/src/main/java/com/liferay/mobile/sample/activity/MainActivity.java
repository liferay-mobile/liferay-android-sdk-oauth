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

package com.liferay.mobile.sample.activity;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;

import com.liferay.mobile.android.oauth.OAuth;
import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.oauth.activity.OAuthActivity;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;
import com.liferay.mobile.android.task.callback.AsyncTaskCallback;
import com.liferay.mobile.android.task.callback.typed.JSONArrayAsyncTaskCallback;
import com.liferay.mobile.android.util.Validator;
import com.liferay.mobile.android.v62.group.GroupService;
import com.liferay.mobile.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class MainActivity extends Activity {

	public static final int AUTHENTICATE_REQUEST_CODE = 1;

	@Override
	public void onActivityResult(int request, int result, Intent intent) {
		if ((request == AUTHENTICATE_REQUEST_CODE) && (result == RESULT_OK)) {
			OAuthConfig config = (OAuthConfig)intent.getSerializableExtra(
				OAuthActivity.EXTRA_OAUTH_CONFIG);

			String consumerKey = config.getConsumerKey();
			String consumerSecret = config.getConsumerSecret();
			String token = config.getToken();
			String tokenSecret = config.getTokenSecret();

			Log.d(_TAG, "Consumer key: " + consumerKey);
			Log.d(_TAG, "Consumer secret: " + consumerSecret);
			Log.d(_TAG, "Token: " + token);
			Log.d(_TAG, "Token secret: " + tokenSecret);

			String server = getString(R.string.oauth_server);
			OAuth auth = new OAuth(config);
			AsyncTaskCallback callback = _getPrintSitesCallback();

			Session session = new SessionImpl(server, auth, callback);

			GroupService service = new GroupService(session);

			try {
				service.getUserSites();
			}
			catch (Exception e) {
				Log.e(_TAG, "Error during service call", e);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		String server = getString(R.string.oauth_server);
		String consumerKey = getString(R.string.oauth_consumer_key);
		String consumerSecret = getString(R.string.oauth_consumer_secret);

		if (Validator.isNull(server) || Validator.isNull(consumerKey) ||
			Validator.isNull(consumerSecret)) {

			Toast.makeText(
				this, "oauth.xml is not properly configured.",
				Toast.LENGTH_LONG).show();

			return;
		}

		OAuthConfig config = new OAuthConfig(
			server, consumerKey, consumerSecret);

		Intent intent = new Intent(this, OAuthActivity.class);
		intent.putExtra(OAuthActivity.EXTRA_OAUTH_CONFIG, config);

		startActivityForResult(intent, AUTHENTICATE_REQUEST_CODE);
	}

	private AsyncTaskCallback _getPrintSitesCallback() {
		final TextView textView = (TextView)findViewById(R.id.sites);

		return new JSONArrayAsyncTaskCallback() {

			@Override
			public void onSuccess(JSONArray sites) {
				try {
					StringBuilder sb = new StringBuilder();

					for (int i = 0; i < sites.length(); i++) {
						JSONObject site = sites.getJSONObject(i);
						sb.append(site.getString("name"));
						sb.append("\n");
					}

					textView.setText(sb.toString());
				}
				catch (JSONException je) {
					Log.e(_TAG, "Could not parse JSON", je);
				}
			}

			@Override
			public void onFailure(Exception exception) {
				textView.setText(exception.getMessage());
			}

		};
	}

	private static final String _TAG = MainActivity.class.getSimpleName();

}