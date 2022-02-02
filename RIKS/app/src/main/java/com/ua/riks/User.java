package com.ua.riks;

import android.content.Context;
import android.util.Log;

class User {
    public static int id = -1;
    public static String email;
    public static String username;
    public static String displayname;
    public static String cnic;
    public static String address;
    public static String contactno;
    public static String type;
    public static Integer userId;
    public static String profilepicname;

    public static void login(Context context, int id1, String email1, String username1, String displayname1, String cnic1, String address1, String contactno1, String type1, String profilepic1){
        id = id1;
        email = email1;
        username = username1;
        displayname = displayname1;
        cnic = cnic1;
        address =address1;
        contactno = contactno1;
        type = type1;
        profilepicname = profilepic1;
        SharedPreferencesHelper.atLogin(context);
    }

    public static void logout(Context context){
        SharedPreferencesHelper.atLogout(context);
    }

    public static void restart(Context context){
        SharedPreferencesHelper.atStart(context);
    }

    public static void setUserId(Context context, int value){
        userId = value;
        SharedPreferencesHelper.setUserId(context,value);
    }

    public static void setEmail(Context context, String value){
        email = value;
        SharedPreferencesHelper.setEmail(context,value);
    }

    public static void setUserName(Context context, String value){
        username = value;
        SharedPreferencesHelper.setUserName(context,value);
    }

    public static void setDisplayName(Context context, String value){
        displayname = value;
        SharedPreferencesHelper.setDisplayName(context,value);
    }

    public static void setCNIC(Context context, String value){
        cnic = value;
        SharedPreferencesHelper.setCNIC(context,value);
    }

    public static void setAddress(Context context, String value){
        address = value;
        SharedPreferencesHelper.setAddress(context,value);
    }

    public static void setContactNo(Context context, String value){
        contactno = value;
        SharedPreferencesHelper.setContactNo(context,value);
    }

    public static void setType(Context context, String value){
        type = value;
        SharedPreferencesHelper.setType(context,value);
    }

    public static void setProfilePic(Context context, String value){
        profilepicname = value;
        SharedPreferencesHelper.setProfilePic(context,value);
    }

}

