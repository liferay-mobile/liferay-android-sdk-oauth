package com.liferay.mobile.android.oauth.signpost.adapter;

import com.liferay.mobile.android.http.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class HttpRequestAdapter implements oauth.signpost.http.HttpRequest {

	public HttpRequestAdapter(Request request) {
		this.request = request;
	}

	public String getMethod() {
		return String.valueOf(request.getMethod());
	}

	public String getRequestUrl() {
		return request.getURL();
	}

	public void setRequestUrl(String url) {
		request.setURL(url);
	}

	public String getHeader(String name) {
		String header = request.getHeaders().get(name);
		if (header == null) {
			return null;
		}
		return header;
	}

	public void setHeader(String name, String value) {
		request.getHeaders().put(name, value);
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

	public InputStream getMessagePayload() throws IOException {
		if (request.getBody() == null) {
			return null;
		}
		return new ByteArrayInputStream(((String) request.getBody()).getBytes());
	}

	public Object unwrap() {
		return request;
	}

	private Request request;

}
