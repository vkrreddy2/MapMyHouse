package com.dasari.android.maps.mapmyhouse.httpconnection;

import android.content.Context;

public class HTTPConnectionManager {

	private static HTTPConnectionManager mInstance = null;
	private MyLocationRequestTask mLocationRequestAsyncTask = null; 
	private HTTPConnectionManager() {

	}

	public static HTTPConnectionManager getInstance() {
		if (mInstance == null) {
			mInstance = new HTTPConnectionManager();
		}
		return mInstance;
	}

	public void makeRequest(Context context, String url, int requestID,
			IOResponseListener listenr) {
		mLocationRequestAsyncTask = new MyLocationRequestTask(
				context, requestID);
		mLocationRequestAsyncTask.registerResponseListener(listenr);
		mLocationRequestAsyncTask.doInBackground(url);
	}
	
	public void cancelrequest()
	{
		mLocationRequestAsyncTask.cancel(true);
	}

}
