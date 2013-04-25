package net.kaasnake.c8960.service;

import net.kaasnake.Logger;
import net.kaasnake.c8960.lednotifications.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

public class DeviceStateReciever extends BroadcastReceiver {

	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context cont, Intent intent) {
		Logger.Log("LN DeviceStateReciever", intent.getAction());
		Logger.Log("LN DeviceStateReciever cont", cont.getPackageName().toString());
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(cont);
		Boolean r = prefs.getBoolean(
				cont.getResources().getString(R.string.service_state), false);
		/*if (prefs.getBoolean(
				cont.getResources().getString(R.string.service_state), false))*/
		Logger.Log("LN DeviceStateReciever","started"+ r.toString());
		String action = intent.getAction(); 
		if (r)
			if (action == TelephonyManager.ACTION_PHONE_STATE_CHANGED
					|| action == ACTION_SMS_RECEIVED
					/*
					|| action == Intent.ACTION_POWER_CONNECTED
					|| action == Intent.ACTION_POWER_DISCONNECTED
					|| action == Intent.ACTION_BATTERY_CHANGED
					*/) {
				Logger.Log("LN DeviceStateReciever", "wait");
				// wait until event be written to db
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Logger.Log("LN DeviceStateReciever", "continue");
				Intent mI = new Intent(
						LedNotificationService.PHONE_STATE_SERVICE);
				Logger.Log("LN DeviceStateReciever", "starting service");
				
				cont.startService(mI);
			}
	}
}
