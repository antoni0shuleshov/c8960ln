package net.kaasnake.c8960.ledcontrol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

import net.kaasnake.Logger;

public class LedControl {

	static final boolean useDummy = false;

	static boolean testLed(String colorFile) {
		int state = getState(colorFile);
		switchLed(colorFile, state);
		int state2 = getState(colorFile);
		return state2 == state;
	}
	
	
	public static void LedsOff(){
		for (LedsEnum led : LedsEnum.values())
			Led.switchOff(led);
	}
	
	static void switchLed(String colorFile, int brightness) {
		if (LedControl.useDummy) {
			LedControlDummy.switchLed(colorFile, brightness);
			return;
		} else {

			PrintWriter outStream = null;
			String sbrightness = Integer.toString(brightness);
			try {
				FileOutputStream fos = new FileOutputStream(colorFile);
				outStream = new PrintWriter(new OutputStreamWriter(fos));
				outStream.print(sbrightness);
				Logger.Log("switchLed", "writing " + sbrightness + " to " + colorFile);
			} catch (Exception e) {
				Logger.Log("switchLed", "Error" + e.getLocalizedMessage(), "e");
			} finally {
				if (outStream != null)
					outStream.close();
			}
		}
	}

	static int getState(String colorFile) {
		if (LedControl.useDummy) {
			return LedControlDummy.getState(colorFile);
			// return 0;
		} else {
			BufferedReader inStream = null;
			try {
				FileInputStream fis = new FileInputStream(colorFile);
				Reader reader = new InputStreamReader(fis);
				int res = 0;
				char[] r = new char[3];
				reader.read(r);
				reader.close();
				try {
					res = Integer.parseInt(new String(r));
				} catch (NumberFormatException e) {
					res = 0;
				}
				Logger.Log("getState", colorFile+"=" + res);
				if (res >= 0)
					return res;
				else
					return 0;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (inStream != null)
					try {
						inStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

			return 0;
		}
	}

	public static void initControl() {
		if (LedControl.useDummy) {
			LedControlDummy.initControl();
			return;
		} else {
			
			Process p;
			try {
				// Preform su to get root privledges
				p = Runtime.getRuntime().exec("su");

				// Attempt to write a file to a root-only
				DataOutputStream os = new DataOutputStream(p.getOutputStream());
				os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");

				// Close the terminal
				os.writeBytes("exit\n");
				os.flush();
				try {
					p.waitFor();
					if (p.exitValue() != 255) {
						boolean okTest = false;
						for (LedsEnum led : LedsEnum.values())
							okTest |= testLed(led.getSysfsPath());
						if (okTest)
							for (LedsEnum led : LedsEnum.values()) {
								Runtime.getRuntime()
										.exec("su -c chmod 777 "
												+ led.getSysfsPath());
							}
						Logger.Log("root", "root OK");
					} else {
						// TODO Code to run on unsuccessful
						Logger.Log("root", "not root", "e");
					}
				} catch (InterruptedException e) {
					// TODO Code to run in interrupted exception
					Logger.Log("root", "root error", "e");
				}
			} catch (IOException e) {
				// TODO Code to run in input/output exception
				Logger.Log("root", "root error 2", "e");
			}
			
		}
	}
}
