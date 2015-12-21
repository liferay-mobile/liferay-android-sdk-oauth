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

import android.net.Uri;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.liferay.mobile.android.oauth.OAuthCallback.Page;
import com.liferay.mobile.android.util.Validator;

/**
 * @author Bruno Farache
 */
public class OAuthWebClient extends WebViewClient {

	public OAuthWebClient(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	@Override
	public void onPageFinished(WebView view, String URL) {
		OAuthWebView webView = (OAuthWebView)view;

		if (URL.contains(OAuthWebView.OAUTH_TOKEN) &&
			webView.isGrantAutomatically()) {

			String javascript = "javascript:(function(){" +
				"document.getElementById('" + OAUTH_PORTLET_FORM_ID +
				"').submit();})()";

			webView.loadUrl(javascript);

			return;
		}
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String URL) {
		OAuthWebView webView = (OAuthWebView)view;

		if (URL.contains(OAuthWebView.OAUTH_TOKEN)) {
			webView.onLoadPage(Page.ASK_PERMISSION);

			return false;
		}

		if (URL.startsWith(callbackURL)) {
			webView.onCallbackURL(Uri.parse(URL));

			return false;
		}

		String denyURL = webView.getDenyURL();

		if (Validator.isNotNull(denyURL) && URL.contains(denyURL)) {
			webView.onLoadPage(Page.DENIED);

			return true;
		}

		return super.shouldOverrideUrlLoading(view, URL);
	}

	protected static final String OAUTH_PORTLET_FORM_ID =
		"_3_WAR_oauthportlet_fm";

	protected String callbackURL;

}