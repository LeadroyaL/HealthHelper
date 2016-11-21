package com.leadroyal.isee.healthhelper.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by LeadroyaL on 2016/11/20.
 */

public class BluetoothArrayAdapter extends ArrayAdapter<BluetoothDevice> {

    public BluetoothArrayAdapter(Context context, List<BluetoothDevice> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
}
