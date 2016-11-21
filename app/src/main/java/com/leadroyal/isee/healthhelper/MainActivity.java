package com.leadroyal.isee.healthhelper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.leadroyal.isee.healthhelper.util.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    BluetoothDevice device;
    BluetoothSocket socket;
    InputStream inputStream;
    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (device == null) {
            ToastUtils.show(this, "get empty device");
            return;
        }
        device = (BluetoothDevice) intent.getExtras().get("device");
        connect();
        if (inputStream == null || outputStream == null) {
            ToastUtils.show(this, "IO error");
            return;
        }

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
}
