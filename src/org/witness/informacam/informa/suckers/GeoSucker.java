package org.witness.informacam.informa.suckers;

import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.witness.informacam.informa.SensorLogger;
import org.witness.informacam.utils.Constants.Suckers;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

@SuppressWarnings("rawtypes")
public class GeoSucker extends SensorLogger implements LocationListener {
	LocationManager lm;
	Criteria criteria;
	
	@SuppressWarnings("unchecked")
	public GeoSucker(Context c) {
		super(c);
		setSucker(this);
		
		lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		
		if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
		
		setTask(new TimerTask() {

			@Override
			public void run() throws NullPointerException {
				if(getIsRunning()) {
					double[] loc = updateLocation();
					try {
						if (loc != null)
							sendToBuffer(jPack(Suckers.Geo.Keys.GPS_COORDS, "[" + loc[0] + "," + loc[1] + "]"));
					} catch (JSONException e) {
						Log.e(Suckers.LOG,"location json error",e);
					} catch(NullPointerException e) {
						Log.e(Suckers.LOG, "location NPE", e);
					}
				}
			}
		});
		
		getTimer().schedule(getTask(), 0, Suckers.Geo.LOG_RATE);
	}
	
	public JSONObject forceReturn() {
		double[] loc = updateLocation();
		try {
			if (loc != null)
				return jPack(Suckers.Geo.Keys.GPS_COORDS, "[" + loc[0] + "," + loc[1] + "]");
			else
				return null;
		} catch(JSONException e){
			return null;
		}
	}
	
	private double[] updateLocation() {
		try {
			String bestProvider = lm.getBestProvider(criteria, false);
			Location l = lm.getLastKnownLocation(bestProvider);
			
			if (l != null)
				return new double[] {l.getLatitude(),l.getLongitude()};
			else
				return null;
			
		} catch(NullPointerException e) {
			Log.e(Suckers.LOG,"location NPE", e);
			return null;
		} catch(IllegalArgumentException e) {
			Log.e(Suckers.LOG, "location illegal arg",e);
			return null;
		}
	}
	
	public void stopUpdates() {
		setIsRunning(false);
		lm.removeUpdates(this);
		Log.d(Suckers.LOG, "shutting down GeoSucker...");
	}

	@Override
	public void onLocationChanged(Location location) {}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}	
}
