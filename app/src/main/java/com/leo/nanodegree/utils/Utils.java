package com.leo.nanodegree.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
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

    public static void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
