package com.dasari.android.maps.mapmyhouse.dao;

public class MyLocationResponseDao {
	private String my_house_id;
	private String my_house_latitude;
	private String my_house_longitude;
	private String my_house_address;
	private String updatetime;
	private String reserved_1;
	private String phoneNumber;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getMy_house_id() {
		return my_house_id;
	}
	public void setMy_house_id(String my_house_id) {
		this.my_house_id = my_house_id;
	}
	public String getMy_house_latitude() {
		return my_house_latitude;
	}
	public void setMy_house_latitude(String my_house_latitude) {
		this.my_house_latitude = my_house_latitude;
	}
	public String getMy_house_longitude() {
		return my_house_longitude;
	}
	public void setMy_house_longitude(String my_house_longitude) {
		this.my_house_longitude = my_house_longitude;
	}
	public String getMy_house_address() {
		return my_house_address;
	}
	public void setMy_house_address(String my_house_address) {
		this.my_house_address = my_house_address;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getReserved_1() {
		return reserved_1;
	}
	public void setReserved_1(String reserved_1) {
		this.reserved_1 = reserved_1;
	}
	
	
	
}
