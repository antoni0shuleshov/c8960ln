package net.kaasnake.c8960.service;

import net.kaasnake.Logger;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class MissedEventContentObserver extends ContentObserver {

	/* (non-Javadoc)
	 * @see android.database.ContentObserver#deliverSelfNotifications()
	 */
	@Override
	public boolean deliverSelfNotifications() {
		return true;
	}

	protected MissedEventCallBack callback;

	private boolean registered;
	private Context context;
	private Uri uri;
	public MissedEventContentObserver(Handler handler, Context cont, Uri uri,
			MissedEventCallBack callback) {
		super(handler);
		this.callback = callback;
		this.context = cont;
		this.uri = uri;
	}

	public void registerObserver() {
		registered = true;
		Logger.Log("registerObserver",this.uri.toString());
		context.getContentResolver().registerContentObserver(this.uri, true, this);
	}

	public void unRegisterObserver() {	
		if (registered)
			context.getContentResolver().unregisterContentObserver(this);
		registered = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.ContentObserver#onChange(boolean)
	 */
	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		this.callback.onCallBack();
	}

}
