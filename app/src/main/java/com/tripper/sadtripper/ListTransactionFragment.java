package com.tripper.sadtripper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tripper.sadtripper.helper.TransactionSQLiteHandler;
import com.tripper.sadtripper.persistence.Farmer;
import com.tripper.sadtripper.persistence.Transaction;

import java.util.ArrayList;


public class ListTransactionFragment extends Fragment  {


    TransactionSQLiteHandler dbt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_list_transaction, container, false);
        dbt = new TransactionSQLiteHandler(getActivity());


        TextView list = (TextView) myInflater.findViewById(R.id.list_transaction);

        ArrayList<Transaction> transactionArrayList = dbt.getAllTransaction();

        for(Transaction transaction:transactionArrayList)
        {
            list.append(transaction.getNoPo()
                    +"  "+transaction.getFarmerCode()
                    +"  "+transaction.getFarmerName()
                    +"  "+transaction.getFieldCode()
                    +"\n"+transaction.getLotnumber()
                    +" "+transaction.getItemCode()
                    +"  "+transaction.getPacking()
                    +"  "+"Rp. "+transaction.getPrice()
                    +"  "+"("+transaction.getQty()+")"
                    +"\n\n");
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
