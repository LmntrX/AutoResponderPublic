package net.swierczynski.autoresponder.preferences;

import net.swierczynski.autoresponder.R;
import android.content.*;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.*;

public class UserPreferences extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	public static void registerPreferencesChangeListener(Context ctx, OnSharedPreferenceChangeListener listener) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
		preferences.registerOnSharedPreferenceChangeListener(listener);
	}
	
	public static boolean isIconInTaskbarSelected(Context ctx) {
		return UserPreferences.isOptionSelected(ctx, "ICON_IN_TASKBAR", true);
	}
	
	private static boolean isOptionSelected(Context ctx, String name, boolean defaultValue) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
		return preferences.getBoolean(name, defaultValue);
	}

	public static int getCallCountPrefs(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getInt("CALL_COUNT",1);
    }

    public static void setCallCountPrefs(Context ctx, int count){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("CALL_COUNT",count);
        editor.apply();
    }

    public static void writeCallCount(Context ctx, int count, String phoneNumber){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("CALL_COUNT_"+phoneNumber,count);
        editor.apply();
    }

    public static int readCallCount(Context ctx, String phoneNumber){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getInt("CALL_COUNT_"+phoneNumber,0);
    }
	
}
