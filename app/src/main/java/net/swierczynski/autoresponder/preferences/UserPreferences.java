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

    public static void writeLastNotifiedNumber(Context ctx, String phoneNumber) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Last_Notified",phoneNumber);
        editor.apply();
    }

    public static String readLastNotifiedNumber(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString("Last_Notified","-1");
    }

    public static void saveAutoRespondToTextsState(Context ctx, boolean enabled) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Auto_Respond_To_Texts_State",enabled);
        editor.apply();
    }

    public static boolean readAutoRespondToTextsState(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getBoolean("Auto_Respond_To_Texts_State",false);
    }

    public static void saveAutoRespondToMissedCalls(Context ctx, boolean enabled) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Auto_Respond_To_Missed_Calls",enabled);
        editor.apply();
    }

    public static boolean readAutoRespondToMissedCalls(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getBoolean("Auto_Respond_To_Missed_Calls",true);
    }

    public static void notAnyMore(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("First_Open",false);
        editor.apply();
    }

    public static boolean isItFirstTime(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getBoolean("First_Open",true);
    }
}
