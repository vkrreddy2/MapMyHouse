package com.dasari.android.maps.mapmyhouse;


import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasari.android.maps.mapmyhouse.httpconnection.HttpConnectionManager;
import com.dasari.android.maps.mapmyhouse.httpconnection.HttpConnectionManager.IOResponseListener;

public class RegisterMyLocation extends Activity implements IOResponseListener{

	// Extra Constant for passing to register acitivity..
	private static final String LOACL_DETAILS_PARCEL = "com.android.dasari.myLocalDetails";
	
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
	
	// Constant extra for phonenumber.
	private static final String PHONE_NUMBER = "phoneNumber";
	
	// Constant extra for total address.
	private static final String MY_ADDRESS = "address";
	// Constant extra value for locality.
	private static final String LOCALITY = "locality";
	
	// Constant extra value for administrator.
	private static final String ADMIN = "administration";
	
	// Constant extra value for postal code.
	private static final String POSTAL_CODE = "postal_code";
	
	// Constant extra value for country.
	private static final String COUNTRY = "country";
	
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
	
	private EditText mUniqueID;
	
	// Address value which user types in edit text.
	private EditText mAddress;
	
	// Locality from Lat Lon.
	private EditText mLocality;
	
	// Admin from Lat Lon.
	private EditText mAdmin;
	
	// Postal code from Lat Lon.
	private EditText mPostalCode;
	
	// Country from Lat Lon.
	private EditText mCountry;
	
	// Phone number.
	private EditText mPhoneNumber;
	
	
	// Root view for this layout.
	private View mRootView;

	private String mLocalityExtra;

	private String mAdminExtra;

	private String mPostalCodeExtra;

	private String mCountryExtra;

	private LocationDetails locDetails;

	private String postUrl = "http://ibreddy.in/REST/MapMyHouse/addMapData";
	
	// Response id for post connection.
	private static final int POST_REQUEST_ID = 216;

	// http params to pass.
	private HttpParams mParams;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_my_location);
		Intent localIntent = getIntent();
		locDetails = (LocationDetails)localIntent.getParcelableExtra(LOACL_DETAILS_PARCEL);
		mMyLatitude = locDetails.getLatitude();
		mMyLongtitude = locDetails.getLongitude();
		mMyUniqueKey = locDetails.getUniqueKey();
		mAdminExtra = locDetails.getAdmin();
		mLocalityExtra = locDetails.getLocality();
		mPostalCodeExtra = locDetails.getPostalCode();
		mCountryExtra = locDetails.getCountry();
		getActionBar().setTitle(R.string.register_location);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
		initView();

	}

	private void initView(){
	
		mRootView = (LinearLayout)findViewById(R.id.root_view);
		
		mUniqueID = (EditText)mRootView.findViewById(R.id.myUniqueID);
		mLatitudeValue = (TextView)mRootView.findViewById(R.id.mylatitudevalue);
		mLatitudeValue.setText(String.valueOf(mMyLatitude));
		mLongitudeValue = (TextView)mRootView.findViewById(R.id.mylongitudevalue);
		mLongitudeValue.setText(String.valueOf(mMyLongtitude));
		mUniqueString = (TextView)mRootView.findViewById(R.id.myUniqueID);
		mAddress = (EditText)mRootView.findViewById(R.id.myAddress);
		mLocality = (EditText)mRootView.findViewById(R.id.myLocality);
		mLocality.setText(mLocalityExtra);
		mAdmin = (EditText)mRootView.findViewById(R.id.myAdminArea);
		mAdmin.setText(mAdminExtra);
		mPostalCode = (EditText)mRootView.findViewById(R.id.myPostalCode);
		mPostalCode.setText(mPostalCodeExtra);
		mCountry = (EditText)mRootView.findViewById(R.id.myCountry);
		mCountry.setText(mCountryExtra);
		
		// Phone number view.
	    mPhoneNumber = (EditText)mRootView.findViewById(R.id.myPhone_number);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.register_my_location, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_cancel :
				finish();
				break;
			case R.id.action_save :
				registerLocation();
			default :
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void registerLocation() {

		String totalAddress = mAddress.getText().toString() + "\n" +
		                      mAdmin.getText().toString() + "\n" +
				              mLocality.getText().toString() + "\n" +
		                      mPostalCode.getText().toString() + "\n" +
				              mCountry.getText().toString();
		mParams = new BasicHttpParams();
		mParams.setParameter(LATITUDE, mLatitudeValue.getText().toString());
		mParams.setParameter(LONGITUDE, mLongitudeValue.getText().toString());
		mParams.setParameter(UNIQUE_KEY, mUniqueID.getText().toString());
		mParams.setParameter(MY_ADDRESS, totalAddress);
		mParams.setParameter("reserved_1","Reserved");
		mParams.setParameter(PHONE_NUMBER, mPhoneNumber.getText().toString());
		HttpConnectionManager.getInstance().makeRequest(RegisterMyLocation.this, postUrl ,HttpConnectionManager.REQUEST_TYPE.POST,
				POST_REQUEST_ID , RegisterMyLocation.this, mParams);
		
	}
	
	@Override
	public void onResponseReceived(Object response, int requestID) {
		// TODO Auto-generated method stub
		finish();
		
	}

	@Override
	public void onExceptionReceived(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNoInternetAceess() {
		// TODO Auto-generated method stub
		
	}

}
