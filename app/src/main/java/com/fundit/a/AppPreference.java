package com.fundit.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Nivida new on 17-Jul-17.
 */

public class AppPreference {

    public static final String FILE_NAME="FundItPref";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    int messageCount = 0;

    public AppPreference(Context context) {
        preferences=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        editor=preferences.edit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("loggedIn",false);
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean("loggedIn",loggedIn).commit();
    }

    public String getUserID() {
        return preferences.getString("userID","");
    }

    public void setUserID(String userID) {
        editor.putString("userID",userID).commit();
    }

    public String getUserRoleID() {
        return preferences.getString("roleID","");
    }

    public void setUserRoleID(String userRoleID) {
        editor.putString("roleID",userRoleID).commit();
    }

    public String getTokenHash() {
        return preferences.getString("tokenHash","");
    }

    public void setTokenHash(String tokenHash) {
        editor.putString("tokenHash",tokenHash).commit();
    }

    public String getUserData() {
        return preferences.getString("userData","");
    }

    public void setUserData(String userData) {
        editor.putString("userData", userData).commit();
    }

    public String getMemberData() {
        return preferences.getString("memberData","");
    }

    public void setMemberData(String memberData) {
        editor.putString("memberData", memberData).commit();
    }

    public int getMessageCount() {
        return preferences.getInt("Count" , 0);
    }

    public void setMessageCount(int messageCount) {
        editor.putInt("Count" , messageCount).commit();
    }

    public boolean isSkiped() {
        return preferences.getBoolean("skiped" , false);
    }

    public void setSkiped(boolean skiped) {
        editor.putBoolean("skiped" , skiped).commit();
    }


    public void clearData(Context context){
        SharedPreferences.Editor prefs = context.getSharedPreferences(FILE_NAME , 0).edit();
        prefs.clear();
        prefs.commit();
        Log.e("Congo" , "Clear All Data");
    }
}
