/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dasari.android.maps.mapmyhouse.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.dasari.android.maps.mapmyhouse.R;
import com.dasari.android.maps.mapmyhouse.interfaces.ILocationChangeListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class LocationManager implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	// A request to connect to Location Services
	private LocationRequest mLocationRequest;

	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;

	private static LocationManager mLocationManager;
	// Handle to SharedPreferences for this app
	SharedPreferences mPrefs;

	// Handle to a SharedPreferences editor
	SharedPreferences.Editor mEditor;
	private ILocationChangeListener mLocationListener;
	/*
	 * Note if updates have been turned on. Starts out as "false"; is set to
	 * "true" in the method handleRequestSuccess of LocationUpdateReceiver.
	 */
	boolean mUpdatesRequested = false;

	public static LocationManager getInstance() {
		if (mLocationManager == null) {
			mLocationManager = new LocationManager();
		}

		return mLocationManager;
	}

	public void initialize(Context context) {
		mLocationRequest = LocationRequest.create();

		/*
		 * Set the update interval
		 */
		mLocationRequest
				.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Set the interval ceiling to one minute
		mLocationRequest
				.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		// Note that location updates are off until the user turns them on
		mUpdatesRequested = false;
		mPrefs = context.getSharedPreferences(LocationUtils.SHARED_PREFERENCES,
				Context.MODE_PRIVATE);

		// Get an editor
		mEditor = mPrefs.edit();

		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(context, this, this);
	}

	public void registerLocationListener(ILocationChangeListener listener) {
		mLocationListener = listener;
	}

	public void unRegisterLocationListener() {
		mLocationListener = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		mLocationListener.onLocationChange(location);
	}

	@Override
	public void onConnected(Bundle args) {
        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
        if(mLocationListener != null)
        {
        	mLocationListener.onServiceConnected(args);
        }
	}

	@Override
	public void onDisconnected() {
		mLocationListener.onServiceDisconnected();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		mLocationListener.onServiceConnectionFailed(arg0);
	}
	
	public void connectToLocationService()
	{
        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mLocationClient.connect();
	}
	
    /**
     * Invoked by the "Get Location" button.
     *
     * Calls getLastLocation() to get the current location
     *
     * @param context The view object associated with this method, in this case a Button.
     */
    public void getLocation(Context context) {

        // If Google Play Services is available
        if (servicesConnected(context)) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();
            if(currentLocation != null)
            {
            	mLocationListener.onLocationChange(currentLocation);
            }
        }
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected(Context context) {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, context.getString(R.string.play_services_available));

            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            return false;
        }
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    public void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    public void stopPeriodicUpdates() {
    	
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }

        disconnectToService();
    }
    
    private void disconnectToService()
    {
        // After disconnect() is called, the client is considered "dead".
        mLocationClient.disconnect();
    }
}
