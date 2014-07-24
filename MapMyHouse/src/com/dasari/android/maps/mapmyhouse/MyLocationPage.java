package com.dasari.android.maps.mapmyhouse;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class MyLocationPage extends Activity{
	
	// Shared pref for Latitude.
	private static final String MY_PAGE_PREF_LATITUDE = "mapMyHousePref_latitude";

	// Shared pref for Longitude.
	private static final String MY_PAGE_PREF_LONGITUDE = "mapMyHousePref_longitude";

	// Shared pref file.
	private static final String MY_PAGE_SHARED_PREF = "mapMyHousePref";

	// shared pref UID
	private static final String MY_PAGE_PREF_UID = "mapMyHousePref_uid";

	// shared pref Phone number
	private static final String MY_PAGE_PREF_PHONE_NUMBER = "mapMyHousePref_phone_number";

	// sharef pref Address
	private static final String MY_PAGE_PREF_ADDRESS = "mapMyHousePref_Address";

	private SharedPreferences myPageSharedPref;

	private String myPageLatitude;

	private String myPageLongitude;

	private String myPageUId;

	private String myPageAddress;

	private WebView myMapView;

	private GoogleMap mPageGoogleMap;

	private String myPagePhoneNumber;

	private TextView myUidView;

	private TextView myPhoneView;

	private TextView myAddressView;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.my_location_page);
		loadSharedPref();

		mPageGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		mPageGoogleMap.getUiSettings().setZoomControlsEnabled(false);
		mPageGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
		LatLng myPageLoc = new LatLng(Double.parseDouble(myPageLatitude), Double.parseDouble(myPageLongitude));
		mPageGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPageLoc, 15));
		mPageGoogleMap.addMarker(new MarkerOptions().title(myPageUId).snippet(myPageAddress)
				.position(myPageLoc));
		myUidView = (TextView)findViewById(R.id.map_my_house_id);
		myUidView.setText(myPageUId);
		myPhoneView = (TextView)findViewById(R.id.my_page_phone_number);
		myPhoneView.setText(myPagePhoneNumber);
		myAddressView = (TextView)findViewById(R.id.my_page_address);
		myAddressView.setText(myPageAddress);
	}
	
	private void loadSharedPref() {
	    myPageSharedPref = getSharedPreferences(MY_PAGE_SHARED_PREF, MODE_PRIVATE);
	    myPageLatitude = myPageSharedPref.getString(MY_PAGE_PREF_LATITUDE, "0.0");
	    myPageLongitude = myPageSharedPref.getString(MY_PAGE_PREF_LONGITUDE, "0,0");
	    myPageUId = myPageSharedPref.getString(MY_PAGE_PREF_UID, "UID");
	    myPageAddress = myPageSharedPref.getString(MY_PAGE_PREF_ADDRESS, "My Location");
	    myPagePhoneNumber = myPageSharedPref.getString(MY_PAGE_PREF_PHONE_NUMBER, "00000");
	    Log.i("rami", " . "+ myPageAddress + myPageLatitude + myPageLongitude +myPageUId);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.my_location_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.share_via :
				Intent shareAction = new Intent(Intent.ACTION_SEND);
				shareAction.putExtra(Intent.EXTRA_TEXT, myPageUId);

				shareAction.setType("text/plain");
				startActivity(Intent.createChooser(shareAction, getResources().getText(R.string.send_to)));
				
				break;

			default :
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
