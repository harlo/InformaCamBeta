package org.witness.informacam.informa;

import org.witness.informacam.R;
import org.witness.informacam.app.MainActivity;
import org.witness.informacam.crypto.SignatureUtility;
import org.witness.informacam.utils.Constants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class InformaService extends Service {
	public static InformaService informaService;
	private final IBinder binder = new LocalBinder();
	
	NotificationManager nm;
	
	Intent toMainActivity;
	private String informaCurrentStatusString;
	private int informaCurrentStatus;
	
	private SignatureUtility signatureService;
	
	public class LocalBinder extends Binder {
		public InformaService getService() {
			return InformaService.this;
		}
	}
	
	
	public void setCurrentStatus(int status) {
		informaCurrentStatus = status;
		informaCurrentStatusString = getResources().getStringArray(R.array.informa_statuses)[informaCurrentStatus];
		showNotification();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	public static InformaService getInstance() {
		return informaService;
	}
	
	public int getStatus() {
		return informaCurrentStatus;
	}
	
	@Override
	public void onCreate() {
		Log.d(Constants.Informa.LOG, "InformaService running");
		
		toMainActivity = new Intent(this, MainActivity.class);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		
		
		informaService = this;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(Constants.Informa.LOG, "InformaService stopped");
	}
	
	@SuppressWarnings("deprecation")
	public void showNotification() {
		Notification n = new Notification(
				R.drawable.ic_ssc,
				getString(R.string.app_name),
				System.currentTimeMillis());
		
		PendingIntent pi = PendingIntent.getActivity(
				this,
				Constants.Informa.FROM_NOTIFICATION_BAR, 
				toMainActivity,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		n.setLatestEventInfo(this, getString(R.string.app_name), informaCurrentStatusString, pi);
		nm.notify(R.string.app_name_lc, n);
	}

}