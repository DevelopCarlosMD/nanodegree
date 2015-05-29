package com.leo.nanodegree.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by leo on 5/29/15.
 */
public class Utils {


    public static void showToast(Context context, String message, int duration){
        Toast.makeText(context,message,duration).show();
    }
}
