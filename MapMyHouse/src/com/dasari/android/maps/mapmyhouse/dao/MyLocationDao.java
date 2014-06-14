package com.dasari.android.maps.mapmyhouse.dao;

public class MyLocationDao {
	
	private int _id;
	private String myHouseID;
	private double myLocationLatitude;
	private double myLocationLongitude;
	private String myAddress;
	private String Reserver1;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getMyHouseID() {
		return myHouseID;
	}
	public void setMyHouseID(String myHouseID) {
		this.myHouseID = myHouseID;
	}
	public double getMyLocationLatitude() {
		return myLocationLatitude;
	}
	public void setMyLocationLatitude(double myLocationLatitude) {
		this.myLocationLatitude = myLocationLatitude;
	}
	public double getMyLocationLongitude() {
		return myLocationLongitude;
	}
	public void setMyLocationLongitude(double myLocationLongitude) {
		this.myLocationLongitude = myLocationLongitude;
	}
	public String getMyAddress() {
		return myAddress;
	}
	public void setMyAddress(String myAddress) {
		this.myAddress = myAddress;
	}
	public String getReserver1() {
		return Reserver1;
	}
	public void setReserver1(String reserver1) {
		Reserver1 = reserver1;
	}
}
