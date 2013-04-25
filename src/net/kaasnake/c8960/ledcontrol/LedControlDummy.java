package net.kaasnake.c8960.ledcontrol;

import net.kaasnake.Logger;

public class LedControlDummy {
	
    static void switchLed(String colorFile, int brightness) {
        Logger.Log("dummy","switchLed("+colorFile+""+brightness+")");   
    }
    
    static int getState(String colorFile){
    	Logger.Log("dummy","getState("+colorFile+")");
    	return 0;
    }
    public static void initControl(){
    	Logger.Log("dummy","initControl()");
    }
}