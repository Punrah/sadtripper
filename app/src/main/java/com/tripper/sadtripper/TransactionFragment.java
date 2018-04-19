package com.tripper.sadtripper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.tripper.sadtripper.app.Formater;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class TransactionFragment extends Fragment  implements ZXingScannerView.ResultHandler  {

    private static final int PERMISSION_REQUEST_CODE_CAMERA = 2;

    private ZXingScannerView mScannerView;
    boolean isCam;
    Button scan;
    LinearLayout qrCameraLayout,cam;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_transaction, container, false);
        mScannerView = new ZXingScannerView(getActivity());
        qrCameraLayout = (LinearLayout) myInflater.findViewById(R.id.all);
        scan = (Button) myInflater.findViewById(R.id.scan);
        cam = (LinearLayout) myInflater.findViewById(R.id.qr);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QrScanner();
            }
        });

        return myInflater;
    }

    public void QrScanner(){

        qrCameraLayout.removeAllViews();
        qrCameraLayout.addView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        // Start camera
        setConditionCamera();
        isCam=true;

    }

    @Override
    public void handleResult(Result rawResult) {
        qrCameraLayout.removeAllViews();
        qrCameraLayout.addView(cam);
        qrCameraLayout.addView(scan);
        Intent i = new Intent(getActivity(),TransactionActivity.class);
        i.putExtra("farmer_code",rawResult.getText());

        SharedPreferences sharedPref =  getActivity().getApplicationContext().getSharedPreferences("nilai",Context.MODE_PRIVATE);

        int noStart = sharedPref.getInt("no_start", 0);
        i.putExtra("no_start", noStart);

        startActivity(i);
    }

    public  void requestPermission(String strPermission, int perCode, Context _c, Activity _a){
        switch (perCode) {
            case  PERMISSION_REQUEST_CODE_CAMERA:
                if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
                    Toast.makeText(getActivity(),"Camera permission allows us to access camera. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
                } else {

                    ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
                }
                break;


        }


    }

    public  boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setPermissionCamera();
                } else {

                    Toast.makeText(getActivity(),"Permission Denied, You cannot access camera.",Toast.LENGTH_LONG).show();

                }
                break;


        }
    }

    public void setConditionCamera()
    {
        if (checkPermission(Manifest.permission.CAMERA,getActivity().getApplicationContext(),getActivity())) {
            setPermissionCamera();
        }
        else
        {
            requestPermission(Manifest.permission.CAMERA,PERMISSION_REQUEST_CODE_CAMERA,getActivity().getApplicationContext(),getActivity());
        }
    }

    public  void setPermissionCamera()
    {
        getCamera();
    }

    private void getCamera()
    {
        mScannerView.startCamera();
    }



    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
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
