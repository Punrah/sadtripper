package com.tripper.sadtripper;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class Print extends AppCompatActivity implements Runnable {
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    protected static final String TAG = "TAG";
    String BILL;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private Handler handler = new C02531();
    private ProgressDialog progressDialog;

    class C02531 extends Handler {
        C02531() {
        }

        public void handleMessage(Message msg) {
            Print.this.progressDialog.dismiss();
            Toast.makeText(Print.this, msg.obj.toString(), 0).show();
        }
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
        for (int k = 0; k < b.length; k++) {
            System.out.println("Arun  [" + k + "] = 0x" + Formatter.byteToHex(b[k]));
        }
        return b[3];
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.pritn);
        this.BILL = getIntent().getStringExtra("content");
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.bluetoothAdapter == null) {
            Toast.makeText(this, "Message1", 0).show();
        } else if (this.bluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent(this, BTListActivity.class), 1);
        } else {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 2);
        }
    }

    public void printContent() {
        try {
            this.bluetoothSocket.getOutputStream().write(this.BILL.getBytes());
        } catch (Exception e) {
            Log.e("MainActivityPrint", "Exe ", e);
        }
        closeSocket(this.bluetoothSocket);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onActivityResult(int mRequestCode, int mResultCode, Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
        switch (mRequestCode) {
            case 1:
                if (mResultCode == -1) {
                    String mDeviceAddress = mDataIntent.getExtras().getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    this.bluetoothDevice = this.bluetoothAdapter.getRemoteDevice(mDeviceAddress);
                    this.progressDialog = ProgressDialog.show(this, "Connecting...", this.bluetoothDevice.getName() + " : " + this.bluetoothDevice.getAddress(), true, true);
                    new Thread(this).start();
                    return;
                }
                return;
            case 2:
                if (mResultCode == -1) {
                    startActivityForResult(new Intent(this, BTListActivity.class), 1);
                    return;
                } else {
                    Toast.makeText(this, "Message", 0).show();
                    return;
                }
            default:
                return;
        }
    }

    public void run() {
        try {
            this.bluetoothSocket = this.bluetoothDevice.createInsecureRfcommSocketToServiceRecord(this.applicationUUID);
            this.bluetoothAdapter.cancelDiscovery();
            this.bluetoothSocket.connect();
            Message alertMessage = new Message();
            alertMessage.obj = "DeviceConnected ";
            this.handler.sendMessage(alertMessage);
            printContent();
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(this.bluetoothSocket);
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Message alertMessage = new Message();
            alertMessage.obj = "Connection Failed ";
            this.handler.sendMessage(alertMessage);
            Log.d(TAG, "SocketClosed");
            finish();
        } catch (IOException e) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }
}