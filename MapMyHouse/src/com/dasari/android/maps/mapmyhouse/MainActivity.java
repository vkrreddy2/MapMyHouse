package com.dasari.android.maps.mapmyhouse;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.dasari.android.maps.mapmyhouse.interfaces.ILocationChangeListener;
import com.dasari.android.maps.mapmyhouse.location.LocationManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity implements
		OnMyLocationButtonClickListener, ILocationChangeListener, OnClickListener{

	private GoogleMap mGoogleMap;
	private ImageView mNavigationButton;
	// private Location mCurrentLocation;
	// LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LocationManager.getInstance().registerLocationListener(this);
		LocationManager.getInstance().initialize(getApplicationContext());
		
		mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		mGoogleMap.setOnMyLocationButtonClickListener(this);
		mGoogleMap.setMyLocationEnabled(true);
		
		mNavigationButton = (ImageView)findViewById(R.id.direction_button);
		mNavigationButton.setOnClickListener(this);
/*		LatLng bangalore = new LatLng(12.971599, 77.594563);
		mGoogleMap.setOnMyLocationButtonClickListener(this);
		mGoogleMap.setMyLocationEnabled(true);
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangalore, 13));
		mGoogleMap.addMarker(new MarkerOptions().title("Bangalore")
				.snippet("The most populous city in India.")
				.position(bangalore));
*/
		/*
		 * map.setOnMapClickListener(new OnMapClickListener() {
		 * 
		 * @Override public void onMapClick(LatLng latlang) { LatLng area = new
		 * LatLng(latlang.latitude, latlang.longitude); map.addMarker(new
		 * MarkerOptions() .title("Clicked loc") .snippet("{" + latlang.latitude
		 * + "," + latlang.longitude + "}") .position(area));
		 * 
		 * } });
		 */
		// LocationManager locManager = (LocationManager)
		// getSystemService(LOCATION_SERVICE);
		// Criteria cri = new Criteria();
		// String bestProvider = locManager.getBestProvider(cri, true);
		//
		// Location loc = locManager.getLastKnownLocation(bestProvider);
		// String latMy = String.valueOf(loc.getLatitude());
		// String lngMy = String.valueOf(loc.getLongitude());

		// Toast.makeText(this, "Navigation", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onMyLocationButtonClick() {
		LocationManager.getInstance().getLocation(getApplicationContext());
		return false;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LocationManager.getInstance().connectToLocationService();
	}
	
	@Override
	protected void onStop() {
		LocationManager.getInstance().stopPeriodicUpdates();
		LocationManager.getInstance().unRegisterLocationListener();
		super.onStop();
	}

	@Override
	public void onLocationChange(Location location) {
		
		 double latitude = location.getLatitude();
		 double longitude = location.getLongitude();
		 LatLng myloc = new LatLng(latitude, longitude);
		 mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc, 13));
		 mGoogleMap.addMarker(new MarkerOptions().title("My Location")
		 .snippet("This si my location").position(myloc));
		
	}

	@Override
	public void onServiceConnected(Bundle args) {
		Toast.makeText(getApplicationContext(), "Location service connected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onServiceDisconnected() {
		Toast.makeText(getApplicationContext(), "Location service disconnected", Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onServiceConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(getApplicationContext(), "Location service connected Failed", Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.direction_button:

						 Intent navigation = new Intent(Intent.ACTION_VIEW);
						 navigation.setData(Uri.parse("geo:0,0?q=12.927204,77.686455 (" + "My House" + ")"));
						 startActivity(navigation);
						 
						 
		}
	}
}
