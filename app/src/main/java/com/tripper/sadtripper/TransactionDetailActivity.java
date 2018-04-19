package com.tripper.sadtripper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tripper.sadtripper.helper.FarmerSQLiteHandler;
import com.tripper.sadtripper.helper.TransactionSQLiteHandler;
import com.tripper.sadtripper.persistence.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class TransactionDetailActivity extends AppCompatActivity {

    private FarmerSQLiteHandler db;
    private TransactionSQLiteHandler dbt;


    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    BluetoothDevice bluetoothDevice;
    InputStream inputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String value = "";


    byte FONT_TYPE;

    private static BluetoothSocket btsocket;

    private static OutputStream outputStream;




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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        db = new FarmerSQLiteHandler(getApplicationContext());
        dbt = new TransactionSQLiteHandler(getApplicationContext());


        TextView qtyTextView = findViewById(R.id.qty);
        Button confirm = findViewById(R.id.confirm);
        Button print = findViewById(R.id.confirm_print);
        Button print2 = findViewById(R.id.confirm_print2);
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

         int jum=0;
         if(qty.equals("") || price.equals(""))
         {
            jum=0;
         }
        else
         {
             jum = Integer.parseInt(qty)* Integer.parseInt(price);
         }



         final String tt= "            TRIPPER         \n"+
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
                 "Harga      : Rp."+price+"\n"+
                 "Jumlah     :"+qty+"\n"+
                 "_____________________________\n"+
                 "Total      :Rp."+ jum+"\n\n"+
                 "             TTD             " +"\n\n\n\n\n\n";




        qtyTextView.setText(tt);

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

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


IntentPrint( tt
);



            }
        });


        print2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                IntentPrint2(tt
                );



            }
        });






    }

    private void createPdf(){
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(100, 200, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        canvas.drawText(poNumber,10,10,paint);
        canvas.drawText(farmerCode,10,30,paint);
        canvas.drawText(farmerName,10,50,paint);
        canvas.drawText(lotNumber,10,70,paint);
        canvas.drawText(fieldCode,10,90,paint);
        canvas.drawText(itemCode,10,110,paint);
        canvas.drawText(description,10,130,paint);
        canvas.drawText(packing,10,150,paint);
        canvas.drawText(price,10,170,paint);
        canvas.drawText(qty,10,190,paint);

        // finish the page
        document.finishPage(page);

        // write the document content

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sadtripper/";
        String targetPdf = path+poNumber+"_"+lotNumber+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            viewPdf(targetPdf);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    // Method for opening a pdf file
    private void viewPdf( String directory) {

        File pdfFile = new File(directory);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
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

    public void InitPrinter ()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try
        {
            if(!bluetoothAdapter.isEnabled())
            {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    Log.e("namewkwk",device.getAddress());
                    if(device.getName().equals("BlueTooth Printer")) //Note, you will need to change this to match the name of your device
                    {
                        bluetoothDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                beginListenForData();
            }
            else
            {
                value+="No Devices found";
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                return;
            }
        }
        catch(Exception ex)
        {
            value+=ex.toString()+ "\n" +" InitPrinter \n";
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
            Log.e("eror",value);
        }
    }

    public void InitPrinter2 ()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try
        {
            if(!bluetoothAdapter.isEnabled())
            {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    Log.e("namewkwk",device.getName());
                    if(device.getName().equals("InnerPrinter")) //Note, you will need to change this to match the name of your device
                    {
                        bluetoothDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                beginListenForData();
            }
            else
            {
                value+="No Devices found";
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                return;
            }
        }
        catch(Exception ex)
        {
            value+=ex.toString()+ "\n" +" InitPrinter \n";
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
            Log.e("eror",value);
        }
    }

    public void beginListenForData()
    {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = inputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Log.d("e", data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void IntentPrint(String txt)
    {
        byte[] buffer = txt.getBytes();
        byte[] PrintHeader = { (byte) 0xAA, 0x55,2,0 };
        PrintHeader[3]=(byte) buffer.length;
        InitPrinter();
        if(PrintHeader.length>128)
        {
            value+="\nValue is more than 128 size\n";
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {

                outputStream.write(txt.getBytes());
                outputStream.close();
                socket.close();
            }
            catch(Exception ex)
            {
                value+=ex.toString()+ "\n" +"Excep IntentPrint \n";
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                Log.d("e", value);
            }
        }

    }

            public void IntentPrint2(String txt)
            {
                byte[] buffer = txt.getBytes();
                byte[] PrintHeader = { (byte) 0xAA, 0x55,2,0 };
                PrintHeader[3]=(byte) buffer.length;
                InitPrinter2();
                if(PrintHeader.length>128)
                {
                    value+="\nValue is more than 128 size\n";
                    Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                }
                else
                {
                    try
                    {

                        outputStream.write(txt.getBytes());
                        outputStream.close();
                        socket.close();
                    }
                    catch(Exception ex)
                    {
                        value+=ex.toString()+ "\n" +"Excep IntentPrint \n";
                        Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                        Log.d("e", value);
                    }
                }

            }



    protected void printBill() {

        if(btsocket == null){

            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);

            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);

        }

        else{

            OutputStream opstream = null;

            try {

                opstream = btsocket.getOutputStream();

            } catch (IOException e) {

                e.printStackTrace();

            }

            outputStream = opstream;



            //print command

            try {

                try {

                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

                outputStream = btsocket.getOutputStream();

                byte[] printformat = new byte[]{0x1B,0x21,0x03};

                outputStream.write(printformat);





                printCustom("Fair Group BD",2,1);

                printCustom("Pepperoni Foods Ltd.",0,1);

                printPhoto(R.drawable.ic_menu_camera);

                printCustom("H-123, R-123, Dhanmondi, Dhaka-1212",0,1);

                printCustom("Hot Line: +88000 000000",0,1);

                printCustom("Vat Reg : 0000000000,Mushak : 11",0,1);

                String dateTime[] = getDateTime();

                printText(leftRightAlign(dateTime[0], dateTime[1]));

                printText(leftRightAlign("Qty: Name" , "Price "));

                printCustom(new String(new char[32]).replace("\0", "."),0,1);

                printText(leftRightAlign("Total" , "2,0000/="));

                printNewLine();

                printCustom("Thank you for coming & we look",0,1);

                printCustom("forward to serve you again",0,1);

                printNewLine();

                printNewLine();



                outputStream.flush();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }



    protected void printDemo() {

        if(btsocket == null){

            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);

            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);

        }

        else{

            OutputStream opstream = null;

            try {

                opstream = btsocket.getOutputStream();

            } catch (IOException e) {

                e.printStackTrace();

            }

            outputStream = opstream;



            //print command

            try {

                try {

                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

                outputStream = btsocket.getOutputStream();



                byte[] printformat = { 0x1B, 0*21, FONT_TYPE };

                //outputStream.write(printformat);



                //print title

                printUnicode();

                //print normal text

                printCustom("makan",0,0);

                printPhoto(R.drawable.ic_menu_camera);

                printNewLine();

                printText("     >>>>   Thank you  <<<<     "); // total 32 char in a single line

                //resetPrint(); //reset printer

                printUnicode();

                printNewLine();

                printNewLine();



                outputStream.flush();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }



    //print custom

    private void printCustom(String msg, int size, int align) {

        //Print config "mode"

        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text

        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text

        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text

        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text

        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text

        try {

            switch (size){

                case 0:

                    outputStream.write(cc);

                    break;

                case 1:

                    outputStream.write(bb);

                    break;

                case 2:

                    outputStream.write(bb2);

                    break;

                case 3:

                    outputStream.write(bb3);

                    break;

            }



            switch (align){

                case 0:

                    //left align

                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);

                    break;

                case 1:

                    //center align

                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);

                    break;

                case 2:

                    //right align

                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);

                    break;

            }

            outputStream.write(msg.getBytes());

            outputStream.write(PrinterCommands.LF);

            //outputStream.write(cc);

            //printNewLine();

        } catch (IOException e) {

            e.printStackTrace();

        }



    }



    //print photo

    public void printPhoto(int img) {

        try {

            Bitmap bmp = BitmapFactory.decodeResource(getResources(),

                    img);

            if(bmp!=null){

                byte[] command = Utils.decodeBitmap(bmp);

                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);

                printText(command);

            }else{

                Log.e("Print Photo error", "the file isn't exists");

            }

        } catch (Exception e) {

            e.printStackTrace();

            Log.e("PrintTools", "the file isn't exists");

        }

    }



    //print unicode

    public void printUnicode(){

        try {

            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);

            printText(Utils.UNICODE_TEXT);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }





    //print new line

    private void printNewLine() {

        try {

            outputStream.write(PrinterCommands.FEED_LINE);

        } catch (IOException e) {

            e.printStackTrace();

        }



    }



    public static void resetPrint() {

        try{

            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);

            outputStream.write(PrinterCommands.FS_FONT_ALIGN);

            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);

            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);

            outputStream.write(PrinterCommands.LF);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    //print text

    private void printText(String msg) {

        try {

            // Print normal text

            outputStream.write(msg.getBytes());

        } catch (IOException e) {

            e.printStackTrace();

        }



    }



    //print byte[]

    private void printText(byte[] msg) {

        try {

            // Print normal text

            outputStream.write(msg);

            printNewLine();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }





    private String leftRightAlign(String str1, String str2) {

        String ans = str1 +str2;

        if(ans.length() <31){

            int n = (31 - str1.length() + str2.length());

            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;

        }

        return ans;

    }





    private String[] getDateTime() {

        final Calendar c = Calendar.getInstance();

        String dateTime [] = new String[2];

        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);

        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);

        return dateTime;

    }





}
