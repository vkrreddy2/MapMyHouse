package com.dasari.android.maps.mapmyhouse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import android.support.v7.app.ActionBarActivity;
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

public class MainActivity extends Activity implements
		OnMyLocationButtonClickListener, ILocationChangeListener, OnClickListener{

	private static final int LOCATION_REQUEST_CODE = 4444;
	
	//Log Tag constant
	private static final String TAG = MainActivity.class.getCanonicalName();
		
	// Constant extra value to return with address
	private static final String MY_ADDRESS_VALUE = "mMyAddress";
	
	// Constant extra value for latitude.
	private static final String LATITUDE = "latitude";
	
	// Constant extra value for longitude.
	private static final String LONGITUDE = "longitude";
	
	// Constant extra value for UniqueKey.
	private static final String UNIQUE_KEY = "unique_key";
	
	// Constant extra value for locality.
	private static final String LOCALITY = "locality";
	
	// Constant extra value for administrator.
	private static final String ADMIN = "administration";
	
	// Constant extra value for postal code.
	private static final String POSTAL_CODE = "postal_code";
	
	// Constant extra value for country.
	private static final String COUNTRY = "country";
	
	private GoogleMap mGoogleMap;
	private ImageView mNavigationButton;
	private double mLatitude = 333.333;
	private double mLongitude = 333.333;
	private String mUniqueKey = "rami9999";
	private String mAddress = null;
	private String mPostalCode = null;
	private String mCountryName = null;
	private String mAdminArea = null;
	private String mLocality = null;
	
	private String[] mCityAddress = null;

	private boolean mRegisterEnable;

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
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem register = menu.findItem(R.id.register);
		register.setVisible(mRegisterEnable);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.register) {
			 registerLocation(mLatitude, mLongitude ,mUniqueKey);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		 mLatitude= location.getLatitude();
		 mLongitude = location.getLongitude();
		 LatLng myloc = new LatLng(mLatitude, mLongitude);
		 mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc, 13));
		 mGoogleMap.addMarker(new MarkerOptions().title("My Location")
		 .snippet("This is my location").position(myloc));
		 
         new DownlaodAddressFormLatLog().execute(mLatitude, mLongitude);
//		 MyLocationDao myLocDao = new MyLocationDao();
//		 myLocDao.setMyHouseID("KA9999");
//		 myLocDao.setMyLocationLatitude(mLatitude);
//		 myLocDao.setMyLocationLongitude(mLongitude);
//		 myLocDao.setMyAddress("Flat# 311/A, Deverabisinahalli, Bangalore");
//		 
//		 MapMyHouseDBHelper.getInstance(getApplicationContext()).openDatabase();
//		 MapMyHouseDBHelper.getInstance(getApplicationContext()).insert(myLocDao);
//		 MapMyHouseDBHelper.getInstance(getApplicationContext()).close();
//	 
	
	}

	private class DownlaodAddressFormLatLog extends AsyncTask<Double, Void, Void>{

		@Override
		protected Void doInBackground(Double... params) {
			 // Used to get the NEAREST address of the location pointed by
			 // longitude and latitude.
			 Geocoder myLoc = new Geocoder(MainActivity.this, Locale.getDefault());
			 List<android.location.Address> myadd;
			try {
				myadd = myLoc.getFromLocation(params[0], params[1], 1);
				 if (myadd !=null && myadd.size()>0){
					 for (android.location.Address add : myadd){
						 mLocality = add.getLocality();
						 mAdminArea = add.getAdminArea();
						 mPostalCode = add.getPostalCode();
						 mCountryName = add.getCountryName();
						 Log.v("rami", "add     "+ add + "size -- " +myadd.size() + "\n"
								+ mLocality + "\n"+ mAdminArea + "\n"+ mPostalCode + "\n"+ mCountryName);
					 }
					    
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mRegisterEnable = true;
			invalidateOptionsMenu();
		}
	}
	/**
	 * To register location and showing user his unique key along with latitude
	 * longitude and prompting user to fill address (optional).
	 * @param latitude Latitude value of the user location.
	 * @param longitude Longitude value of the user location.
	 * @param key
	 */
	private void registerLocation(Double latitude, Double longitude, String key) {
		Intent registerLocation = new Intent(this, RegisterMyLocation.class);
		registerLocation.putExtra(LATITUDE, latitude);
		registerLocation.putExtra(LONGITUDE, longitude);
		registerLocation.putExtra(UNIQUE_KEY, key);
		registerLocation.putExtra(LOCALITY, mLocality);
		registerLocation.putExtra(ADMIN, mAdminArea);
		registerLocation.putExtra(POSTAL_CODE, mPostalCode);
		registerLocation.putExtra(COUNTRY, mCountryName);

		startActivityForResult(registerLocation, LOCATION_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK){
			if (data != null){
				Log.v(TAG, "onResultActivity()... result OK.");
				mAddress = data.getStringExtra(MY_ADDRESS_VALUE);
			}
		}
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
