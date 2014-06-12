package com.dasari.android.maps.mapmyhouse.interfaces;

import com.google.android.gms.common.ConnectionResult;

import android.location.Location;
import android.os.Bundle;

public interface ILocationChangeListener {
	public void onLocationChange(Location location);
	public void onServiceConnected(Bundle args);
	public void onServiceDisconnected();
	public void onServiceConnectionFailed(ConnectionResult arg0);
}
