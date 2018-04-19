package com.tripper.sadtripper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tripper.sadtripper.AsyncTask.MyAsyncTask;
import com.tripper.sadtripper.app.AppConfig;
import com.tripper.sadtripper.helper.TransactionSQLiteHandler;
import com.tripper.sadtripper.persistence.Transaction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UploadTransactionFragment extends Fragment  {

    private TransactionSQLiteHandler dbt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_upload_transaction, container, false);

        dbt= new TransactionSQLiteHandler(getActivity());

        Button button = myInflater.findViewById(R.id.upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SwipeActivity.class);
                i.putExtra("msg","upload transaction data !");
                startActivityForResult(i,1);
            }
        });


        return myInflater;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {

                new uploadTransaction().execute();
            }
        }
    }


    private class uploadTransaction extends MyAsyncTask {
        Bundle savedInstanceState;
        String data;
        public uploadTransaction()
        {
            ArrayList<Transaction> listTransaction=dbt.getAllTransaction();

            Gson gson = new Gson();
            String json = gson.toJson(listTransaction);
            data="{ \"result\":"+ json+ "}";
        }




        @Override
        public Context getContext () {
            return getActivity();
        }



        @Override
        public void setSuccessPostExecute() {

            Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();





        }

        @Override
        public void setFailPostExecute() {
            Toast.makeText(getActivity(), "Upload Failed", Toast.LENGTH_SHORT).show();

        }

        public void postData() {
            String url = AppConfig.URL_TRANSACTION;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("data", data));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request


                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, "UTF-8");

                if (jsonStr != null) {
                    try {
                        isSucces=true;
                        JSONObject obj = new JSONObject(jsonStr);


                        String str = obj.getString("status");


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
