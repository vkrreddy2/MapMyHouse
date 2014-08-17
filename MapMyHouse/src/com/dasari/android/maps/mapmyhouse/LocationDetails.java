package com.dasari.android.maps.mapmyhouse;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationDetails implements Parcelable{
	// Latitude variable.
	private double mLatitude = 333.333;
	
	// Longitude Variable.
	private double mLongitude = 333.333;
	
	// Unique key.
	private String mUniqueKey = "rami9999";
	
	// Total address including city, postal and country.
	private String mTotalAddress = null;
	
	// Postal code. Ex: 523169
	private String mPostalCode = null;
	
	// Country Name. Ex: India
	private String mCountryName = null;
	
	// Administration area. Ex: Karnataka
	private String mAdminArea = null;
	
	// Locality. Ex: Bangalore
	private String mLocality = null;
	
	// getters and setters for the above field variables.
	
	// Getter method for Latitude.
	public double getLatitude() {
		return mLatitude;
	}
	// Setter method for Latitude.
	public void setLatitude (double setLong){
		mLatitude = setLong;
	}
	
	// Getter method for Longitude.
	public double getLongitude() {
		return mLongitude;
	}
	// Setter method for Longitude.
	public void setLongitude (double setLongitude){
		mLongitude = setLongitude;
	}
	
	// Getter method for total address.
	public String getTotalAddress() {
		return mTotalAddress;
	}
	// Setter method for total address.
	public void setTotalAddress (String setTotalAddress){
		mTotalAddress = setTotalAddress;
	}
	
	// Getter method for administrator.
	public String getAdmin() {
		return mAdminArea;
	}
	// Setter method for administrator.
	public void setAdmin (String setAdmin){
		mAdminArea = setAdmin;
	}
	
	// Getter method for locality.
	public String getLocality() {
		return mLocality;
	}
	// Setter method for locality.
	public void setLocality (String setLocal){
		mLocality = setLocal;
	}
	
	// Getter method for Longitude.
	public String getPostalCode() {
		return mPostalCode;
	}
	// Setter method for Longitude.
	public void setPostalCode (String setPostal){
		mPostalCode = setPostal;
	}
	
	// Getter method for country.
	public String getCountry() {
		return mCountryName;
	}
	// Setter method for country.
	public void setCountry (String setCountry){
		mCountryName = setCountry;
	}
	
	// Getter method for unique key.
	public String getUniqueKey() {
		return mUniqueKey;
	}
	// Setter method for unique key.
	public void setUniqueKey (String setKey){
		mUniqueKey = setKey;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeDouble(mLatitude);
		dest.writeDouble(mLongitude);
		dest.writeString(mUniqueKey);
		dest.writeString(mTotalAddress);
		dest.writeString(mAdminArea);
		dest.writeString(mLocality);
		dest.writeString(mPostalCode);
		dest.writeString(mCountryName);
	}
	
	public static final Parcelable.Creator<LocationDetails> CREATOR = new Creator<LocationDetails>() {

		@Override
		public LocationDetails createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			LocationDetails lDetails = new LocationDetails();
			lDetails.mLatitude = source.readDouble();
			lDetails.mLongitude = source.readDouble();
			lDetails.mUniqueKey = source.readString();
			lDetails.mTotalAddress = source.readString();
			lDetails.mAdminArea = source.readString();
			lDetails.mLocality = source.readString();
			lDetails.mPostalCode = source.readString();
			lDetails.mCountryName = source.readString();
			return lDetails;
		}

		@Override
		public LocationDetails[] newArray(int size) {
			// TODO Auto-generated method stub
			return new LocationDetails[size];
		}
	};
}
