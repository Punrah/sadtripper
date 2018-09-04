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

import com.tripper.sadtripper.persistence.Item;
import com.tripper.sadtripper.persistence.Transaction;

import java.util.ArrayList;


public class ItemSQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = ItemSQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "sadtripperitem";

	// Login table name
	private static final String TABLE_USER = "`item`";

	// Login Table Columns names
	private static final String itemCodeKey = "e";
	private static final String descriptionKey = "f";
	private static final String packingKey = "i";
	private static final String priceKey = "j";



    private ItemSQLiteHandler db;
    private SessionManager session;

	public ItemSQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_FARMER_TABLE = "CREATE TABLE " + TABLE_USER + "("

				+ itemCodeKey + " TEXT,"
				+ descriptionKey + " TEXT,"
				+ priceKey + " TEXT,"

				+ packingKey + " TEXT" + ")";

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
	public void addUser(Item user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(itemCodeKey,user.getItemCode());
		values.put(descriptionKey,user.getDescription());

		values.put(packingKey,user.getPacking());
		values.put(priceKey,user.getPrice());



		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	public void updateUser(Item user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(itemCodeKey,user.getItemCode());
		values.put(descriptionKey,user.getDescription());

		values.put(packingKey,user.getPacking());
		values.put(priceKey,user.getPrice());



		// Inserting Row
		long id = db.update(TABLE_USER, values, itemCodeKey +"="+ user.getItemCode(),null);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public ArrayList<Item> getAllTransaction() {

		ArrayList<Item> listFarmer= new ArrayList<>() ;
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		Log.d(TAG, "Fetching user from Sqlite: " +selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row

		while (cursor.moveToNext()) {
			Item user = new Item();
			user.ItemCode =cursor.getString(cursor.getColumnIndex(itemCodeKey));
			user.Description =cursor.getString(cursor.getColumnIndex(descriptionKey));
			user.Packing =cursor.getString(cursor.getColumnIndex(packingKey));
			user.Price =Double.parseDouble(cursor.getString(cursor.getColumnIndex(priceKey)));
			listFarmer.add(user);

		}
		cursor.close();
		db.close();

		return listFarmer;
	}

	public ArrayList<String> getItemCodeList() {

		ArrayList<String> listFarmer= new ArrayList<>() ;
		String selectQuery = "SELECT "+itemCodeKey+" FROM " + TABLE_USER;

		Log.d(TAG, "Fetching user from Sqlite: " +selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row

		while (cursor.moveToNext()) {
			String user = new String();
			user =cursor.getString(cursor.getColumnIndex(itemCodeKey));
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
