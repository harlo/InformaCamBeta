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

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class SensorLogger<T> {
	public T _sucker;
	
	Timer mTimer = new Timer();
	TimerTask mTask;
	
	String tag;
	
	File mLog;
	JSONArray mBuffer;
	
	InformaService is;
	
	boolean isRunning;
		
	public interface OnSuckerUpdateListener {
		public void onSuckerUpdate(long timestamp, LogPack logPack);
	}
	
	public SensorLogger(InformaService is) {
		this.is = is;
		isRunning = true;
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
	
	public boolean getIsRunning() {
		return isRunning;
	}
	
	public JSONObject returnFromLog() {
		JSONObject logged = new JSONObject();
		
		return logged;
	}
	
	public LogPack forceReturn() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, JSONException {
		if(_sucker.getClass().getDeclaredMethod("forceReturn", null) != null) {
			Method fr = _sucker.getClass().getDeclaredMethod("forceReturn", null);
			return (LogPack) fr.invoke(_sucker, null);
		}
		
		return null;
	}

	public void sendToBuffer(LogPack logPack) throws JSONException {
		((OnSuckerUpdateListener) is).onSuckerUpdate(System.currentTimeMillis(), logPack); 
	}
}
