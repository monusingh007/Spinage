package com.spinages.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PrefManager

{



    // Shared pref file name
    private static final String PREF_NAME = "monusingh";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public void createMessageBuffer(String key, String message) {


        editor.putString(key, message);

        // commit changes
        editor.commit();
    }

    public String getPreviousMessage(String key) {
        try {
            return pref.getString(key, "null");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "null";
    }

    public void clearKeyMessage(String key) {
        SharedPreferences settings =_context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearAllData() {
        editor.clear();
        editor.commit();
    }

    public void saveArraylist(ArrayList<String> arrayList) {
        Set<String> set = new HashSet<String>();
        set.addAll(arrayList);
        editor.putStringSet("message_array", set);
        editor.commit();

    }

    public ArrayList<String> getArrayList() {
        try {
            Set<String> set = pref.getStringSet("message_array", null);
            ArrayList<String> sample = new ArrayList<String>(set);

            return sample;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}