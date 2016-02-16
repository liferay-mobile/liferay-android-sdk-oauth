package com.liferay.mobile.android.oauth.signpost.adapter;

import com.liferay.mobile.android.http.Method;
import com.liferay.mobile.android.http.Request;
import com.liferay.mobile.android.http.Response;
import com.liferay.mobile.android.http.client.HttpClient;
import com.liferay.mobile.android.http.client.OkHttpClientImpl;

import java.util.HashMap;

import oauth.signpost.AbstractOAuthProvider;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

/**
 * @author Javier Gamarra
 */
public class RequestOAuthProvider extends AbstractOAuthProvider {

	public RequestOAuthProvider(String requestTokenEndpointUrl, String accessTokenEndpointUrl,
								String authorizationWebsiteUrl) {
		super(requestTokenEndpointUrl, accessTokenEndpointUrl, authorizationWebsiteUrl);
		this.httpClient = new OkHttpClientImpl();
	}

	@Override
	protected HttpRequest createRequest(String endpointUrl) throws Exception {
		Request request = new Request(
			Method.GET, new HashMap<String, String>(), endpointUrl, null, 15000);
		return new HttpRequestAdapter(request);
	}

	@Override
	protected HttpResponse sendRequest(HttpRequest request) throws Exception {
		Response response = httpClient.send((Request) request.unwrap());
		return new HttpResponseAdapter(response);
	}

	private transient HttpClient httpClient;

}
