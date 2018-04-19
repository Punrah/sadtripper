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

import com.tripper.sadtripper.helper.FarmerSQLiteHandler;
import com.tripper.sadtripper.helper.TransactionSQLiteHandler;


public class DeleteOptionFragment extends Fragment  {

    private FarmerSQLiteHandler db;
    private TransactionSQLiteHandler dbt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_delete_option, container, false);
        db = new FarmerSQLiteHandler(getActivity());
        dbt = new TransactionSQLiteHandler(getActivity());
        Button deleteFarmer = (Button) myInflater.findViewById(R.id.delete_farmer);
        Button deleteTransaction = (Button) myInflater.findViewById(R.id.delete_transaction);

        deleteFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SwipeActivity.class);
                i.putExtra("msg","delete farmer !");
                startActivityForResult(i,1);
            }
        });

        deleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SwipeActivity.class);
                i.putExtra("msg","delete transaction !");
                startActivityForResult(i,2);
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
                db.deleteUsers();
                Toast.makeText(getActivity(), "All farmers data has been deleted", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 2)
        {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                dbt.deleteUsers();
                Toast.makeText(getActivity(), "All transaction data has been deleted", Toast.LENGTH_SHORT).show();
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

