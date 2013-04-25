package net.kaasnake.c8960.service;

import net.kaasnake.Logger;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class StateManager {
	
	private Context context;
	
	public StateManager(Context context){
		this.context = context;
	}
	public Boolean hasMissedSMS() {
		final Uri SMS_INBOX = Uri.parse("content://sms/inbox");

		Cursor c = context.getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
		int unreadMessagesCount = c.getCount();

		c.deactivate();
		Logger.Log("hasMissedSMS","Missed SMS count: " + unreadMessagesCount);
		return unreadMessagesCount > 0;
	}
	public Boolean hasMissedCalls() {
		
		final String[] projection = null;		
		final String selection = "";//android.provider.CallLog.Calls.DATE+">="+lastQueryTime;// 
		final String[] selectionArgs = null;
		final String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
		int MISSED_CALL_TYPE = android.provider.CallLog.Calls.MISSED_TYPE;
		Cursor cursor = null;
		try{
		    cursor = context.getContentResolver().query(
		            Uri.parse("content://call_log/calls"),
		            projection,
		            selection,
		            selectionArgs,
		            sortOrder);
		    while (cursor.moveToNext()) { 
		    	String callNumber = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
		        String callType = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
		        String isCallNew = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NEW));
		        if(Integer.parseInt(callType) == MISSED_CALL_TYPE && Integer.parseInt(isCallNew) > 0){
		            Logger.Log("hasMissedCalls","Missed Call Found: " + callNumber);
		            return true;
		        }else{
		        	return false;
		        }
		        	
		    }
		}catch(Exception ex){
		    Logger.Log("LN","ERROR: " + ex.toString(),"e");
		}finally{
		    cursor.close();
		}
		return false;
	}
}
