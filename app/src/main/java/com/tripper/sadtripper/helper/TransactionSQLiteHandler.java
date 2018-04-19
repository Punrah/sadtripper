/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.tripper.sadtripper.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tripper.sadtripper.persistence.Transaction;

import java.util.ArrayList;


public class TransactionSQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = TransactionSQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "sadtrippertransaction";

	// Login table name
	private static final String TABLE_USER = "`transaction`";

	// Login Table Columns names
	private static final String dateKey = "a";
	private static final String farmerCodeKey = "b";
	private static final String fieldCodeKey = "c";
	private static final String farmerNameKey = "d";
	private static final String itemCodeKey = "e";
	private static final String descriptionKey = "f";
	private static final String priceKey = "g";
	private static final String qtykey = "h";
	private static final String packingKey = "i";
	private static final String noPoKey = "j";
	private static final String loNumberKey = "k";
	private static final String idWebsolKey = "l";
	private static final String transactionname = "m";



    private TransactionSQLiteHandler db;
    private SessionManager session;

	public TransactionSQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_FARMER_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ dateKey + " TEXT,"
				+ idWebsolKey + " TEXT,"
				+ farmerCodeKey + " TEXT,"
				+ farmerNameKey + " TEXT,"
				+ fieldCodeKey + " TEXT,"
				+ itemCodeKey + " TEXT,"
				+ descriptionKey + " TEXT,"
				+ priceKey + " TEXT,"
				+ qtykey + " TEXT,"
				+ packingKey + " TEXT,"
				+ noPoKey + " TEXT,"
				+ transactionname + " TEXT,"
				+ loNumberKey + " TEXT" + ")";
		db.execSQL(CREATE_FARMER_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(Transaction user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(dateKey, user.getDate());
		values.put(idWebsolKey, user.getIdWebsol()); // Name
		values.put(farmerCodeKey, user.getFarmerCode()); // Email
		values.put(farmerNameKey, user.getFarmerName());
		values.put(fieldCodeKey,user.getFieldCode());
		values.put(itemCodeKey,user.getItemCode());
		values.put(descriptionKey,user.getDescription());
		values.put(priceKey,user.getPrice());
		values.put(qtykey,user.getQty());
		values.put(packingKey,user.getPacking());
		values.put(noPoKey,user.getNoPo());
		values.put(loNumberKey,user.getLotnumber());
		values.put(transactionname,user.TransactionName);

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	public void updateUser(Transaction user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(dateKey, user.getDate());
		values.put(idWebsolKey, user.getIdWebsol()); // Name
		values.put(farmerCodeKey, user.getFarmerCode()); // Email
		values.put(farmerNameKey, user.getFarmerName());
		values.put(fieldCodeKey,user.getFieldCode());
		values.put(itemCodeKey,user.getItemCode());
		values.put(descriptionKey,user.getDescription());
		values.put(priceKey,user.getPrice());
		values.put(qtykey,user.getQty());
		values.put(packingKey,user.getPacking());
		values.put(noPoKey,user.getNoPo());
		values.put(loNumberKey,user.getLotnumber());
		values.put(transactionname,user.TransactionName);


		// Inserting Row
		long id = db.update(TABLE_USER, values, loNumberKey +"="+ user.getLotnumber(),null);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public ArrayList<Transaction> getAllTransaction() {

		ArrayList<Transaction> listFarmer= new ArrayList<>() ;
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		Log.d(TAG, "Fetching user from Sqlite: " +selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row

		while (cursor.moveToNext()) {
			Transaction user = new Transaction();
			user.Date=(cursor.getString(cursor.getColumnIndex(dateKey)));
			user.idWebsol=cursor.getString(cursor.getColumnIndex(idWebsolKey));
			user.FarmerCode =cursor.getString(cursor.getColumnIndex(farmerCodeKey));
			user.FarmerName=cursor.getString(cursor.getColumnIndex(farmerNameKey));
			user.FieldCode =cursor.getString(cursor.getColumnIndex(fieldCodeKey));
			user.ItemCode =cursor.getString(cursor.getColumnIndex(itemCodeKey));
			user.Description =cursor.getString(cursor.getColumnIndex(descriptionKey));
			user.Price =cursor.getString(cursor.getColumnIndex(priceKey));
			user.Qty =cursor.getString(cursor.getColumnIndex(qtykey));
			user.Packing =cursor.getString(cursor.getColumnIndex(packingKey));
			user.NoPo =cursor.getString(cursor.getColumnIndex(noPoKey));
			user.lotnumber =cursor.getString(cursor.getColumnIndex(loNumberKey));
			user.TransactionName =cursor.getString(cursor.getColumnIndex(transactionname));
			listFarmer.add(user);

		}
		cursor.close();
		db.close();

		return listFarmer;
	}





	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
