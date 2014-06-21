package com.dasari.android.maps.mapmyhouse.httpconnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.dasari.android.maps.mapmyhouse.httpconnection.HttpConnectionManager.IOResponseListener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class HttpPostAsyncTask extends AsyncTask<String, Integer, String> {

	private IOResponseListener mResponseListener = null;
	private Context mContext = null;
	private int mRequestID = -1;
	private static String STATUS_SUCCESS = "success";
	private HttpParams mHttpParams = null;
	public HttpPostAsyncTask(Context context, int requestID, IOResponseListener listener, HttpParams params) {
		mContext = context;
		mRequestID = requestID;
		mResponseListener = listener;
		mHttpParams = params;
	}
	@Override
	protected void onPreExecute() {
		checkDataConnectivity();
	}

	private boolean checkDataConnectivity() {
		ConnectivityManager checkConnection = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = (NetworkInfo) checkConnection
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobileInfo = (NetworkInfo) checkConnection
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
			return true;
		}
		return false;
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL postUrl;
		DataOutputStream output;
		try {
			String api = (String) params[0];

			postUrl = new URL(api);
			HttpURLConnection connection = (HttpURLConnection) postUrl
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			// HTTP/1.0 may not support this function.
			connection.setChunkedStreamingMode(0);
			connection.setUseCaches(false);
			connection.setRequestProperty("content-type", "application/json");

			connection.connect();
			
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("unique_key", mHttpParams.getParameter("unique_key"));
			jsonParams.put("latitude", mHttpParams.getParameter("latitude"));
			jsonParams.put("longitude", mHttpParams.getParameter("longitude"));
			jsonParams.put("address", mHttpParams.getParameter("address"));

			output = new DataOutputStream(connection.getOutputStream());
			output.writeBytes(jsonParams.toString());
			
			return STATUS_SUCCESS;
			
		} catch (JSONException e) {
			mResponseListener.onExceptionReceived(e);
			return null;
		} catch (MalformedURLException e1) {
			mResponseListener.onExceptionReceived(e1);
			return null;
		} catch (IOException e) {
			mResponseListener.onExceptionReceived(e);
			return null;
		}
	}
	@Override
	protected void onPostExecute(String result) {
		if(result != null)
		{
			mResponseListener.onResponseReceived(result, mRequestID);
		}
	}
}
