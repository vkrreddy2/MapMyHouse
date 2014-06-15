package com.dasari.android.maps.mapmyhouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegisterMyLocation extends Activity {

	// Log tag for this class.
	private static final String TAG = RegisterMyLocation.class.getCanonicalName();
	
	// Constant extra value to return with address
	private static final String MY_ADDRESS_VALUE = "mMyAddress";
	
	// Constant extra value for latitude.
	private static final String LATITUDE = "latitude";
	
	// Constant extra value for longitude.
	private static final String LONGITUDE = "longitude";
	
	// Constant extra value for UniqueKey.
	private static final String UNIQUE_KEY = "unique_key";
	
	// Default values for latitude and longitude can any thing out of the range
	// +180 to -180. Chosen 333.333
	// Latitude value to get from intent extras.
	private double mMyLatitude = 333.333;
	
	// Longitude value to get from intent extras.
	private double mMyLongtitude = 333.333;
	
	// Unique value to display.
	//TODO: The default value should be a value, which should not match random generation.
	private String mMyUniqueKey = "Rami9999";
	
	// Latitude value to display. 
	private TextView mLatitudeValue;
	
	// Longitude value to display. 
	private TextView mLongitudeValue;
	
	// Unique value to display.
	private TextView mUniqueString;

	// Address value which user types in edit text.
	private EditText mAddress;
	
	// Root view for this layout.
	private View mRootView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_my_location);
		Intent localIntent = getIntent();
		mMyLatitude = localIntent.getDoubleExtra(LATITUDE, 333.333);
		mMyLongtitude = localIntent.getDoubleExtra(LONGITUDE, 333.333);
		mMyUniqueKey = localIntent.getStringExtra(UNIQUE_KEY);
		initView();

	}

	private void initView(){
		Log.v(TAG, "lat" + mMyLatitude + "  long  "+ mMyLongtitude);
		
		mRootView = (RelativeLayout)findViewById(R.id.container);
		
		mLatitudeValue = (TextView)mRootView.findViewById(R.id.mylatitudevalue);
		mLatitudeValue.setText(String.valueOf(mMyLatitude));
		mLongitudeValue = (TextView)mRootView.findViewById(R.id.mylongitudevalue);
		mLongitudeValue.setText(String.valueOf(mMyLongtitude));
		mUniqueString = (TextView)mRootView.findViewById(R.id.uniqueString);
		mUniqueString.setText(mMyUniqueKey);
		mAddress = (EditText)mRootView.findViewById(R.id.myAddress);
		
		
	}

	public void okActivity(View v) {
		Intent returnIntent = new Intent();
		String Addressvalue = mAddress.getText().toString();
		Log.v(TAG, "Click on OK button.. addressvalue  "+ Addressvalue);
		returnIntent.putExtra(MY_ADDRESS_VALUE, Addressvalue);
		setResult(RESULT_OK, returnIntent);
		finish();
		
	}
	
	public void cancelActivity(View v) {
		Intent returnIntent = new Intent();
		String Addressvalue = mAddress.getText().toString();
		Log.v(TAG, " Click on Cancel button.. addressvalue  "+ Addressvalue);
		returnIntent.putExtra(MY_ADDRESS_VALUE, Addressvalue);
		setResult(RESULT_CANCELED, returnIntent);
		Log.i("rami", "click cancel");
		finish();
		
	}

}
