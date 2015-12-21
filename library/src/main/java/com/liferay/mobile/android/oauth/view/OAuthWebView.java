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
import android.os.Build;

import android.util.AttributeSet;

import android.webkit.CookieManager;
import android.webkit.WebView;

import com.liferay.mobile.android.oauth.OAuthCallback;
import com.liferay.mobile.android.oauth.OAuthCallback.Page;
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

	public String getDenyURL() {
		return denyURL;
	}

	public boolean isGrantAutomatically() {
		return grantAutomatically;
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
		callback.onFailure(exception);
	}

	@Subscribe
	public void onSuccess(OAuthConfig config) {
		callback.onSuccess(config);
	}

	public void start(OAuthConfig config, OAuthCallback callback) {
		start(config, callback, null, true);
	}

	public void start(
		OAuthConfig config, OAuthCallback callback, String denyURL) {

		start(config, callback, denyURL, false);
	}

	protected void onCallbackURL(Uri callbackURL) {
		config.setVerifier(callbackURL);
		onLoadPage(OAuthCallback.Page.GRANTED);

		AccessTokenAsyncTask task = new AccessTokenAsyncTask(config);
		task.execute();
	}

	protected void onLoadPage(Page page) {
		callback.onLoadPage(page, this, getUrl());
	}

	@SuppressWarnings("deprecation")
	protected void removeAllCookies() {
		CookieManager manager = CookieManager.getInstance();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			manager.removeAllCookies(null);
		}
		else {
			manager.removeAllCookie();
		}
	}

	protected void start(
		OAuthConfig config, OAuthCallback callback, String denyURL,
		boolean grantAutomatically) {

		removeAllCookies();

		this.config = config;
		this.callback = callback;
		this.denyURL = denyURL;
		this.grantAutomatically = grantAutomatically;

		getSettings().setJavaScriptEnabled(true);
		setWebViewClient(new OAuthWebClient(this.config.getCallbackURL()));

		AsyncTask task = new RequestTokenAsyncTask(this.config);
		task.execute();
	}

	protected static final String OAUTH_TOKEN = "oauthportlet_oauth_token";

	protected OAuthCallback callback;
	protected OAuthConfig config;
	protected String denyURL;
	protected boolean grantAutomatically;

}