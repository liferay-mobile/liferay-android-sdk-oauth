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

package com.liferay.mobile.sample.activity;

import android.app.Activity;

import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;

import android.webkit.WebView;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liferay.mobile.android.callback.typed.JSONArrayCallback;
import com.liferay.mobile.android.oauth.OAuth;
import com.liferay.mobile.android.oauth.OAuthCallback;
import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.oauth.view.OAuthWebView;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;
import com.liferay.mobile.android.util.Validator;
import com.liferay.mobile.android.v62.group.GroupService;
import com.liferay.mobile.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class MainActivity extends Activity implements OAuthCallback {

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);

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

		final OAuthConfig config = new OAuthConfig(
			server, consumerKey, consumerSecret);

		Button login = (Button)findViewById(R.id.login);
		final OAuthWebView webView = (OAuthWebView)findViewById(R.id.webView);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				webView.loadUrl("about:blank");
				webView.setVisibility(View.VISIBLE);
				webView.start(config, MainActivity.this);
			}

		});

		_progressBar = (ProgressBar)findViewById(R.id.progressBar);
	}

	@Override
	public void onFailure(Exception exception) {
		Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();

		_progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onLoadPage(Page page, WebView webView, String URL) {
		webView.setVisibility(View.INVISIBLE);

		if (page == Page.ASK_PERMISSION) {
			_progressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onSuccess(OAuthConfig config) {
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
		JSONArrayCallback callback = _getPrintSitesCallback();

		Session session = new SessionImpl(server, auth, callback);

		GroupService service = new GroupService(session);

		try {
			service.getUserSites();
		}
		catch (Exception e) {
			Log.e(_TAG, "Error during service call", e);
		}

		_progressBar.setVisibility(View.INVISIBLE);
	}

	private JSONArrayCallback _getPrintSitesCallback() {
		final TextView result = (TextView)findViewById(R.id.result);

		return new JSONArrayCallback() {

			@Override
			public void onSuccess(JSONArray sites) {
				try {
					StringBuilder sb = new StringBuilder();

					for (int i = 0; i < sites.length(); i++) {
						JSONObject site = sites.getJSONObject(i);
						sb.append(site.getString("name"));
						sb.append("\n");
					}

					result.setText(sb.toString());
				}
				catch (JSONException je) {
					Log.e(_TAG, "Could not parse JSON", je);
				}
			}

			@Override
			public void onFailure(Exception exception) {
				result.setText(exception.getMessage());
			}

		};
	}

	private static final String _TAG = MainActivity.class.getSimpleName();

	private ProgressBar _progressBar;

}