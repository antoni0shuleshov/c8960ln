package net.kaasnake.c8960.service;

import java.util.HashSet;
import java.util.Set;

import net.kaasnake.Logger;
import net.kaasnake.c8960.ledcontrol.Led;
import net.kaasnake.c8960.ledcontrol.LedControl;
import net.kaasnake.c8960.ledcontrol.LedsEnum;
import net.kaasnake.c8960.lednotifications.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.preference.PreferenceManager;

public class LedNotificationService extends Service {

	private MissedEventContentObserver missedCALLS = null;
	private MissedEventContentObserver missedSMS = null;
	private StateManager stateManager = null;
	public static String PHONE_STATE_SERVICE = "PHONE_STATE_SERVICE";
	
	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.Log("Service", "onStartCommand");
		stateManager = new StateManager(getApplicationContext());

		missedCALLS = new MissedEventContentObserver(new Handler(),
				this.getApplicationContext(),android.provider.CallLog.Calls.CONTENT_URI, new MissedEventCallBack() {
					@Override
					public void onCallBack() {
						Logger.Log("CALL", "call observer callback");
						setLedState();
					}
				});
		missedSMS = new MissedEventContentObserver(new Handler(),
				this.getApplicationContext(), Uri.parse("content://mms-sms/conversations"), new MissedEventCallBack() {
					@Override
					public void onCallBack() {
						Logger.Log("SMS", "sms observer callback");
						setLedState();
					}
				});
		setLedState();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		Logger.Log("Service", "onBind");
		return mBinder;
	}

	// public static BroadcastReceiver reciever=new ScreenStateReciever();

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.Log("Service", "onCreate");		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.Log("Service", "onDestroy");
		missedCALLS.unRegisterObserver();
		missedSMS.unRegisterObserver();
		LedControl.LedsOff();
	}

	private LedsEnum getLed(int prefEventID) {
		LedsEnum res = LedsEnum.NULL;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String ledAssignedPrefs = prefs.getString(getApplicationContext()
				.getResources().getString(prefEventID), "NULL");
		try {
			res = LedsEnum.valueOf(ledAssignedPrefs);
		} catch (Exception e) {
		}
		return res;
	}

	private void setLedState() {
		boolean hasMissedEvent = false;
		LedControl.LedsOff();
		Set<LedsEnum> res = new HashSet<LedsEnum>();
		if (stateManager.hasMissedCalls()) {
			res.add(getLed(R.string.missed_call_led));
			hasMissedEvent = true;
			missedCALLS
					.registerObserver();
		} else {
			missedCALLS.unRegisterObserver();
		}
		//if (true){
		if (stateManager.hasMissedSMS()) {
			res.add(getLed(R.string.missed_sms_led));
			hasMissedEvent = true;
			missedSMS.registerObserver();
		} else {

			missedSMS.unRegisterObserver();
		}
		if (hasMissedEvent) {
			for (LedsEnum led : res) {
				Led.switchOn(led);
			}
		} else {
			this.stopSelf();
		}
	}

	public static boolean isMyServiceRunning(Context cont) {
		ActivityManager manager = (ActivityManager) cont
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (LedNotificationService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
