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

package com.liferay.mobile.android.oauth.view;

import android.content.Context;

import android.net.Uri;

import android.os.AsyncTask;

import android.util.AttributeSet;

import android.view.View;

import android.webkit.WebView;

import com.liferay.mobile.android.oauth.OAuthCallback;
import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.oauth.bus.BusUtil;
import com.liferay.mobile.android.oauth.task.AccessTokenAsyncTask;
import com.liferay.mobile.android.oauth.task.RequestTokenAsyncTask;

import com.squareup.otto.Subscribe;

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

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

		BusUtil.register(this);
	}

	@Subscribe
	public void onAuthorizeURL(String authorizeURL) {
		loadUrl(authorizeURL);
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		BusUtil.unregister(this);
	}

	@Subscribe
	public void onFailure(Exception exception) {
		_callback.onFailure(exception);
	}

	@Subscribe
	public void onSuccess(OAuthConfig config) {
		_callback.onSuccess(config);
	}

	public void start(OAuthConfig config, OAuthCallback callback) {
		_config = config;
		_callback = callback;

		if (elementIdButtonGrantAccess == null) {
			elementIdButtonGrantAccess = DEFAUL_ELEMENT_ID_BUTTON_GRANT_ACCESS;
		}

		getSettings().setJavaScriptEnabled(true);
		setWebViewClient(new OAuthWebClient(_config.getCallbackURL()));

		AsyncTask task = new RequestTokenAsyncTask(_config);
		task.execute();
	}

	public boolean allowAutomatically;
	public String elementIdButtonGrantAccess;

	protected void onCallbackURL(Uri callbackURL) {
		_config.setVerifier(callbackURL);
		_callback.onCallbackURL(callbackURL);

		AccessTokenAsyncTask task = new AccessTokenAsyncTask(_config);
		task.execute();
	}


	protected void onPreLoadGrantAccessPage() {
		if (allowAutomatically) {
			setVisibility(View.INVISIBLE);
			_callback.onGrantedAccess();
		}
	}

	protected static final String DEFAUL_ELEMENT_ID_BUTTON_GRANT_ACCESS =
	"_3_WAR_oauthportlet_fm";

	protected static final String OAUTH_TOKEN = "oauthportlet_oauth_token";

	private OAuthCallback _callback;
	private OAuthConfig _config;

}