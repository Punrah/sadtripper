package com.tripper.sadtripper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tripper.sadtripper.app.Formater;
import com.tripper.sadtripper.helper.FarmerSQLiteHandler;
import com.tripper.sadtripper.helper.ItemSQLiteHandler;
import com.tripper.sadtripper.persistence.Farmer;
import com.tripper.sadtripper.persistence.Item;

import java.util.ArrayList;
import java.util.Formatter;


public class ListFarmerFragment extends Fragment  {

    private FarmerSQLiteHandler db;
    private ItemSQLiteHandler dbi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_list_farmer, container, false);
        db = new FarmerSQLiteHandler(getActivity());
        dbi = new ItemSQLiteHandler(getActivity());

        TextView number = (TextView) myInflater.findViewById(R.id.number_farmer);
        TextView list = (TextView) myInflater.findViewById(R.id.list_farmer);

        ArrayList<Farmer> farmerArrayList = db.getAllFarmer();
        ArrayList<Item> itemArrayList = dbi.getAllTransaction();


        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("nilai",Context.MODE_PRIVATE);
        int transactionNumber = sharedPref.getInt("transaction_number", 0);
        int noStart = sharedPref.getInt("no_start", 0);


        number.append("PO number will start at "+ Formater.getPoNo(noStart)+"\n");
        number.append("Transaction Number is "+Formater.getNoTransaction(transactionNumber)+"\n");
        number.append("Number of Farmer is "+farmerArrayList.size());

        for(Item farmer:itemArrayList)
        {
            list.append(farmer.getItemCode()+"  "+farmer.getDescription()+"  "+farmer.getPacking()+"\n");
        }
        for(Farmer farmer:farmerArrayList)
        {
            list.append(farmer.getFarmerCode()+"  "+farmer.getFieldCode()+"  "+farmer.farmerName+"\n");
        }






        return myInflater;
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
