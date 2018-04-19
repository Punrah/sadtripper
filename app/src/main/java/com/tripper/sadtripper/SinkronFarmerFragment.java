package com.tripper.sadtripper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tripper.sadtripper.AsyncTask.MyAsyncTask;
import com.tripper.sadtripper.app.AppConfig;
import com.tripper.sadtripper.helper.FarmerSQLiteHandler;
import com.tripper.sadtripper.helper.ItemSQLiteHandler;
import com.tripper.sadtripper.helper.TransactionSQLiteHandler;
import com.tripper.sadtripper.persistence.Farmer;
import com.tripper.sadtripper.persistence.Item;
import com.tripper.sadtripper.persistence.Transaction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


public class SinkronFarmerFragment extends Fragment implements Runnable  {
    protected static final String TAG = "TAG";
    private FarmerSQLiteHandler db;
    private TransactionSQLiteHandler dbt;
    private ItemSQLiteHandler dbi;
    private ArrayList<Farmer> listFarmer ;
    private  ArrayList<Item> listItem;
    BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private Handler handler = new C02441();
    private ProgressDialog progressDialog;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    TextView device;
    Button mFind;

    BluetoothAdapter bluetoothAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_sinkron_farmer, container, false);
        db = new FarmerSQLiteHandler(getActivity());
        dbt = new TransactionSQLiteHandler(getActivity());
        dbi = new ItemSQLiteHandler(getActivity());
        Button download = myInflater.findViewById(R.id.sinkron);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SwipeActivity.class);
                i.putExtra("msg","download farmer data !");
                startActivityForResult(i,3);
            }
        });



        return myInflater;
    }

    class C02441 extends Handler {
        C02441() {
        }

        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    class C02452 implements View.OnClickListener {
        C02452() {
        }

        public void onClick(View mView) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(getActivity(), "Message1", Toast.LENGTH_SHORT).show();
            } else if (bluetoothAdapter.isEnabled()) {
                getActivity().startActivityForResult(new Intent(getContext(), BTListActivity.class), 1);
            } else {
                getActivity().startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 2);
            }
        }
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
                    this.progressDialog = ProgressDialog.show(getActivity(), "Connecting...", this.bluetoothDevice.getName() + " : " + this.bluetoothDevice.getAddress(), true, true);
                    new Thread(this).start();
                    return;
                }
                return;
            case 2:
                if (mResultCode == -1) {
                    startActivityForResult(new Intent(getActivity(), BTListActivity.class), 1);
                    return;
                } else {
                    Toast.makeText(getActivity(), "Message", Toast.LENGTH_SHORT).show();
                    return;
                }
            case 3:
            {
                if (mResultCode == Activity.RESULT_OK) {

                    new getFarmer().execute();
                }
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
        } catch (IOException e) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }


    private class getFarmer extends MyAsyncTask {
        Bundle savedInstanceState;

        int transactionNumber;
        int noStart;

        public getFarmer()
        {
        }




        @Override
        public Context getContext () {
            return getActivity();
        }



        @Override
        public void setSuccessPostExecute() {

            db.deleteUsers();
            dbt.deleteUsers();
            dbi.deleteUsers();
            for (Farmer farmer : listFarmer) {
                db.addUser(farmer);

            }

            for (Item farmer : listItem) {
                dbi.addUser(farmer);

            }


            SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("nilai",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("no_start", noStart);
            editor.putInt("transaction_number", transactionNumber);
            editor.putInt("lot_number", 0);
            editor.commit();





        }

        @Override
        public void setFailPostExecute() {

        }

        public void postData() {
            String url = AppConfig.URL_LIST_FARMER;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, "UTF-8");

                if (jsonStr != null) {
                    try {
                        isSucces=true;
                        JSONObject obj = new JSONObject(jsonStr);

                        listItem =new ArrayList<>();
                        JSONArray jsonArray1 = obj.getJSONArray("item");
                        for(int i=0;i<jsonArray1.length();i++)
                        {
                            JSONObject jsonObject =jsonArray1.getJSONObject(i);
                            Item item = new Item();
                            item.ItemCode = jsonObject.getString("ItemCode");
                            item.Description = jsonObject.getString("Description");
                            item.Packing = jsonObject.getString("Packing");
                            listItem.add(item);
                        }
                        JSONArray jsonArray = obj.getJSONArray("result");
                        transactionNumber = obj.getInt("TransactionName");
                        noStart = obj.getInt("PoNo");
                        listFarmer =new ArrayList<>();

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Farmer farmer = new Farmer();
                            farmer.id = jsonObject.getString("id");
                            farmer.farmerCode = jsonObject.getString("FarmerCode");
                            farmer.fieldCode = jsonObject.getString("FieldCode");
                            farmer.crop = jsonObject.getString("Crop");
                            farmer.location = jsonObject.getString("Location");
                            farmer.plantSize = jsonObject.getString("PlantSize");
                            farmer.spacing = jsonObject.getString("Spacing");
                            farmer.area = jsonObject.getString("Area");
                            farmer.yieldWet = jsonObject.getString("YieldWet");
                            farmer.yieldDried = jsonObject.getString("YieldDried");
                            farmer.cuc = jsonObject.getString("CUC");
                            farmer.ics = jsonObject.getString("ICS");
                            farmer.status = jsonObject.getString("Status");
                            farmer.idWebsol = jsonObject.getString("idWebsol");
                            farmer.farmerName = jsonObject.getString("FarmerName");
                            farmer.generate = jsonObject.getString("generate");



                            listFarmer.add(farmer);
                        }

                    } catch (final JSONException e) {
                        badServerAlert();
                    }
                } else {
                    badServerAlert();
                }
            } catch (IOException e) {
                badInternetAlert();
            }
        }






    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();

    }







}
