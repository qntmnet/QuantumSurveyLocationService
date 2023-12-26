package com.qntm.qntm_survey.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {

    private static final String PUSH_KEY = "push_key";


    public SharedPreferences pref;


    public PreferencesUtils(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setFCMPushKey(Context context, String push_key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(PUSH_KEY, push_key);
        editor.apply();
    }

    public static String getFCMPushKey(Context context) {
        SharedPreferences savedSession = PreferenceManager.getDefaultSharedPreferences(context);
        return savedSession.getString(PUSH_KEY, null);
    }
}
