package net.kaasnake.c8960.service;

import net.kaasnake.Logger;
import net.kaasnake.c8960.ledcontrol.LedControl;
import net.kaasnake.c8960.lednotifications.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootListener extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Logger.Log("boot","service val"+prefs
				.getBoolean(
						context.getResources()
								.getString(R.string.service_state), false));
		if (prefs
				.getBoolean(
						context.getResources()
								.getString(R.string.service_state), false)) {
			Logger.Log("boot","service starting");
			LedControl.initControl();			
			Intent mI = new Intent(LedNotificationService.PHONE_STATE_SERVICE);
			context.startService(mI);			
		}
	}
}