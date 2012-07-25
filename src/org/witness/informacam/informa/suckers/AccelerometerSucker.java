package org.witness.informacam.informa.suckers;

import java.util.List;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.witness.informacam.informa.SensorLogger;
import org.witness.informacam.utils.Constants.Suckers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;


import android.util.Log;

@SuppressWarnings("rawtypes")
public class AccelerometerSucker extends SensorLogger implements SensorEventListener {
	SensorManager sm;
	List<Sensor> availableSensors;
	boolean hasAccelerometer, hasOrientation, hasLight, hasMagneticField;
	JSONObject currentAccelerometer, currentLight, currentMagField;
			
	@SuppressWarnings("unchecked")
	public AccelerometerSucker(Context c) {
		super(c);
		setSucker(this);
		
		sm = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		availableSensors = sm.getSensorList(Sensor.TYPE_ALL);
		
		currentAccelerometer = currentLight = currentMagField = new JSONObject();
		
		for(Sensor s : availableSensors) {
			switch(s.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				hasAccelerometer = true;
				sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
				break;
			case Sensor.TYPE_LIGHT:
				sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
				hasLight = true;
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
				hasOrientation = true;
				break;
			}
				
		}
		
		setTask(new TimerTask() {
			
			@Override
			public void run() {
				try {
					if(hasAccelerometer)
						readAccelerometer();
					if(hasLight)
						readLight();
					if(hasOrientation)
						readOrientation();
				} catch(JSONException e){}
			}
		});
		
		getTimer().schedule(getTask(), 0, Suckers.Accelerometer.LOG_RATE);
	}
	
	private void readAccelerometer() throws JSONException {
		sendToBuffer(currentAccelerometer);
	}
	
	private void readOrientation() throws JSONException {
		sendToBuffer(currentMagField);
	}
	
	private void readLight() throws JSONException {
		sendToBuffer(currentLight);
	}
	
	public JSONObject forceReturn() {
		try {
			JSONObject fr = new JSONObject();
			fr.put(Suckers.Accelerometer.Keys.ACC, currentAccelerometer);
			fr.put(Suckers.Accelerometer.Keys.ORIENTATION, currentMagField);
			fr.put(Suckers.Accelerometer.Keys.LIGHT, currentLight);
			return fr;
		} catch(JSONException e) {
			return null;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized(this) {
			if(getIsRunning()) {
				JSONObject sVals = new JSONObject();
				
				try {				
					switch(event.sensor.getType()) {
					case Sensor.TYPE_ACCELEROMETER:
						float[] acc = event.values.clone();
						sVals.put(Suckers.Accelerometer.Keys.X, acc[0]);
						sVals.put(Suckers.Accelerometer.Keys.Y, acc[1]);
						sVals.put(Suckers.Accelerometer.Keys.Z, acc[2]);
						currentAccelerometer = sVals;
						break;
					case Sensor.TYPE_MAGNETIC_FIELD:
						float[] geoMag = event.values.clone();
						sVals.put(Suckers.Accelerometer.Keys.AZIMUTH, geoMag[0]);
						sVals.put(Suckers.Accelerometer.Keys.PITCH, geoMag[1]);
						sVals.put(Suckers.Accelerometer.Keys.ROLL, geoMag[2]);
						currentMagField = sVals;
						break;
					case Sensor.TYPE_LIGHT:
						sVals.put(Suckers.Accelerometer.Keys.LIGHT_METER_VALUE, event.values[0]);
						currentLight = sVals;
						break;
					}
					
					//sendToBuffer(sVals);
				} catch(JSONException e) {}
			}
		}
	}
	
	public void stopUpdates() {
		setIsRunning(false);
		sm.unregisterListener(this);
		Log.d(Suckers.LOG, "shutting down AccelerometerSucker...");
	}
}