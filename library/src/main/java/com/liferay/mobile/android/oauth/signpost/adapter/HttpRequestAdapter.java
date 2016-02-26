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

package com.liferay.mobile.android.oauth.signpost.adapter;

import com.liferay.mobile.android.http.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Map;

/**
 * @author Javier Gamarra
 */
public class HttpRequestAdapter implements oauth.signpost.http.HttpRequest {

	public HttpRequestAdapter(Request request) {
		this.request = request;
	}

	public Map<String, String> getAllHeaders() {
		return request.getHeaders();
	}

	public String getContentType() {
		if (request.getBody() == null) {
			return null;
		}

		return "application/json";
	}

	public String getHeader(String name) {
		String header = request.getHeaders().get(name);

		if (header == null) {
			return null;
		}

		return header;
	}

	public InputStream getMessagePayload() throws IOException {
		if (request.getBody() == null) {
			return null;
		}

		return new ByteArrayInputStream(((String)request.getBody()).getBytes());
	}

	public String getMethod() {
		return String.valueOf(request.getMethod());
	}

	public String getRequestUrl() {
		return request.getURL();
	}

	public void setHeader(String name, String value) {
		request.getHeaders().put(name, value);
	}

	public void setRequestUrl(String url) {
		request.setURL(url);
	}

	public Object unwrap() {
		return request;
	}

	private Request request;

}