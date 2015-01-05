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

import android.net.Uri;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Bruno Farache
 */
public class OAuthWebClient extends WebViewClient {

	public OAuthWebClient(String callbackURL) {
		_callbackURL = callbackURL;
	}

	@Override
	public void onPageFinished(WebView view, String URL) {
		OAuthWebView webView = (OAuthWebView)view;

		if (URL.startsWith(_callbackURL)) {
			webView.onCallbackURL(Uri.parse(URL));
		}
	}

	private String _callbackURL;

}