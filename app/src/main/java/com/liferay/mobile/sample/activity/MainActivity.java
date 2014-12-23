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

import com.liferay.mobile.android.auth.oauth.OAuthActivity;
import com.liferay.mobile.android.auth.oauth.OAuthConfig;
import com.liferay.mobile.sample.R;

/**
 * @author Bruno Farache
 */
public class MainActivity extends Activity {

	public static int AUTHENTICATE_REQUEST_CODE = 1;

	@Override
	public void onActivityResult(int request, int result, Intent intent) {
		if ((request == AUTHENTICATE_REQUEST_CODE) && (result == RESULT_OK)) {
			OAuthConfig config = (OAuthConfig)intent.getSerializableExtra(
				OAuthActivity.EXTRA_OAUTH_CONFIG);

			System.out.println("Consumer key: " + config.getConsumerKey());
			System.out.println(
				"Consumer secret: " + config.getConsumerSecret());

			System.out.println("Token: " + config.getToken());
			System.out.println("Token secret: " + config.getTokenSecret());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		OAuthConfig config = new OAuthConfig(
			"http://192.168.56.1:8080", "abb49e76-aafb-405a-8619-76be986e6752",
			"525041f5b3f8f248643c31dd384637ed");

		Intent intent = new Intent(this, OAuthActivity.class);
		intent.putExtra(OAuthActivity.EXTRA_OAUTH_CONFIG, config);

		startActivityForResult(intent, AUTHENTICATE_REQUEST_CODE);
	}

}