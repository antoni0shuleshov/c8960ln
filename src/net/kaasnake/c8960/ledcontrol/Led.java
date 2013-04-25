package net.kaasnake.c8960.ledcontrol;

public class Led{
	private LedsEnum led;
	public Led(LedsEnum led){
		this.led = led;
	}
	public void switchOn(){
		LedControl.switchLed(led.getSysfsPath(), led.getOnValue());
	}
	
	public void switchOff(){
		LedControl.switchLed(led.getSysfsPath(), led.getOffValue());
	}
	
	public int getState(){
		return LedControl.getState(led.getSysfsPath());
	}
	
	public boolean isOn(){
		return LedControl.getState(led.getSysfsPath())>0;
	}
	
	public static void switchOn(LedsEnum leds){
		Led led = new Led(leds);
		led.switchOn();
	}
	public static void switchOff(LedsEnum leds){
		Led led = new Led(leds);
		led.switchOff();
	}
	
	public static int getState(LedsEnum led){
		return LedControl.getState(led.getSysfsPath());
	}
	
	public static boolean isOn(LedsEnum led){
		return LedControl.getState(led.getSysfsPath())>0;
	}
}