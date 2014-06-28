package com.dasari.android.maps.mapmyhouse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.dasari.android.maps.mapmyhouse.dao.MyLocationResponseDao;
import com.dasari.android.maps.mapmyhouse.httpconnection.HttpConnectionManager;
import com.dasari.android.maps.mapmyhouse.httpconnection.HttpConnectionManager.IOResponseListener;
import com.dasari.android.maps.mapmyhouse.httpconnection.HttpConnectionManager.REQUEST_TYPE;
import com.dasari.android.maps.mapmyhouse.location.MyLocationManager;
import com.dasari.android.maps.mapmyhouse.location.MyLocationManager.ILocationChangeListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity
		implements
			OnMyLocationButtonClickListener,
			ILocationChangeListener,
			OnClickListener,
			IOResponseListener {

	// Log tag
	private static final String TAG = MainActivity.class.getCanonicalName();

	// Parcelable location details
	public LocationDetails myDetails;
	private MyLocationResponseDao mLocationResponse;
	// Extra Constant for passing to register acitivity..
	private static final String LOACL_DETAILS_PARCEL = "com.android.dasari.myLocalDetails";

	private GoogleMap mGoogleMap;

	private boolean mRegisterEnable;
	private boolean mNavigationEnable = false;
	private String mURLAll = "http://ibreddy.in/REST/MapMyHouse/GetAllData";
	private String mURLOne = "http://ibreddy.in/REST/MapMyHouse/GetDataOfID?id=";
	// private Location mCurrentLocation;
	// LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#90000000")));
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#80000000")));

		MyLocationManager.getInstance().registerLocationListener(this);
		MyLocationManager.getInstance().initialize(getApplicationContext());

		myDetails = new LocationDetails();
		mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		mGoogleMap.setOnMyLocationButtonClickListener(this);
		mGoogleMap.setMyLocationEnabled(true);

		mGoogleMap.getUiSettings().setZoomControlsEnabled(false);

		View locationButton = ((View) findViewById(1).getParent())
				.findViewById(2);

		// and next place it, for exemple, on bottom right (as Google Maps app)
		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton
				.getLayoutParams();
		// position on right bottom
		rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		rlp.setMargins(0, 0, 30, 30);

		/*
		 * LatLng bangalore = new LatLng(12.971599, 77.594563);
		 * mGoogleMap.setOnMyLocationButtonClickListener(this);
		 * mGoogleMap.setMyLocationEnabled(true);
		 * mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangalore,
		 * 13)); mGoogleMap.addMarker(new MarkerOptions().title("Bangalore")
		 * .snippet("The most populous city in India.") .position(bangalore));
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

	private void doSearch(String query) {
		String url = mURLOne + query;
	//	String url = mURLAll;
		HttpConnectionManager.getInstance().makeRequest(this, url,
				REQUEST_TYPE.GET, 101, this, null);
	}
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
		         MenuItem searchMenuItem = menu.findItem(R.id.search);
		            if (searchMenuItem != null) {
		                searchMenuItem.collapseActionView();
		            }
		            
		            Log.v(TAG, query);
		            doSearch(query);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				Log.v(TAG, newText);
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem register = menu.findItem(R.id.register);
		MenuItem navigation = menu.findItem(R.id.navigation);
		register.setVisible(mRegisterEnable);
		navigation.setVisible(mNavigationEnable);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.register) {
			registerLocation();
			return true;
		}
		if(item.getItemId() == R.id.navigation)
		{
			 Intent navigation = new Intent(Intent.ACTION_VIEW);
			 navigation.setData(Uri.parse("geo:0,0?q=" + mLocationResponse.getMy_house_latitude()+ "," + mLocationResponse.getMy_house_longitude() + "("
			 + mLocationResponse.getMy_house_address() + ")"));
			 startActivity(navigation);

		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onMyLocationButtonClick() {
		MyLocationManager.getInstance().getLocation(getApplicationContext());
		return false;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyLocationManager.getInstance().registerLocationListener(this);
		MyLocationManager.getInstance().initialize(getApplicationContext());
		MyLocationManager.getInstance().connectToLocationService();

	};
	@Override
	protected void onStop() {
		MyLocationManager.getInstance().stopPeriodicUpdates();
		MyLocationManager.getInstance().unRegisterLocationListener();
		super.onStop();
	}

	@Override
	public void onLocationChange(Location location) {
		myDetails = new LocationDetails();
		myDetails.setLatitude(location.getLatitude());
		myDetails.setLongitude(location.getLongitude());
		double localLat = myDetails.getLatitude();
		double localLong = myDetails.getLongitude();
		LatLng myloc = new LatLng(localLat, localLong);
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc, 13));
		mGoogleMap.addMarker(new MarkerOptions().title("My Location")
				.snippet("This is my location").position(myloc));
		// TODO random code.. generaiton.

		myDetails.setUniqueKey("rami99999");

		new DownlaodAddressFormLatLog().execute(localLat, localLong);
		// MyLocationDao myLocDao = new MyLocationDao();
		// myLocDao.setMyHouseID("KA9999");
		// myLocDao.setMyLocationLatitude(mLatitude);
		// myLocDao.setMyLocationLongitude(mLongitude);
		// myLocDao.setMyAddress("Flat# 311/A, Deverabisinahalli, Bangalore");
		//
		// MapMyHouseDBHelper.getInstance(getApplicationContext()).openDatabase();
		// MapMyHouseDBHelper.getInstance(getApplicationContext()).insert(myLocDao);
		// MapMyHouseDBHelper.getInstance(getApplicationContext()).close();
		//

	}

	private class DownlaodAddressFormLatLog
			extends
				AsyncTask<Double, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Double... params) {
			// Used to get the NEAREST address of the location pointed by
			// longitude and latitude.
			Geocoder myLoc = new Geocoder(MainActivity.this,
					Locale.getDefault());
			List<android.location.Address> myadd;
			try {
				myadd = myLoc.getFromLocation(params[0], params[1], 1);
				if (myadd != null && myadd.size() > 0) {
					for (android.location.Address add : myadd) {
						myDetails.setLocality(add.getLocality());
						myDetails.setAdmin(add.getAdminArea());
						myDetails.setPostalCode(add.getPostalCode());
						myDetails.setCountry(add.getCountryName());
					}

				}
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "IO exception at GeoCoder.. probably system error");
				e.printStackTrace();
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result)
				Toast.makeText(MainActivity.this,
						R.string.geocoder_connection_error, Toast.LENGTH_LONG)
						.show();
			mRegisterEnable = true;
			invalidateOptionsMenu();
		}
	}
	/**
	 * To register location and showing user his unique key along with latitude
	 * longitude and prompting user to fill address (optional).
	 * 
	 * @param latitude
	 *            Latitude value of the user location.
	 * @param longitude
	 *            Longitude value of the user location.
	 * @param key
	 */
	private void registerLocation() {
		Intent registerLocation = new Intent(this, RegisterMyLocation.class);
		// Bundle bundle = new Bundle();
		// bundle.putParcelable(LOACL_DETAILS_PARCEL, myDetails);
		registerLocation.putExtra(LOACL_DETAILS_PARCEL, (Parcelable) myDetails);
		// registerLocation.putExtras(bundle);

		startActivity(registerLocation);
	}

	@Override
	public void onServiceConnected(Bundle args) {
		Toast.makeText(getApplicationContext(), "Location service connected",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onServiceDisconnected() {
		Toast.makeText(getApplicationContext(),
				"Location service disconnected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onServiceConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(getApplicationContext(),
				"Location service connected Failed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.direction_button :
		//
		// Intent navigation = new Intent(Intent.ACTION_VIEW);
		// navigation.setData(Uri.parse("geo:0,0?q=12.927204,77.686455 ("
		// + "My House" + ")"));
		// startActivity(navigation);
		//
		// }
	}
	
	
	@Override
	public void onResponseReceived(Object response, int requestID) {
		if (response != null) {
			Log.i(TAG, response.toString());
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			List<MyLocationResponseDao> list=null;
			Type listType = new TypeToken<List<MyLocationResponseDao>>(){}.getType();
			list = gson.fromJson(response.toString(),listType);
			if(list != null && list.size() !=0)
			{
				Log.i(TAG, list.get(0).getMy_house_id());
				moveToLocation(list.get(0));
			}
		}
	}

	@Override
	public void onExceptionReceived(Exception ex) {
		Log.v(TAG, ex.getMessage());
	}

	@Override
	public void onNoInternetAceess() {
		Log.v(TAG, "No Internet");
	}
	
	private void moveToLocation(MyLocationResponseDao myLocationDao)
	{
		if(mGoogleMap != null)
		{
			mGoogleMap.clear();
			double localLat = Double.parseDouble(myLocationDao.getMy_house_latitude());
			double localLong = Double.parseDouble(myLocationDao.getMy_house_longitude());
			LatLng myloc = new LatLng(localLat, localLong);
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc, 13));
			mGoogleMap.addMarker(new MarkerOptions().title(myLocationDao.getMy_house_id())
					.snippet(myLocationDao.getMy_house_address()).position(myloc));
			mNavigationEnable = true;
			mLocationResponse = myLocationDao;
			invalidateOptionsMenu();

		}
	}
}
