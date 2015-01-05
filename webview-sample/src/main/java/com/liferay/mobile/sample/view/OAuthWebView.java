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

package com.liferay.mobile.sample.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.Uri;

import android.os.AsyncTask;

import android.support.v4.content.LocalBroadcastManager;

import android.util.AttributeSet;

import android.webkit.WebView;

import android.widget.Toast;

import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.oauth.activity.OAuthActivity;
import com.liferay.mobile.android.oauth.task.AccessTokenAsyncTask;
import com.liferay.mobile.android.oauth.task.RequestTokenAsyncTask;

/**
 * @author Bruno Farache
 */
public class OAuthWebView extends WebView {

	public OAuthWebView(Context context) {
		this(context, null);
	}

	public OAuthWebView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	public void start(OAuthConfig config) {
		_config = config;

		getSettings().setJavaScriptEnabled(true);
		setWebViewClient(new OAuthWebClient(_config.getCallbackURL()));

		registerReceiver();

		AsyncTask task = new RequestTokenAsyncTask(getContext(), _config);
		task.execute();
	}

	protected void onCallbackURL(Uri uri) {
		String verifier = uri.getQueryParameter("oauth_verifier");
		_config.setVerifier(verifier);

		AccessTokenAsyncTask task = new AccessTokenAsyncTask(
			getContext(), _config);

		task.execute();
	}

	protected void onFailure(Exception exception) {
		Toast.makeText(
			getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
	}

	protected void onOpenBrowser(String URL) {
		loadUrl(URL);
	}

	protected void onSuccess() {
		StringBuilder sb = new StringBuilder();
		sb.append("Success!");

		sb.append("\nToken: ");
		sb.append(_config.getToken());

		sb.append("\nToken Secret: ");
		sb.append(_config.getTokenSecret());

		Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_LONG).show();
	}

	protected void registerReceiver() {
		_receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				if (OAuthActivity.ACTION_FAILURE.equals(action)) {
					Exception exception =
						(Exception)intent.getSerializableExtra(
							OAuthActivity.EXTRA_EXCEPTION);

					OAuthWebView.this.onFailure(exception);
				}
				else if (OAuthActivity.ACTION_OPEN_BROWSER.equals(action)) {
					String URL = intent.getStringExtra(OAuthActivity.EXTRA_URL);

					OAuthWebView.this.onOpenBrowser(URL);
				}
				else if (OAuthActivity.ACTION_SUCCESS.equals(action)) {
					OAuthWebView.this.onSuccess();
				}
			}

		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(OAuthActivity.ACTION_FAILURE);
		filter.addAction(OAuthActivity.ACTION_OPEN_BROWSER);
		filter.addAction(OAuthActivity.ACTION_SUCCESS);

		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(
			getContext());

		manager.registerReceiver(_receiver, filter);
	}

	private OAuthConfig _config;
	private BroadcastReceiver _receiver;

}