package com.leadroyal.isee.healthhelper.util;

import android.os.Message;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.leadroyal.isee.healthhelper.MainActivity;
import com.leadroyal.isee.healthhelper.ShowResultActivity;
import com.leadroyal.isee.healthhelper.proto.HealthData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Arrays;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by LeadroyaL on 2016/11/21.
 */

public class HttpUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getKey(String bluetoothMAC) {
        //TODO waiting for sever
        client.get("http://www.bing.com", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Message msg = Message.obtain();
                msg.what = MainActivity.MSG_AES_KEY;
//                TODO waiting for server
//                msg.obj = new String(responseBody);
                msg.obj = "0123456789ABCDEF";
                MainActivity.handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Message msg = Message.obtain();
                msg.what = MainActivity.MSG_FAIL;
                MainActivity.handler.sendMessage(msg);
            }
        });
    }

    public static void uploadData(String uploadString) {
        String xinlv = uploadString.split(" ")[0];
        String xueyang = uploadString.split(" ")[1];
        HealthData.Body body = HealthData.Body.newBuilder().setXinlv(Integer.valueOf(xinlv)).setXueyang(Integer.valueOf(xueyang)).build();
        byte[] bytes = body.toByteArray();
        Log.d("TAG", Arrays.toString(bytes));
//        TODO waiting for server
//        send deviceID = ID data = hexString
        Message msg = Message.obtain();
        msg.what = MainActivity.MSG_OK;
        MainActivity.handler.sendMessage(msg);
    }

    public static void downloadData() {
//        TODO waiting for server
//        send deviceID = ID
        client.get("http://www.bing.com", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //TODO parse data
                Message msg = Message.obtain();
                msg.what = ShowResultActivity.MSG_OK;
                //response is a perfect html code
                String s = ParseUtils.parseBodies(null);
                msg.obj = null;
                ShowResultActivity.handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Message msg = Message.obtain();
                msg.what = ShowResultActivity.MSG_FAIL;
                ShowResultActivity.handler.sendMessage(msg);
            }
        });
    }

}
