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

package com.liferay.mobile.android.oauth.signpost;

import com.liferay.mobile.android.http.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Javier Gamarra
 */
public class HttpResponseAdapter implements oauth.signpost.http.HttpResponse {

	public HttpResponseAdapter(Response response) {
		this.response = response;
	}

	@Override
	public InputStream getContent() throws IOException {
		try {
			return response.getBodyAsStream();
		}
		catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getReasonPhrase() throws Exception {
		return null;
	}

	@Override
	public int getStatusCode() throws IOException {
		return response.getStatusCode();
	}

	@Override
	public Object unwrap() {
		return response;
	}

	protected Response response;

}