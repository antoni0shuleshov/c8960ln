package net.kaasnake.c8960.ledcontrol;

import android.content.res.Resources;
import net.kaasnake.c8960.lednotifications.R;

public enum LedsEnum{
	BLUE("/sys/class/leds/BLUE_LED/brightness",0,255, R.string.blueLedCaption),
	RED("/sys/class/leds/RED_LED/brightness",0,255, R.string.redLedCaption),
	NULL("/dev/null",0,255,R.string.noLedCaption);
	/* 
	 * KEYS("/sys/class/leds/KP_backlight/brightness",0,255, R.string.keysLedCaption); 
	 * */
	
	private String sysfspath;
	private int caption;
	private int offval, onval;
	
	public String getSysfsPath(){
		return this.sysfspath;
	}
	public int getOnValue(){return this.onval;}
	public int getOffValue(){return this.offval;}
	
	LedsEnum(String path, int offValue, int onValue, int captionResource){
		this.sysfspath = path;
		this.offval = offValue;
		this.onval = onValue;
		this.caption = captionResource;
	}
	public static CharSequence[] getValues(){
		LedsEnum[] leds = LedsEnum.values();
		CharSequence[] res = new String[leds.length];
		for (int i=0;i<leds.length;i++){
			res[i]=leds[i].toString();
		}
		return res;
	}
	
	public String getCaption(Resources resources){
		return resources.getString(this.caption);
	}
	
	public static CharSequence[] getTitles(Resources resources){
		LedsEnum[] leds = LedsEnum.values();
		CharSequence[] res = new String[leds.length];
		for (int i=0;i<leds.length;i++){
			res[i]=leds[i].getCaption(resources);
		}
		return res;
	}
}
