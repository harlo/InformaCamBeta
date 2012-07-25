package org.witness.informacam.informa;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.witness.informacam.crypto.SignatureUtility;
import org.witness.informacam.utils.Constants.Suckers;

import android.content.Context;

public class SensorLogger<T> {
	public T _sucker;
	
	Timer mTimer = new Timer();
	TimerTask mTask;
	
	String tag;
	
	File mLog;
	JSONArray mBuffer;
	
	boolean isRunning, canBeCleared;
	
	public static Context _c;
	
	public SensorLogger(Context c) {
		_c = c;
		mBuffer = new JSONArray();
		isRunning = true;
		canBeCleared = true;
	}
	
	public T getSucker() {
		return _sucker;
	}
	
	public void setSucker(T sucker) {
		_sucker = sucker;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String name) {
		this.tag = name;
	}
	
	public JSONArray getLog() {
		return mBuffer;
	}

	public Timer getTimer() {
		return mTimer;
	}
	
	public TimerTask getTask() {
		return mTask;
	}
	
	public void setTask(TimerTask task) {
		mTask = task;
	}
	
	public void setIsRunning(boolean b) {
		isRunning = b;
		if(!b)
			mTimer.cancel();
	}
	
	public void lockLog() {
		canBeCleared = false;
	}
	
	public void unlockLog() {
		canBeCleared = true;
	}
	
	public boolean getIsRunning() {
		return isRunning;
	}
	
	public JSONObject returnFromLog() {
		JSONObject logged = new JSONObject();
		
		return logged;
	}
	
	public JSONObject returnCurrent() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, JSONException {
		JSONObject current = new JSONObject();
		// TODO: sign this data and append signature? maybe...
		
		if(_sucker.getClass().getDeclaredMethod("forceReturn", null) != null) {
			Method fr = _sucker.getClass().getDeclaredMethod("forceReturn", null);
			current = (JSONObject) fr.invoke(_sucker, null);
			current.put(Suckers.Keys.SIGNATURE, SignatureUtility.getInstance().signData(current.toString().getBytes()));
		} else {
			current = null;
		}
		
		return current;
	}

	public void sendToBuffer(JSONObject logItem) throws JSONException {
		if(mBuffer.length() > 100 && canBeCleared) {
			mBuffer = null;
			mBuffer = new JSONArray();
		}
		
		logItem.put(Suckers.Keys.TIMESTAMP, System.currentTimeMillis());
		mBuffer.put(logItem);
		//Log.d(InformaConstants.SUCKER_TAG, "logged: " + logItem.toString());
	}
	
	public JSONObject jPack(String key, Object val) throws JSONException {
		JSONObject item = new JSONObject();
		
		try {
			item.put(key, val.toString());
		} catch(NullPointerException e) {
			item.put(key, "");
		}

		return item;
	}
}
