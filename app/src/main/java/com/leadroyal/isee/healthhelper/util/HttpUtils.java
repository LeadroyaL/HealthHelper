package com.leadroyal.isee.healthhelper.util;

import android.bluetooth.BluetoothAdapter;
import android.os.Message;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.leadroyal.isee.healthhelper.MainActivity;
import com.leadroyal.isee.healthhelper.ShowResultActivity;
import com.leadroyal.isee.healthhelper.proto.HealthData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Arrays;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by LeadroyaL on 2016/11/21.
 */

public class HttpUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getKey(String bluetoothMAC) {
        RequestParams params = new RequestParams("deviceID", bluetoothMAC);
        client.get("http://123.206.214.19/getToken", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Message msg = Message.obtain();
                msg.what = MainActivity.MSG_AES_KEY;
                msg.obj = responseBody;
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
        String bluetoothMAC = BluetoothAdapter.getDefaultAdapter().getAddress();
        HealthData.Body body = HealthData.Body.newBuilder().setXinlv(Integer.valueOf(xinlv)).setXueyang(Integer.valueOf(xueyang)).build();
        byte[] bytes = body.toByteArray();
        Log.d("upload:", Arrays.toString(bytes));
        RequestParams params = new RequestParams();
        params.add("deviceID", bluetoothMAC);
        byte[] enc = CryptoUtils.AESEnc(bytes);
        params.add("data", ParseUtils.b2S(enc));
        client.post("http://http://123.206.214.19/uploadData", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Message msg = Message.obtain();
                msg.what = MainActivity.MSG_OK;
                MainActivity.handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Message msg = Message.obtain();
                msg.what = MainActivity.MSG_UPLOAD_FAIL;
                MainActivity.handler.sendMessage(msg);
            }
        });

    }

    public static void downloadData() {
        String bluetoothMAC = BluetoothAdapter.getDefaultAdapter().getAddress();
        RequestParams params = new RequestParams("deviceID", bluetoothMAC);
        client.post("http://123.206.214.19/show", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Message msg = Message.obtain();
                msg.what = ShowResultActivity.MSG_OK;
                msg.obj = new String(responseBody);
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
