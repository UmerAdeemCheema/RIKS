package com.ua.riks;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelper {
    static final String userIdKey = "USERIDKEY";
    static final String userEmailKey = "USEREMAILKEY";
    static final String userNameKey = "USERNAMEKEY";
    static final String displayNameKey = "USERDISPLAYNAMEKEY";
    static final String userCNICKey = "USERCNICKEY";
    static final String userAddressKey = "USERADDRESSKEY";
    static final String userContactKey = "USERCONTACTNOKEY";
    static final String userTypeKey = "USERTYPEKEY";
    static final String userProfileKey = "USERPROFILEKEY";
    static final String preferenceName = "RIKS_SH_PREF";

    public static void atLogin(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putInt(userIdKey, User.id);
        editor.putString(userEmailKey, User.email);
        editor.putString(userNameKey, User.username);
        editor.putString(displayNameKey, User.displayname);
        editor.putString(userCNICKey, User.cnic);
        editor.putString(userAddressKey, User.address);
        editor.putString(userContactKey, User.contactno);
        editor.putString(userTypeKey, User.type);
        editor.putString(userProfileKey, User.profilepicname);
        editor.apply();
    }

    public static void atLogout(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putInt(userIdKey, -1);
        editor.putString(userEmailKey, "");
        editor.putString(userNameKey, "");
        editor.putString(displayNameKey, "");
        editor.putString(userCNICKey, "");
        editor.putString(userAddressKey, "");
        editor.putString(userContactKey, "");
        editor.putString(userTypeKey, "");
        editor.putString(userProfileKey, "");
        editor.apply();
    }

    public static void atStart(Context context){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
        User.userId =  prefs.getInt(userIdKey, -1);
        User.email = prefs.getString(userEmailKey, "");
        User.username = prefs.getString(userNameKey, "");
        User.displayname = prefs.getString(displayNameKey, "");
        User.cnic = prefs.getString(userCNICKey, "");
        User.address = prefs.getString(userAddressKey, "");
        User.contactno = prefs.getString(userContactKey, "");
        User.type = prefs.getString(userTypeKey, "");
        User.profilepicname = prefs.getString(userProfileKey, "");
    }

    public static void setUserId(Context context, int value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putInt(userIdKey, value);
    }

    public static void setEmail(Context context, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putString(userEmailKey, value);
    }

    public static void setUserName(Context context, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putString(userNameKey, value);
    }

    public static void setDisplayName(Context context, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putString(displayNameKey, value);
    }

    public static void setCNIC(Context context, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putString(userCNICKey, value);
    }

    public static void setAddress(Context context, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putString(userAddressKey, value);
    }

    public static void setContactNo(Context context, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putString(userContactKey, value);
    }

    public static void setType(Context context, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putString(userTypeKey, value);
    }

    public static void setProfilePic(Context context, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE).edit();
        editor.putString(userProfileKey, value);
    }
}