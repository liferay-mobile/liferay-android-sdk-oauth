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

import com.liferay.mobile.android.http.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Map;

import oauth.signpost.http.HttpRequest;

/**
 * @author Javier Gamarra
 */
public class HttpRequestAdapter implements HttpRequest {

	public HttpRequestAdapter(Request request) {
		this.request = request;
	}

	@Override
	public Map<String, String> getAllHeaders() {
		return request.getHeaders();
	}

	@Override
	public String getContentType() {
		if (request.getBody() == null) {
			return null;
		}

		return "application/json";
	}

	@Override
	public String getHeader(String name) {
		return request.getHeaders().get(name);
	}

	@Override
	public InputStream getMessagePayload() throws IOException {
		if (request.getBody() == null) {
			return null;
		}

		return new ByteArrayInputStream(((String)request.getBody()).getBytes());
	}

	@Override
	public String getMethod() {
		return String.valueOf(request.getMethod());
	}

	@Override
	public String getRequestUrl() {
		return request.getURL();
	}

	@Override
	public void setHeader(String name, String value) {
		request.getHeaders().put(name, value);
	}

	@Override
	public void setRequestUrl(String url) {
		request.setURL(url);
	}

	@Override
	public Object unwrap() {
		return request;
	}

	protected Request request;

}