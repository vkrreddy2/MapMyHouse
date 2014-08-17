package com.dasari.android.maps.mapmyhouse.httpconnection;

import org.apache.http.params.HttpParams;

import android.content.Context;

public class HttpConnectionManager {

	private static HttpConnectionManager mInstance = null;
	private HttpGetAsynctask mHttpGetAsyncTask = null;
	private HttpPostAsyncTask mHttpPostAsyncTask = null;
	public static enum REQUEST_TYPE {
		GET, POST
	}
	private HttpConnectionManager() {

	}

	public static HttpConnectionManager getInstance() {
		if (mInstance == null) {
			mInstance = new HttpConnectionManager();
		}
		return mInstance;
	}

	public void makeRequest(Context context, String url, REQUEST_TYPE requestType,
			int requestID, IOResponseListener listener, HttpParams params) throws IllegalArgumentException{
		switch (requestType) {
			case GET :
					mHttpGetAsyncTask = new HttpGetAsynctask(context, requestID, listener);
					mHttpGetAsyncTask.execute(url);
				break;
			case POST:
					mHttpPostAsyncTask = new HttpPostAsyncTask(context, requestID, listener, params);
					mHttpPostAsyncTask.execute(url);
				break;
			default :
					throw new IllegalArgumentException("Request type method is not valid");
		}
	}

	public interface IOResponseListener {
		public void onResponseReceived(Object response, int requestID);
		public void onExceptionReceived(Exception ex);
		public void onNoInternetAceess();
	}

}
