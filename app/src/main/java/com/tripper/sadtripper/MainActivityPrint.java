package com.tripper.sadtripper;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tripper.sadtripper.helper.FarmerSQLiteHandler;
import com.tripper.sadtripper.helper.TransactionSQLiteHandler;
import com.tripper.sadtripper.persistence.Transaction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class MainActivityPrint extends AppCompatActivity implements Runnable {
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    protected static final String TAG = "TAG";
    AlertDialog alertDialog;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    Context context = this;
    TextView device;
    private Handler handler = new C02441();
    Button mFind;
    Button print;
    Button confirm;
    TextView printText;
    private ProgressDialog progressDialog;
    WebView webView;

    private FarmerSQLiteHandler db;
    private TransactionSQLiteHandler dbt;

    String idWebsol;// = getIntent().getStringExtra("id_websol");
    String farmerCode;// = getIntent().getStringExtra("farmer_code");
    String farmerName;// = getIntent().getStringExtra("farmer_name");
    String fieldCode;// = getIntent().getStringExtra("field_code");
    String itemCode;// = getIntent().getStringExtra("item_code");
    String description;// = getIntent().getStringExtra("description");
    String packing;// = getIntent().getStringExtra("packing");
    String price;// = getIntent().getStringExtra("price");
    String qty;// = getIntent().getStringExtra("qty");
    String poNumber;// = getIntent().getStringExtra("po_number");
    String lotNumber;// = getIntent().getStringExtra("lot_number");
    String transactionName;// = getIntent().getStringExtra("transaction_name");

    class C02441 extends Handler {
        C02441() {
        }

        public void handleMessage(Message msg) {
            MainActivityPrint.this.progressDialog.dismiss();
            Toast.makeText(MainActivityPrint.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    class C02452 implements OnClickListener {
        C02452() {
        }

        public void onClick(View mView) {
            MainActivityPrint.this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (MainActivityPrint.this.bluetoothAdapter == null) {
                Toast.makeText(MainActivityPrint.this, "Message1", Toast.LENGTH_SHORT).show();
            } else if (MainActivityPrint.this.bluetoothAdapter.isEnabled()) {




                MainActivityPrint.this.startActivityForResult(new Intent(MainActivityPrint.this, BTListActivity.class), 1);
            } else {
                MainActivityPrint.this.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 2);
            }
        }
    }

    class C02473 implements OnClickListener {

        class C02461 extends Thread {
            C02461() {
            }

            public void run() {
                try {
                    byte[] format = { 27, 33, 0 };
                    byte[] arrayOfByte1 = { 27, 33, 0 };
                    format[2] = ((byte)(0x8 | arrayOfByte1[2]));
                    MainActivityPrint.this.bluetoothSocket.getOutputStream().write(format);

                    MainActivityPrint.this.bluetoothSocket.getOutputStream().write(MainActivityPrint.this.printText.getText().toString().getBytes(Charset.forName("UTF-8")));
                } catch (Exception e) {
                    Log.e("MainActivityPrint", "Exe ", e);
                }
            }
        }

        C02473() {
        }

        public void onClick(View mView) {
            new C02461().start();
        }
    }

    class C02484 extends WebViewClient {
        C02484() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class C02495 implements DialogInterface.OnClickListener {
        C02495() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
        }
    }

    class C02506 extends WebViewClient {
        C02506() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class C02517 implements DialogInterface.OnClickListener {
        C02517() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
        }
    }

    class C02528 extends Thread {
        C02528() {
        }

        public void run() {
            try {
                MainActivityPrint.this.bluetoothSocket.getOutputStream().write("IyalTamizh.bluetoothPrint \n \n Test Print \n\n  ABCDEDGHIJKLMNOPQRSTUVWXYZ \n\n 0123456789 \n\n abcdedghijklmnopqrstuvwxyz \n\n!@#$%^&*()_+-={}[]|:,<>?".getBytes(Charset.forName("UTF-8")));
            } catch (Exception e) {
                Log.e("MainActivityPrint", "Exe ", e);
            }
        }
    }


     String tt="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main_print);

        db = new FarmerSQLiteHandler(getApplicationContext());
        dbt = new TransactionSQLiteHandler(getApplicationContext());

        idWebsol = getIntent().getStringExtra("id_websol");
        farmerCode = getIntent().getStringExtra("farmer_code");
        farmerName = getIntent().getStringExtra("farmer_name");
        fieldCode = getIntent().getStringExtra("field_code");
        itemCode = getIntent().getStringExtra("item_code");
        description = getIntent().getStringExtra("description");
        packing = getIntent().getStringExtra("packing");
        price = getIntent().getStringExtra("price");
        qty = getIntent().getStringExtra("qty");
        poNumber = getIntent().getStringExtra("po_number");
        lotNumber = getIntent().getStringExtra("lot_number");
        transactionName = getIntent().getStringExtra("transaction_name");

        double jum=0;
        if(qty.equals("") || price.equals(""))
        {
            jum=0;
            price="0";
        }
        else
        {
            jum = Double.parseDouble(qty)* Double.parseDouble(price);
        }

        double rest = jum % 500;
        if(rest<250)
        {
            jum=jum-rest;
        }
        else
        {
            jum=jum+(500-rest);
        }



        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);



        tt= "            TRIPPER         \n"+
                "     WE ADD VALUE AT ORIGIN   \n"+
                "Kawasan Industri MM2100 Jl Timor Blok E 1, Desa Jatiwangi - Cikarang Barat Bekasi, Jawa Barat 17845, Indonesia\n"+
                "_____________________________\n"+
                "Tanggal    :"+new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime())+"\n"+
                "PO         :"+poNumber+"\n"+
                "ID Petani  :"+farmerCode+"\n"+
                "Nama Petani:"+farmerName+"\n"+
                "Lot        :"+lotNumber+"\n"+
                "Lahan      :"+fieldCode+"\n"+
                "Kode Barang:"+itemCode+"\n"+
                "Deskripsi  :"+description+"\n"+
                "Packing    :"+packing+"\n"+
                "Harga      :"+kursIndonesia.format(Double.parseDouble(price))+"\n"+
                "Jumlah     :"+qty+" Kg\n"+
                "_____________________________\n"+
                "Total      :"+ kursIndonesia.format(jum)+"\n\n"+
                "             TTD             " +"\n\n\n\n\n\n\n\n\n\n\n\n";



        this.printText = (TextView) findViewById(R.id.qty);
        this.print = (Button) findViewById(R.id.print);
        this.confirm = (Button) findViewById(R.id.confirm);


        MainActivityPrint.this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (MainActivityPrint.this.bluetoothAdapter == null) {
            Toast.makeText(MainActivityPrint.this, "Message1", Toast.LENGTH_SHORT).show();
        } else if (MainActivityPrint.this.bluetoothAdapter.isEnabled()) {

            this.bluetoothDevice = this.bluetoothAdapter.getRemoteDevice("00:11:22:33:44:55");
             new Thread(this).start();
            this.print.setOnClickListener(new C02473());

//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivity(enableBtIntent);
//
//            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//            // If there are paired devices
//            if (pairedDevices.size() > 0) {
//                // Loop through paired devices
//                for (BluetoothDevice device : pairedDevices) {
//
//
//                    Log.e("Mac Addressess","are:  "+bluetoothAdapter.getRemoteDevice(device.getAddress()));
//                }
//            }

        } else {

        }




        printText.setText(tt);

        final Transaction transaction = new Transaction();


        transaction.TransactionName =transactionName;
        transaction.Date= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()).toString();
        transaction.NoPo = poNumber;
        transaction.FarmerCode = farmerCode;
        transaction.FarmerName = farmerName;
        transaction.FieldCode = fieldCode;
        transaction.lotnumber = lotNumber;
        transaction.ItemCode = itemCode;
        transaction.Description = description;
        transaction.Packing = packing;
        transaction.Price = price;
        transaction.Qty = qty;
        transaction.idWebsol=idWebsol;

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbt.addUser(transaction);
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("nilai",Context.MODE_PRIVATE);

                //int noStart = getResources().getInteger("no_start");
                int transactionStart = sharedPref.getInt("transaction_number", 0);
                int noStart = sharedPref.getInt("no_start", 0);
                int lotStart = sharedPref.getInt("lot_number", 0);
                if(lotStart==0)
                {
                    nextPo();

                }
                nextLot();
                closeSocket(bluetoothSocket);
                finish();


            }
        });
    }

    public void nextLot()
    {
        SharedPreferences sharedPref =getApplicationContext().getSharedPreferences("nilai",Context.MODE_PRIVATE);

        int lotNumber = sharedPref.getInt("lot_number", 0);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("lot_number", lotNumber+1);
        editor.commit();
    }

    public void nextPo()
    {
        SharedPreferences sharedPref =getApplicationContext().getSharedPreferences("nilai",Context.MODE_PRIVATE);

        int noStart = sharedPref.getInt("no_start", 0);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("no_start", noStart+1);
        editor.commit();
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
                    this.device.setText(mDeviceAddress);
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    this.bluetoothDevice = this.bluetoothAdapter.getRemoteDevice(mDeviceAddress);
                    this.progressDialog = ProgressDialog.show(this, "Connecting...", this.bluetoothDevice.getName() + " : " + this.bluetoothDevice.getAddress(), true, true);
                    new Thread(this).start();
                    return;
                }
                return;
            case 2:
                if (mResultCode == -1) {
                    String mDeviceAddress = mDataIntent.getExtras().getString("DeviceAddress");
                    this.device.setText(mDeviceAddress);
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    startActivityForResult(new Intent(this, BTListActivity.class), 1);
                    return;
                } else {
                    Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
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
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(this.bluetoothSocket);
        }
    }

    @Override
    public void onBackPressed() {

        closeSocket(bluetoothSocket);
        super.onBackPressed();

    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException e) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }


    public void onTestPrint(View v) {
        new C02528().start();
    }


}