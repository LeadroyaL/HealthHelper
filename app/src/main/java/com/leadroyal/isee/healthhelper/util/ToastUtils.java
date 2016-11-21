package com.leadroyal.isee.healthhelper.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by LeadroyaL on 2016/11/18.
 */

public class ToastUtils {

    public static String TAG = ToastUtils.class.getSimpleName();

    public static void show(Context context, String content) {
        show(context, TAG, content);
    }

    public static void show(Context context, String TAG, String content) {
        Log.d(TAG, content);
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
