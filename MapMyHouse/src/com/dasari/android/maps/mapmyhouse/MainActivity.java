package com.dasari.android.maps.mapmyhouse;

import java.io.IOException;
import java.lang.Character.UnicodeBlock;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract.CommonDataKinds;
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
import com.google.android.gms.internal.ca;
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

	private static final int REQUEST_SELECT_PHONE_NUMBER = 286;
	
	// Number U==7, N==6, I==8 by keyboard format.	
	private static final int QUERY_BY_UNIQUE_ID = 768;
	
	// Number P==10, H==6, O==9;
	private static final int QUERY_BY_PHONE_NUMBER = 1069;
	
	// Shared pref file.
	private static final String MY_SHARED_PREF = "mapMyHousePref";
	
	// shared pref UID
	private static final String MY_PREF_UID = "mapMyHousePref_uid";
	
	// shared pref Phone number
	private static final String MY_PREF_PHONE_NUMBER = "mapMyHousePref_phone_number";
	
	// sharef pref Address
	private static final String MY_PREF_ADDRESS = "mapMyHousePref_Address";

	private GoogleMap mGoogleMap;

	private boolean mRegisterEnable = true;
	private boolean mNavigationEnable = false;
	private boolean mMyPageEnable = false;

	private String mURLAll = "http://mapmyhouse.in/MapMyhouse/MapMyHouse/GetAllData";
	private String mURLUni = "http://mapmyhouse.in/MapMyhouse/MapMyHouse/GetDataOfID?id=";
	private String mURLPho = "http://mapmyhouse.in/MapMyhouse/MapMyHouse/GetDataByPh?ph=";
	
	// private Location mCurrentLocation;
	// LocationClient mLocationClient;

	private MenuItem mRegister;

	private SharedPreferences mMySharedPrefs;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main);

//		ActionBar actionBar = getActionBar();
//		actionBar.setBackgroundDrawable(new ColorDrawable(Color
//				.parseColor("#90000000")));
//		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color
//				.parseColor("#80000000")));
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
		MyLocationManager.getInstance().registerLocationListener(this);
		MyLocationManager.getInstance().initialize(getApplicationContext());

		mMySharedPrefs = getSharedPreferences(MY_SHARED_PREF, MODE_PRIVATE);
		
		myDetails = new LocationDetails();
		mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		mGoogleMap.setOnMyLocationButtonClickListener(this);
		mGoogleMap.setMyLocationEnabled(true);

		mGoogleMap.getUiSettings().setZoomControlsEnabled(false);

		View locationButton = ((View) findViewById(1).getParent())
				.findViewById(2);

		// and next place it, for exemple, on bottom right (as Google Maps app)
//		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton
//				.getLayoutParams();
//		// position on right bottom
//		rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//		rlp.setMargins(0, 0, 30, 30);

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

	private void doSearch(String query, int searchBy) {
		String url = null;
		if (searchBy == QUERY_BY_UNIQUE_ID){
			 url = mURLUni + query;
		} else if (searchBy == QUERY_BY_PHONE_NUMBER){
			url = mURLPho + query;
		} else {
			// U should never reach here. Because we query by either phone or Unique id.
			Toast.makeText(this, R.string.wrong_query, Toast.LENGTH_LONG).show();
			return;
		}
	//	String url = mURLAll;
		HttpConnectionManager.getInstance().makeRequest(this, url,
				REQUEST_TYPE.GET, 101, this, null);
	}
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// Associate searchable configuration with the SearchView
		mRegister = menu.findItem(R.id.register);
        mRegister.setVisible(mRegisterEnable);
        if(mRegisterEnable)
        {
            MenuItem myPage = menu.findItem(R.id.mypage);
            myPage.setVisible(false);
        }
        else
        {
            MenuItem myPage = menu.findItem(R.id.mypage);
            myPage.setVisible(true);
        }
       // mRegister.
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
		            doSearch(query, QUERY_BY_UNIQUE_ID);
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
		mRegister = menu.findItem(R.id.register);
		MenuItem navigation = menu.findItem(R.id.navigation);
		mRegister.setVisible(mRegisterEnable);
		navigation.setVisible(mNavigationEnable);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.navigation :
				 Intent navigation = new Intent(Intent.ACTION_VIEW);
	 			 navigation.setData(Uri.parse("geo:0,0?q=" + mLocationResponse.getMy_house_latitude()+ "," + mLocationResponse.getMy_house_longitude() + "("
	 			 + mLocationResponse.getMy_house_address() + ")"));
	 			 startActivity(navigation);			
				break;
			case R.id.register:
				registerLocation();
				break;
			case R.id.Search_phone_number:
				selectContactPhoneNumber();
				break;
			case R.id.mypage:
				launchMyPageActivity();
			default :
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void launchMyPageActivity() {
		Intent intent = new Intent(this, MyLocationPage.class);
		startActivity(intent);
	}

	public void selectContactPhoneNumber() {
	    // Start an activity for the user to pick a phone number from contacts
	    Intent intent = new Intent(Intent.ACTION_PICK);
	    intent.setType(CommonDataKinds.Phone.CONTENT_TYPE);
	    if (intent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
	        // Get the URI and query the content provider for the phone number
	        Uri contactUri = data.getData();
	        String[] projection = new String[]{CommonDataKinds.Phone.NUMBER};
	        Cursor cursor = getContentResolver().query(contactUri, projection,
	                null, null, null);
	        // If the cursor returned is valid, get the phone number
	        if (cursor != null && cursor.moveToFirst()) {
	            int numberIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
	            String number = cursor.getString(numberIndex);
	            Log.i("rami", "phone nmber before..        "  +number );
	            String uni_number = numberUnification(number);
	            Log.i("rami", "phone nmber after..        "  + uni_number );

	            doSearch(uni_number, QUERY_BY_PHONE_NUMBER);
	        }
	    }
	}
	
	private String numberUnification(String number){
		if (number.startsWith("0")){
			number = number.substring(1);
			Log.i("rami", "start 0.... "+ number);
			return number;
		
		}else if (number.startsWith("+91")){
			number = number.substring(3);
			Log.i("rami", "start +91... "+ number);

			return number;
		}else {
			Log.i("rami", "else.... "+ number);

			return number;
		}
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
		if (mMySharedPrefs != null){
			mRegisterEnable = false;
			mGoogleMap.addMarker(new MarkerOptions().title(mMySharedPrefs.getString(MY_PREF_UID, "myLocation"))
					.snippet(mMySharedPrefs.getString(MY_PREF_ADDRESS, "This is my location")).position(myloc));
		} else {
		mGoogleMap.addMarker(new MarkerOptions().title("My Location")
				.snippet("This is my location").position(myloc));
		}
		// TODO random code.. generaiton.
		invalidateOptionsMenu();
		myDetails.setUniqueKey("rami99999");

		new DownlaodAddressFormLatLog().execute(localLat, localLong);
		// MyLocationDao myLocDao = new MyLocationDao();
		// myLocDao.setMyHouseID("KA9999");
		// myLocDao.setMyLocationLatitude(mLatitude);
		// myLoacDao.setMyLocationLongitude(mLongitude);
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
		Log.i("rami", "in get respone");
		if (response != null) {
			Log.i("rami", "in respose != null");
			Log.i("rami", response.toString());
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			List<MyLocationResponseDao> list=null;
			Type listType = new TypeToken<List<MyLocationResponseDao>>(){}.getType();
			list = gson.fromJson(response.toString(),listType);
			if(list != null && list.size() !=0)
			{
				Log.i(TAG, list.get(0).getMy_house_id());
				Log.i("rami",list.get(0).getPhoneNumber() );
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
