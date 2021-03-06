package com.clwater.littlesee.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yszsyf on 17/3/16.
 */

public class SPHelper {
    private static String DIARYCLASS = "diaryclass";
    private static String NEWSLASS = "newsclass";
    private static String SERVERADDRESS = "serveraddress";
    private static String IMAGECLASS  = "imageclass";

    private static SharedPreferences init(Context context){
        SharedPreferences sp = context.getSharedPreferences("SP", MODE_PRIVATE);
        return sp;
    }

    public static void setStringValue(Context context , String name , String value){
        SharedPreferences sp = init(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getStringValue(Context context, String name){
        SharedPreferences sp = init(context);
        return sp.getString(name , "");
    }

    public static void saveDiaryclass(Context context , String value){
        setStringValue(context , DIARYCLASS , value);
    }

    public static String getDiaryclass(Context context){
        String r = getStringValue(context , DIARYCLASS);
        return r;
    }


    public static void saveImageclass(Context context , String value){
        setStringValue(context , IMAGECLASS , value);
    }

    public static String getImageclass(Context context){
        String r = getStringValue(context , IMAGECLASS);
        return r;
    }

    public static void saveNewsclass(Context context , String value){
        setStringValue(context , NEWSLASS , value);
    }

    public static String getNewsclass(Context context){
        String r = getStringValue(context , NEWSLASS);
        return r;
    }


    public static void saveServeraddress(Context context , String value){
        setStringValue(context , SERVERADDRESS , value);
    }

    public static String getServeraddress(Context context){
        String r = getStringValue(context , SERVERADDRESS);
        if (r.isEmpty()){
            r = WebContent.ServerAddress;
        }
        return r;
    }


}
