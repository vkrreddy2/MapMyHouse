package com.dasari.android.maps.mapmyhouse.httpconnection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dasari.android.maps.mapmyhouse.httpconnection.HttpConnectionManager.IOResponseListener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

public class HttpGetAsynctask extends AsyncTask<String, Void, String> {

	private IOResponseListener mResponseListener = null;
	private Context mContext = null;
	private int mRequestID = -1;

	public HttpGetAsynctask(Context context, int requestID, IOResponseListener listener) {
		mContext = context;
		mRequestID = requestID;
		mResponseListener = listener;
	}
	
	@Override
	protected void onPreExecute() {
		checkInternetConenction();
	}
	@Override
	protected String doInBackground(String... params) {
		try {
			String api = (String) params[0];
			URL url = new URL(api);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			String data = null;
			String response = "";
			while ((data = reader.readLine()) != null) {
				response += data + "\n";
			}
			return response;
		} catch (Exception e) {
			mResponseListener.onExceptionReceived(e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(String response) {
		if (response != null) {
			mResponseListener.onResponseReceived(response, mRequestID);
		}
	}

	// check Internet conenction.
	private void checkInternetConenction() {
		ConnectivityManager connectivity = (ConnectivityManager) this.mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// NetworkInfo[] info = connectivity.getAllNetworkInfo();
			// if (info != null)
			// for (int i = 0; i <info.length; i++)
			// if (info[i].getState() == NetworkInfo.State.CONNECTED)
			// {
			// Toast.makeText(mContext, "Internet is connected",
			// Toast.LENGTH_SHORT).show();
			// }
			//
		} else {
			mResponseListener.onNoInternetAceess();
		}
	}
}