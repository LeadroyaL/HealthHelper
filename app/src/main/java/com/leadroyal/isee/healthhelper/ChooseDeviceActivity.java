package com.leadroyal.isee.healthhelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.leadroyal.isee.healthhelper.util.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class ChooseDeviceActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    BluetoothAdapter bluetoothAdapter;
    List<String> devicesNameList;
    List<BluetoothDevice> devicesList;
    ArrayAdapter<String> mListAdapter;

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.activity_choose_device)
    LinearLayout activityChooseDevice;

    @OnItemClick(R.id.listView)
    void clickItem(int position) {
        ToastUtils.show(this, "long click to establish connection!");
    }

    @OnItemLongClick(R.id.listView)
    boolean longClickItem(int position) {
        BluetoothDevice device = devicesList.get(position);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("device", device);
        startActivity(intent);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        ButterKnife.bind(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        initView();
        if (!initBluetooth())
            return;
        showBondedDevices();
    }

    private void initView() {
        devicesList = new ArrayList<>();
        devicesNameList = new ArrayList<>();
        mListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devicesNameList);
        listView.setAdapter(mListAdapter);
    }

    private boolean initBluetooth() {
        if (bluetoothAdapter == null)
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            ToastUtils.show(this, "something wrong with your bluetooth");
            return false;
        }
        if (!bluetoothAdapter.isEnabled()) {
            ToastUtils.show(this, "your bluetooth is disabled, please check");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
        return true;
    }

    private void showBondedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices == null)
            return;
        devicesList.clear();
        devicesNameList.clear();
        for (BluetoothDevice device : pairedDevices) {
            devicesList.add(device);
            devicesNameList.add(device.getName() + " = " + device.getAddress());
        }
        mListAdapter.notifyDataSetChanged();
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        ToastUtils.show(this, "long click to establish connection!");
    }


    @Override
    public void onRefresh() {
        if (!initBluetooth())
            return;
        showBondedDevices();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK)
            ToastUtils.show(this, "open bluetooth success!");
        else
            ToastUtils.show(this, "open bluetooth fail!");
    }
}
