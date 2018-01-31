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


    public int getCampaignCount() {
        return preferences.getInt("campaignCount" , 0);
    }

    public void setCampaignCount(int campaignCount) {
        editor.putInt("campaignCount" , campaignCount).commit();
    }

    public int getMemberCount() {
        return preferences.getInt("memberCount" , 0);
    }

    public void setMemberCount(int memberCount) {
        editor.putInt("memberCount" , memberCount).commit();
    }

    public int getTotalCount() {
        return preferences.getInt("totalCount" , 0);
    }

    public void setTotalCount(int totalCount) {
        editor.putInt("totalCount" , totalCount).commit();
    }

    public int getCouponCount() {
        return preferences.getInt("CouponCount" , 0);
    }

    public void setCouponCount(int CouponCount) {
        editor.putInt("CouponCount" , CouponCount).commit();
    }




    public int getfundspot_product_count() {
        return preferences.getInt("fundspot_product_count" , 0);
    }

    public void setfundspot_product_count(int fundspot_product_count) {
        editor.putInt("fundspot_product_count" , fundspot_product_count).commit();
    }

    public boolean isFirstTime() {
        return preferences.getBoolean("firstTime" , false);
    }

    public void setFirstTime(boolean firstTime) {
        editor.putBoolean("firstTime" , firstTime).commit();
    }

    public boolean isMemberTimes() {
        return preferences.getBoolean("MemberTimes" , false);
    }

    public void setMemberTimes(boolean MemberTimes) {
        editor.putBoolean("MemberTimes" , MemberTimes).commit();
    }

    public void clearData(Context context){
        SharedPreferences.Editor prefs = context.getSharedPreferences(FILE_NAME , 0).edit();
        prefs.clear();
        prefs.commit();
        Log.e("Congo" , "Clear All Data");
    }
}
