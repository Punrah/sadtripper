package com.tripper.sadtripper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class BTListActivity extends Activity {
    protected static final String TAG = "TAG";
    private BluetoothAdapter bluetoothAdapter;
    private OnItemClickListener deviceClickListener = new C02431();
    private ArrayAdapter<String> pairedDevicesAdapter;

    class C02431 implements OnItemClickListener {
        C02431() {
        }

        public void onItemClick(AdapterView<?> adapterView, View mView, int mPosition, long mLong) {
            try {
                BTListActivity.this.bluetoothAdapter.cancelDiscovery();
                String mDeviceInfo = ((TextView) mView).getText().toString();
                Log.v(BTListActivity.TAG, "Device_Address " + mDeviceInfo.split("\n")[1]);
                Bundle bundle = new Bundle();
                bundle.putString("DeviceAddress", mDeviceInfo.split("\n")[1]);
                bundle.putString("DeviceName", mDeviceInfo.split("\n")[0]);
                Intent mBackIntent = new Intent();
                mBackIntent.putExtras(bundle);
                BTListActivity.this.setResult(-1, mBackIntent);
                BTListActivity.this.finish();
            } catch (Exception ex) {
                Log.v(BTListActivity.TAG, ex.toString());
            }
        }
    }

    protected void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setResult(0);
        this.pairedDevicesAdapter = new ArrayAdapter(this, R.layout.device_name);
        ListView mPairedListView = (ListView) findViewById(R.id.paired_devices);
        mPairedListView.setAdapter(this.pairedDevicesAdapter);
        mPairedListView.setOnItemClickListener(this.deviceClickListener);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> mPairedDevices = this.bluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice mDevice : mPairedDevices) {
                this.pairedDevicesAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
            }
            return;
        }
        this.pairedDevicesAdapter.add("None Paired");
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.bluetoothAdapter != null) {
            this.bluetoothAdapter.cancelDiscovery();
        }
    }
}