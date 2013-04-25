package net.kaasnake.c8960.lednotifications;

import net.kaasnake.c8960.ledcontrol.LedControl;
import net.kaasnake.c8960.ledcontrol.LedsEnum;
import net.kaasnake.c8960.service.LedNotificationService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Resources r = getResources();
		processPreference(R.string.missed_call_led);
		processPreference(R.string.missed_sms_led);
		processPreference(R.string.missed_email_led);
		CheckBoxPreference serviceSwitch = (CheckBoxPreference) findPreference(r.getString(R.string.service_state));
		serviceSwitch
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if ((Boolean)newValue){
							getActivity().startService(new Intent(LedNotificationService.PHONE_STATE_SERVICE));
						}else{							
							getActivity().stopService(new Intent(LedNotificationService.PHONE_STATE_SERVICE));
						}							
						return true;
					}
				});
		Preference offLeds = (Preference)findPreference(r.getString(R.string.off_leds));
		offLeds.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				LedControl.LedsOff();
				return false;
			}
		});
		/*
		Preference testBtn = (Preference)findPreference("test");
		testBtn.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				PropertyPermission permission = 
				         new PropertyPermission("java.*", "write");
				PermissionCollection permissions = permission.newPermissionCollection();
				permissions.add(permission);
				String s = permissions.implies(new PropertyPermission(LedsEnum.BLUE.getSysfsPath(), "write"))?"yes":"no";
				Toast.makeText(getActivity(), s, 100).show();
				return false;
			}
		});
		*/
	}
	
	private void setSummary(ListPreference lp){
		setSummary(lp, lp.getValue());		
	}
	
	private void setSummary(ListPreference lp, String newVal){
		LedsEnum currLed = LedsEnum.NULL;
		try {
			currLed = LedsEnum.valueOf(newVal);
		} catch (Exception e) {
		}
		lp.setSummary(getString(R.string.pref_selected_summary)+" "+currLed.getCaption(getResources()));
	}
	
	private void processPreference(int prefId){
		Resources r = getResources();
		final ListPreference lp = (ListPreference)findPreference(r.getString(prefId));
		lp.setEntries(LedsEnum.getTitles(getResources()));
		lp.setEntryValues(LedsEnum.getValues());
		lp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				setSummary(lp, (String)newValue);
				return true;
			}
		});
		setSummary(lp);
	}
	
}
