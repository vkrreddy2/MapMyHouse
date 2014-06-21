package com.dasari.android.maps.mapmyhouse.httpconnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class HttpPostAsyncTask
		extends
			AsyncTask<String, Integer, Integer> {

	private static final String POST_URL = null;
	private Context mContext;

	public HttpPostAsyncTask(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		checkDataConnectivity();
	}
	
	private boolean checkDataConnectivity() {
		ConnectivityManager checkConnection = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = (NetworkInfo)checkConnection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobileInfo = (NetworkInfo)checkConnection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifiInfo.isConnected() || mobileInfo.isConnected()){
			return true;
		}
		return false;
	}
	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL postUrl;
		DataOutputStream output;

		try {
			postUrl = new URL(POST_URL);

			HttpURLConnection connect = (HttpURLConnection) postUrl
					.openConnection();
			connect.setRequestMethod("POST");
			connect.setDoOutput(true);
			// HTTP/1.0 may not support this function.
			connect.setChunkedStreamingMode(0);
			connect.setUseCaches(false);
			connect.setRequestProperty("content-type", "application/json");

			connect.connect();
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("latitude", params[0]);
			jsonParams.put("longitude", params[1]);
			jsonParams.put("unique_key", params[2]);
			jsonParams.put("address", params[3]);

			output = new DataOutputStream(connect.getOutputStream());
			output.writeBytes(jsonParams.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
