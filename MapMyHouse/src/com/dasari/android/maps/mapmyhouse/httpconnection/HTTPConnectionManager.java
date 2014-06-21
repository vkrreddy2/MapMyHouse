package com.dasari.android.maps.mapmyhouse.httpconnection;

import android.content.Context;

public class HttpConnectionManager {

	private static HttpConnectionManager mInstance = null;
	private HttpGetAsynctask mLocationRequestAsyncTask = null; 
	private HttpConnectionManager() {

	}

	public static HttpConnectionManager getInstance() {
		if (mInstance == null) {
			mInstance = new HttpConnectionManager();
		}
		return mInstance;
	}

	public void makeRequest(Context context, String url, int requestID,
			IOResponseListener listenr) {
		mLocationRequestAsyncTask = new HttpGetAsynctask(
				context, requestID, listenr);
		mLocationRequestAsyncTask.execute(url);
	}
	
	public void cancelrequest()
	{
		mLocationRequestAsyncTask.cancel(true);
	}

	public interface IOResponseListener {
		public void onResponseReceived(Object response, int requestID);
		public void onExceptionReceived(Exception ex);
		public void onNoInternetAceess();
	}

}
