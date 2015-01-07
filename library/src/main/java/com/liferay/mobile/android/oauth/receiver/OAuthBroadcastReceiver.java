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

package com.liferay.mobile.android.oauth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.support.v4.content.LocalBroadcastManager;

import com.liferay.mobile.android.oauth.OAuthCallback;
import com.liferay.mobile.android.oauth.activity.OAuthActivity;

/**
 * @author Bruno Farache
 */
public class OAuthBroadcastReceiver extends BroadcastReceiver {

	public static final String ACTION_AUTHORIZE_URL = "ACTION_AUTHORIZE_URL";

	public static final String ACTION_FAILURE = "ACTION_FAILURE";

	public static final String ACTION_SUCCESS = "ACTION_SUCCESS";

	public static final String EXTRA_AUTHORIZE_URL = "EXTRA_AUTHORIZE_URL";

	public static final String EXTRA_EXCEPTION = "EXTRA_EXCEPTION";

	public OAuthBroadcastReceiver(
		Context context, OAuthCallback callback,
		OnAuthorizeURLListener listener) {

		_context = context.getApplicationContext();
		_callback = callback;
		_listener = listener;
	}

	public OAuthBroadcastReceiver(OAuthActivity activity) {
		this(activity, activity, activity);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (ACTION_AUTHORIZE_URL.equals(action)) {
			String authorizeURL = intent.getStringExtra(EXTRA_AUTHORIZE_URL);
			_listener.onAuthorizeURL(authorizeURL);
		}
		else if (ACTION_FAILURE.equals(action)) {
			Exception exception = (Exception)intent.getSerializableExtra(
				EXTRA_EXCEPTION);

			_callback.onFailure(exception);
		}
		else if (ACTION_SUCCESS.equals(action)) {
			_callback.onSuccess();
		}

		if (ACTION_FAILURE.equals(action) || ACTION_SUCCESS.equals(action)) {
			unregister();
		}
	}

	public void register() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_AUTHORIZE_URL);
		filter.addAction(ACTION_FAILURE);
		filter.addAction(ACTION_SUCCESS);

		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(
			_context);

		manager.registerReceiver(this, filter);
	}

	public void unregister() {
		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(
			_context);

		manager.unregisterReceiver(this);
	}

	private OAuthCallback _callback;
	private Context _context;
	private OnAuthorizeURLListener _listener;

}