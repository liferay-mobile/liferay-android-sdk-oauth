package com.liferay.mobile.android.oauth.signpost.adapter;

import com.liferay.mobile.android.http.Response;

import java.io.IOException;
import java.io.InputStream;

public class HttpResponseAdapter implements oauth.signpost.http.HttpResponse {

	public HttpResponseAdapter(Response response) {
		this.response = response;
	}

	public InputStream getContent() throws IOException {
		try {
			return response.getBodyAsStream();
		}
		catch (Exception e) {
			throw new IOException(e);
		}
	}

	public int getStatusCode() throws IOException {
		return response.getStatusCode();
	}

	public String getReasonPhrase() throws Exception {
		return null;
	}

	public Object unwrap() {
		return response;
	}

	private Response response;

}