package com.leo.nanodegree.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by leo on 5/29/15.
 */
public class Utils {

    private static Toast mToast;
    public static void showToast(Context context, String message, int duration){

        if(mToast != null)
            mToast.cancel();

        mToast = Toast.makeText(context,message,duration);
        mToast.show();
    }
}
