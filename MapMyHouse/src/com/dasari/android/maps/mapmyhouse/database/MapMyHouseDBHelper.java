package com.dasari.android.maps.mapmyhouse.database;

import com.dasari.android.maps.mapmyhouse.dao.MyLocationDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MapMyHouseDBHelper extends SQLiteOpenHelper {

	public static final String TAG = MapMyHouseDBHelper.class
			.getCanonicalName();

	public static final String TABLE_NAME = "my_house_location";
	public static final String _ID = "_id";
	public static final String MY_HOUSE_ID = "my_house_id";
	public static final String MY_HOUSE_LATITUDE = "my_house_latitude";
	public static final String MY_HOUSE_LONGITUDE = "my_house_longitude";
	public static final String MY_HOUSE_ADDRESS = "my_house_address";
	public static final String RESERVED_1 = "reserved_1";

	private static final String DATABASE_NAME = "map_my_house.db";
	private static final int DATABASE_VERSION = 1;
	/* Singleton instance of this class to maintain the database object state. */
	private static MapMyHouseDBHelper mMapMyHpuseDBHelper = null;
	private SQLiteDatabase mSQLiteDBWriteObject = null;

	private static final String CREATE_TABLE_QUERY = "create table"
			+ TABLE_NAME + " (" + _ID + " integer autoincrement, "
			+ MY_HOUSE_ID + " text primary key, " + MY_HOUSE_LATITUDE
			+ " real not null, " + MY_HOUSE_LONGITUDE + " real not null, "
			+ MY_HOUSE_ADDRESS + " text, " + RESERVED_1 + " text);";

	private MapMyHouseDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		Log.v(TAG, "Database name: " + name + " Database version:" + version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(TAG, CREATE_TABLE_QUERY);
		db.execSQL(CREATE_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// onUpgrade(db, oldVersion, newVersion);
	}
	/*
	 * @param context context of the application
	 * 
	 * @return instance of MapMyHouseDBHelper class
	 */
	public static MapMyHouseDBHelper getInstance(Context context) {
		if (mMapMyHpuseDBHelper == null) {
			mMapMyHpuseDBHelper = new MapMyHouseDBHelper(context,
					DATABASE_NAME, null, DATABASE_VERSION);
		}
		return mMapMyHpuseDBHelper;
	}

	public void openDatabase() throws SQLiteException {
		mSQLiteDBWriteObject = mMapMyHpuseDBHelper.getWritableDatabase();
	}

	public void closeDatabse() {
		mMapMyHpuseDBHelper.close();
	}

	public long insert(MyLocationDao myLocationdao) {
		ContentValues values = new ContentValues();
		values.put(MY_HOUSE_ID, myLocationdao.getMyHouseID());
		values.put(MY_HOUSE_LATITUDE, myLocationdao.getMyLocationLatitude());
		values.put(MY_HOUSE_LONGITUDE, myLocationdao.getMyLocationLongitude());
		values.put(MY_HOUSE_ADDRESS, myLocationdao.getMyAddress());
		long insertID = mSQLiteDBWriteObject.insert(TABLE_NAME, "NULL", values);
		return insertID;
	}

	public MyLocationDao getMyLocation(String myHouseID) {

		MyLocationDao myLocationDao = null;
		String query = "select * from " + TABLE_NAME + " where " + MY_HOUSE_ID
				+ " = " + myHouseID;
		Cursor cursor = mSQLiteDBWriteObject.rawQuery(query, null);
		if (cursor != null) {
			cursor.moveToFirst();
			myLocationDao = cursorToMyLocationDao(cursor);
			cursor.close();
		}
		return myLocationDao;
	}

	private MyLocationDao cursorToMyLocationDao(Cursor cursor) {
		MyLocationDao myLocationDao = new MyLocationDao();
		myLocationDao.set_id(cursor.getInt(0));
		myLocationDao.setMyHouseID(cursor.getString(1));
		myLocationDao.setMyLocationLatitude(cursor.getDouble(2));
		myLocationDao.setMyLocationLongitude(cursor.getDouble(3));
		myLocationDao.setMyAddress(cursor.getString(4));
		myLocationDao.setReserver1(cursor.getString(5));
		return myLocationDao;
	}

}
