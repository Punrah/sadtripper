package com.tripper.sadtripper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tripper.sadtripper.helper.FarmerSQLiteHandler;
import com.tripper.sadtripper.helper.TransactionSQLiteHandler;
import com.tripper.sadtripper.persistence.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionPrintActivity extends AppCompatActivity {

    private FarmerSQLiteHandler db;
    private TransactionSQLiteHandler dbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        db = new FarmerSQLiteHandler(getApplicationContext());
        dbt = new TransactionSQLiteHandler(getApplicationContext());

        TextView poNumberTextView = findViewById(R.id.po_number);
        TextView farmerCodeTextView = findViewById(R.id.farmer_code);
        TextView farmerNameTextView = findViewById(R.id.farmer_name);
        TextView lotNumberTextView = findViewById(R.id.lotnumber);
        TextView fieldCodeTextView = findViewById(R.id.field_code);
        TextView itemCodeTextView = findViewById(R.id.item_code);
        TextView descriptionTextView = findViewById(R.id.description);
        TextView packingTextView = findViewById(R.id.packing);
        TextView priceTextView = findViewById(R.id.price);
        TextView qtyTextView = findViewById(R.id.qty);
        Button confirm = findViewById(R.id.confirm);
        String idWebsol = getIntent().getStringExtra("id_websol");
        String farmerCode = getIntent().getStringExtra("farmer_code");
        String farmerName = getIntent().getStringExtra("farmer_name");
        String fieldCode = getIntent().getStringExtra("field_code");
        String itemCode = getIntent().getStringExtra("item_code");
        String description = getIntent().getStringExtra("description");
        String packing = getIntent().getStringExtra("packing");
        String price = getIntent().getStringExtra("price");
        String qty = getIntent().getStringExtra("qty");
        String poNumber = getIntent().getStringExtra("po_number");
        String lotNumber = getIntent().getStringExtra("lot_number");
        String transactionName = getIntent().getStringExtra("transaction_name");

        poNumberTextView.setText(poNumber);
        farmerCodeTextView.setText(farmerCode);
        farmerNameTextView.setText(farmerName);
        lotNumberTextView.setText(lotNumber);
        fieldCodeTextView.setText(fieldCode);
        itemCodeTextView.setText(itemCode);
        descriptionTextView.setText(description);
        packingTextView.setText(packing);
        priceTextView.setText("Rp. "+price);
        qtyTextView.setText(qty);

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
                finish();


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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


}
