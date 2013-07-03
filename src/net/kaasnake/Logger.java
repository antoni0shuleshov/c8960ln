package net.kaasnake;

import android.util.Log;

public class Logger {

	private static final boolean logEnabled = false;
	private static final String prefix = "C8690LN";
	public static void Log(String suffix, String msg, String mode){
		if (Logger.logEnabled){
			if (mode.equals("v")){
				Log.v(prefix+" "+suffix, msg);
			}else if (mode.equals("e")){
				Log.e(prefix+" "+suffix, msg);
			}
		}
	}
	
	public static void Log(String suffix, String msg){
		Log(suffix, msg, "v");
	}
}
