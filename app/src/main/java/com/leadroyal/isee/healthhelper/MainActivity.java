package com.leadroyal.isee.healthhelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.leadroyal.isee.healthhelper.util.CryptoUtils;
import com.leadroyal.isee.healthhelper.util.HttpUtils;
import com.leadroyal.isee.healthhelper.util.ParseUtils;
import com.leadroyal.isee.healthhelper.util.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.textView)
    TextView textView;
    private Context context;
    private static final boolean isOffline = true;
    private static final boolean isWorking = true;
    public static final int MSG_AES_KEY = 0;
    public static final int MSG_OK = 1;
    public static final int MSG_FAIL = 2;
    public static final int MSG_UPLOAD_FAIL = 3;
    public static Handler handler;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    public InputStream inputStream;
    public OutputStream outputStream;
    private String uploadString;
    private Timer timer;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!initBluetooth())
            return;
        context = this;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_AES_KEY:
                        Log.d("handle", "receive AES success!");
                        byte[] s = (byte[]) msg.obj;
                        if (s.length == 32) {
                            CryptoUtils.setAESKey(s);
                        } else
                            ToastUtils.show(context, "receive error AES key");
                        break;
                    case MSG_OK:
                        Log.d("handle", "message ok");
                        break;
                    case MSG_FAIL:
                        ToastUtils.show(context, "please check your network");
                        break;
                    case MSG_UPLOAD_FAIL:
                        ToastUtils.show(context, "upload fail!");
                        break;
                    default:

                }
            }
        };
        HttpUtils.getKey(BluetoothAdapter.getDefaultAdapter().getAddress());
    }

    private boolean initBluetooth() {
        Intent intent = getIntent();
        device = (BluetoothDevice) intent.getExtras().get("device");
        if (device == null) {
            ToastUtils.show(this, "get empty device");
            return false;
        }
        if (!isOffline) {
            connect();
            if (inputStream == null || outputStream == null) {
                ToastUtils.show(this, "IO error");
                return false;
            }
        } else {
            timer = new Timer(true);
            task = new TimerTask() {
                @Override
                public void run() {
                    String s = "100,200";
                    Log.d("BLE", s);
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(String.format("心率:%s,血氧:%s", 123, 123));
                        }
                    });
                    HttpUtils.uploadData(getUploadString());
                }
            };
            timer.schedule(task, 0, 1000);
        }
        return true;
    }

    private void connect() {
        ToastUtils.show(this, "establishing connection");
        UUID uuid = device.getUuids()[0].getUuid();
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message getUploadMsg() {
        Message msg = Message.obtain();
        msg.what = MainActivity.MSG_UPLOAD_FAIL;
        return msg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public String getUploadString() {
        String s = textView.getText().toString();
        String p = "123,123";
//        return s.split(":")[1].replace(",","") + " " + s.split(":")[2];
        return "123 123";
    }
}
