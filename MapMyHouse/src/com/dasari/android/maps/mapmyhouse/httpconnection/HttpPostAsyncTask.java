package com.dasari.android.maps.mapmyhouse.httpconnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.dasari.android.maps.mapmyhouse.httpconnection.HttpConnectionManager.IOResponseListener;

public class HttpPostAsyncTask extends AsyncTask<String, Integer, String> {

	private String TAG = HttpGetAsynctask.class.getCanonicalName();
	private IOResponseListener mResponseListener = null;
	private Context mContext = null;
	private int mRequestID = -1;
	private static String STATUS_SUCCESS = "success";
	private HttpParams mHttpParams = null;
	public HttpPostAsyncTask(Context context, int requestID,
			IOResponseListener listener, HttpParams params) {
		mContext = context;
		mRequestID = requestID;
		mResponseListener = listener;
		mHttpParams = params;
	}
	@Override
	protected void onPreExecute() {
		checkDataConnectivity();
	}

	private void checkDataConnectivity() {
		ConnectivityManager checkConnection = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = (NetworkInfo) checkConnection
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobileInfo = (NetworkInfo) checkConnection
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
			// Do nothing as data connection is present.
		} else
			mResponseListener.onNoInternetAceess();
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL postUrl = null;
		DataOutputStream output = null;
		HttpURLConnection connection = null;
		try {
			String api = (String) params[0];

			postUrl = new URL(api);
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// HTTP/1.0 may not support this function.
			//connection.setChunkedStreamingMode(0);
			//connection.setRequestProperty("Content-Lenght", "10");
			connection.setUseCaches(false);
			//connection.set
			connection.setRequestProperty("content-type", "application/json");
			connection.connect();
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("latitude", mHttpParams.getParameter("latitude"));
			jsonParams.put("longitude", mHttpParams.getParameter("longitude"));
			jsonParams
					.put("unique_key", mHttpParams.getParameter("unique_key"));
			jsonParams.put("address", mHttpParams.getParameter("address"));
			jsonParams.put("reserved_1", mHttpParams.getParameter("reserved_1"));

			output = new DataOutputStream(connection.getOutputStream());
			output.writeBytes(jsonParams.toString());
			
			String status = ((HttpURLConnection) connection).getResponseMessage();
			int value = ((HttpURLConnection) connection).getResponseCode();

			InputStream customResponse;
			if (value == 200){
				customResponse = connection.getInputStream();
			}else {
				customResponse = connection.getErrorStream();
			}
			
			BufferedReader resultBufferReader = new BufferedReader(new InputStreamReader(customResponse));
			StringBuilder responseFromServer = new StringBuilder();
			String Line;
			if ((Line = resultBufferReader.readLine()) != null){
				responseFromServer.append(Line);
			}
			Log.i(TAG, "" +status+ value + " from server    "+ responseFromServer);

			//return STATUS_SUCCESS;
			return status;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		} catch (MalformedURLException e1) {
			Log.e(TAG, "An Mal fromed Url exception while post");
			mResponseListener.onExceptionReceived(e1);
			return null;
		} catch (IOException e2) {
			Log.e(TAG, "An Iu exception while post");
			mResponseListener.onExceptionReceived(e2);
			return null;

		} finally {
			if (connection != null)
				connection.disconnect();
		}

	}
	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			mResponseListener.onResponseReceived(result, mRequestID);
		}
	}
}
