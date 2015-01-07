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

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.Toast;

import com.liferay.mobile.android.oauth.OAuthCallback;
import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.util.Validator;
import com.liferay.mobile.sample.R;
import com.liferay.mobile.sample.view.OAuthWebView;

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

		_config = new OAuthConfig(server, consumerKey, consumerSecret);

		Button login = (Button)findViewById(R.id.login);
		_webView = (OAuthWebView)findViewById(R.id.webView);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				_webView.start(_config, MainActivity.this);
			}

		});
	}

	@Override
	public void onCallbackURL(Uri callbackURL) {
		_webView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onFailure(Exception exception) {
		Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSuccess() {
		StringBuilder sb = new StringBuilder();
		sb.append("Success!");

		sb.append("\nToken: ");
		sb.append(_config.getToken());

		sb.append("\nToken Secret: ");
		sb.append(_config.getTokenSecret());

		Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		_webView.finish();
	}

	private OAuthConfig _config;
	private OAuthWebView _webView;

}