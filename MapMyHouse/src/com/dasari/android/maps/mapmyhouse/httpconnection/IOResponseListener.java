package com.dasari.android.maps.mapmyhouse.httpconnection;

public interface IOResponseListener {
	public void onResponseReceived(Object response, int requestID);
	public void onExceptionReceived(Exception ex);
	public void onNoInternetAceess();
}
