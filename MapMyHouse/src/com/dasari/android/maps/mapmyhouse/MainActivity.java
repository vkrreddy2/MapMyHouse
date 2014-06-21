package com.dasari.android.maps.mapmyhouse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dasari.android.maps.mapmyhouse.location.MyLocationManager;
import com.dasari.android.maps.mapmyhouse.location.MyLocationManager.ILocationChangeListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity
		implements
			OnMyLocationButtonClickListener,
			ILocationChangeListener,
			OnClickListener {

	// Log tag
	private static final String TAG = MainActivity.class.getCanonicalName();

	// Parcelable location details
	public LocationDetails myDetails;

	// Extra Constant for passing to register acitivity..
	private static final String LOACL_DETAILS_PARCEL = "com.android.dasari.myLocalDetails";

	private GoogleMap mGoogleMap;
	private ImageView mNavigationButton;

	private boolean mRegisterEnable;

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

		mNavigationButton = (ImageView) findViewById(R.id.direction_button);
		mNavigationButton.setOnClickListener(this);
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
			registerLocation();
			return true;
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
		switch (v.getId()) {
			case R.id.direction_button :

				Intent navigation = new Intent(Intent.ACTION_VIEW);
				navigation.setData(Uri.parse("geo:0,0?q=12.927204,77.686455 ("
						+ "My House" + ")"));
				startActivity(navigation);

		}
	}
}
