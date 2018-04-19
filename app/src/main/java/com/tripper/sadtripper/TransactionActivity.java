package com.tripper.sadtripper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tripper.sadtripper.app.Formater;
import com.tripper.sadtripper.helper.FarmerSQLiteHandler;
import com.tripper.sadtripper.helper.ItemSQLiteHandler;
import com.tripper.sadtripper.persistence.Farmer;
import com.tripper.sadtripper.persistence.Item;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    private FarmerSQLiteHandler db;
    private ItemSQLiteHandler dbi;
    Spinner spinner;
    Spinner itemCodeEditText;// = findViewById(R.id.itemCode);
    EditText descriptionEditText;// = findViewById(R.id.description);
    EditText packingEditText;// = findViewById(R.id.packing);
    EditText priceEditText;// = findViewById(R.id.price);
    EditText qtyEditText;// = findViewById(R.id.qty);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        db = new FarmerSQLiteHandler(getApplicationContext());
        dbi = new ItemSQLiteHandler(getApplicationContext());

        TextView farmerCodeTextView = findViewById(R.id.farmer_code);
        TextView farmerNameTextView = findViewById(R.id.farmer_name);

         itemCodeEditText = findViewById(R.id.itemCode);
         descriptionEditText = findViewById(R.id.description);
         packingEditText = findViewById(R.id.packing);
         priceEditText = findViewById(R.id.price);
         qtyEditText = findViewById(R.id.qty);

        Button buttonBuy = findViewById(R.id.buy);
        String i = getIntent().getStringExtra("farmer_code");
        final ArrayList<Farmer> listFarmer = db.getFarmerByCode(i);

        ArrayList<String> field = db.getFieldStringByCode(i);



        farmerCodeTextView.setText(""+listFarmer.get(0).farmerCode);
        farmerNameTextView.setText(""+listFarmer.get(0).farmerName);


        priceEditText.requestFocus();



setSpinner(i);
setSpinner2(i);

buttonBuy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(), MainActivityPrint.class);
        i.putExtra("farmer_code",listFarmer.get(0).farmerCode);
        i.putExtra("id_websol",listFarmer.get(0).idWebsol);
        i.putExtra("farmer_name", listFarmer.get(0).farmerName);
        i.putExtra("field_code", spinner.getSelectedItem().toString());
        i.putExtra("item_code",itemCodeEditText.getSelectedItem().toString());
        i.putExtra("description",descriptionEditText.getText().toString());
        i.putExtra("packing",packingEditText.getText().toString());
        i.putExtra("price",priceEditText.getText().toString());
        i.putExtra("qty",qtyEditText.getText().toString());



        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("nilai",Context.MODE_PRIVATE);

        //int noStart = getResources().getInteger("no_start");
        int transactionStart = sharedPref.getInt("transaction_number", 0);
        int lotStart = sharedPref.getInt("lot_number", 0);
        int noStart = getIntent().getIntExtra("no_start",0);

        i.putExtra("po_number",Formater.getPoNo(noStart));
        i.putExtra("lot_number",Formater.getLotNumber(noStart,lotStart));
        i.putExtra( "transaction_name",Formater.getNoTransaction(transactionStart));

        startActivity(i);

    }
});




    }


    private void setSpinner2(String i) {
        itemCodeEditText = (Spinner) findViewById(R.id.itemCode);

        Intent intent = getIntent();

        ArrayList<String> sizes = null;
         ArrayList<Item> items = null;

        if (intent != null) {
            // Receiving ArrayList in Another Activity where "sizeList" is the Key
            sizes = dbi.getItemCodeList();
            items= dbi.getAllTransaction();
        }

        if (sizes != null) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sizes);

            itemCodeEditText.setAdapter(adapter);

        }

        final ArrayList<Item> finalItems = items;
        itemCodeEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                descriptionEditText.setText(finalItems.get(position).getDescription());
                packingEditText.setText(finalItems.get(position).getPacking());
                priceEditText.setText("");
                priceEditText.requestFocus();
                qtyEditText.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }


    private void setSpinner(String i) {
        spinner = (Spinner) findViewById(R.id.field);

        Intent intent = getIntent();

        ArrayList<String> sizes = null;

        if (intent != null) {
            // Receiving ArrayList in Another Activity where "sizeList" is the Key
            sizes = db.getFieldStringByCode(i);
        }

        if (sizes != null) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sizes);

            spinner.setAdapter(adapter);

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                priceEditText.setText("");
                priceEditText.requestFocus();
                qtyEditText.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

        @Override
    public void onBackPressed() {

            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure to exit this transaction?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {


                                SharedPreferences sharedPref =getApplicationContext().getSharedPreferences("nilai",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("lot_number", 0);
                                editor.commit();
                                finish();


                        }})
                    .setNegativeButton(android.R.string.no, null).show();
    }


    public  String nextPo()
    {
        SharedPreferences sharedPref =getPreferences(Context.MODE_PRIVATE);
        //int noStart = getResources().getInteger("no_start");
        int transactionNumber = sharedPref.getInt("transaction_number", 0);
        int noStart = sharedPref.getInt("no_start", 0);
        String val= Formater.getPoNo(noStart);
        noStart=noStart+1;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("no_start", noStart);
        editor.commit();

        return val;
    }
}
