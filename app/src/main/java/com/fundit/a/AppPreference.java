package com.fundit.a;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nivida new on 17-Jul-17.
 */

public class AppPreference {

    public static final String FILE_NAME="FundItPref";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean loggedIn=false;
    String userID="";
    String userRoleID="";
    String tokenHash="";
    String userData="";
    String memberData = "";

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
}
