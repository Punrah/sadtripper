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

import com.tripper.sadtripper.persistence.Farmer;

import java.util.ArrayList;


public class FarmerSQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = FarmerSQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "sadtripperfarmer";

	// Login table name
	private static final String TABLE_USER = "farmer";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_FARMERCODE = "FarmerCode";
	private static final String KEY_FIELDCODE = "FieldCode";
	private static final String KEY_CROP = "Crop";
	private static final String KEY_LOCATION = "Location";
	private static final String KEY_PLANTSIZE = "PlantSize";
	private static final String KEY_SPACING = "Spacing";
	private static final String KEY_AREA = "Area";
	private static final String KEY_YIELDWET = "YieldWet";
	private static final String KEY_YILEDDRIED = "YieldDried";
	private static final String KEY_CUC = "CUC";
	private static final String KEY_ICS = "ICS";
	private static final String KEY_STATUS = "Status";
	private static final String KEY_IDWEBSOL = "IdWebsol";
	private static final String KEY_FARMERNAME = "FarmerName";
	private static final String KEY_GENERATE = "generate";


    private FarmerSQLiteHandler db;
    private SessionManager session;

	public FarmerSQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_FARMER_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " TEXT,"
				+ KEY_FARMERCODE + " TEXT,"
				+ KEY_FIELDCODE + " TEXT,"
				+ KEY_CROP + " TEXT,"
				+ KEY_LOCATION + " TEXT,"
				+ KEY_PLANTSIZE + " TEXT,"
				+ KEY_SPACING + " TEXT,"
				+ KEY_AREA + " TEXT,"
				+ KEY_YIELDWET + " TEXT,"
				+ KEY_YILEDDRIED + " TEXT,"
				+ KEY_CUC + " TEXT,"
				+ KEY_ICS + " TEXT,"
				+ KEY_STATUS + " TEXT,"
				+ KEY_IDWEBSOL + " TEXT,"
				+ KEY_FARMERNAME + " TEXT,"
				+ KEY_GENERATE + " TEXT" + ")";
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
	public void addUser(Farmer user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, user.getId());
		values.put(KEY_FARMERCODE, user.getFarmerCode()); // Name
		values.put(KEY_FIELDCODE, user.getFieldCode()); // Email
		values.put(KEY_CROP, user.getCrop());
		values.put(KEY_LOCATION,user.getLocation());
		values.put(KEY_PLANTSIZE,user.getPlantSize());
		values.put(KEY_SPACING,user.getSpacing());
		values.put(KEY_AREA,user.getArea());
		values.put(KEY_YIELDWET,user.getYieldWet());
		values.put(KEY_YILEDDRIED,user.getYieldDried());
		values.put(KEY_CUC,user.getCuc());
		values.put(KEY_ICS,user.getIcs());
		values.put(KEY_STATUS,user.getStatus());
		values.put(KEY_IDWEBSOL,user.getIdWebsol());
		values.put(KEY_FARMERNAME,user.getFarmerName());
		values.put(KEY_GENERATE,user.getGenerate());

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	public void updateUser(Farmer user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, user.getId());
		values.put(KEY_FARMERCODE, user.getFarmerCode()); // Name
		values.put(KEY_FIELDCODE, user.getFieldCode()); // Email
		values.put(KEY_CROP, user.getCrop());
		values.put(KEY_LOCATION,user.getLocation());
		values.put(KEY_PLANTSIZE,user.getPlantSize());
		values.put(KEY_SPACING,user.getSpacing());
		values.put(KEY_AREA,user.getArea());
		values.put(KEY_YIELDWET,user.getYieldWet());
		values.put(KEY_YILEDDRIED,user.getYieldDried());
		values.put(KEY_CUC,user.getCuc());
		values.put(KEY_ICS,user.getIcs());
		values.put(KEY_STATUS,user.getStatus());
		values.put(KEY_IDWEBSOL,user.getIdWebsol());
		values.put(KEY_FARMERNAME,user.getFarmerName());
		values.put(KEY_GENERATE,user.getGenerate());

		// Inserting Row
		long id = db.update(TABLE_USER, values, KEY_ID +"="+ user.getId(),null);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */

	public ArrayList<Farmer> getAllFarmer() {

		ArrayList<Farmer> listFarmer= new ArrayList<>() ;
		String selectQuery = "SELECT  * FROM " + TABLE_USER +" Order by "+KEY_FARMERCODE+","+KEY_FIELDCODE+" ASC" ;

		Log.d(TAG, "Fetching user from Sqlite: " +selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row

		while (cursor.moveToNext()) {
			Farmer user = new Farmer();
			user.id=(cursor.getString(cursor.getColumnIndex(KEY_ID)));
			user.farmerCode=cursor.getString(cursor.getColumnIndex(KEY_FARMERCODE));
			user.fieldCode =cursor.getString(cursor.getColumnIndex(KEY_FIELDCODE));
			user.crop=cursor.getString(cursor.getColumnIndex(KEY_CROP));
			user.location =cursor.getString(cursor.getColumnIndex(KEY_LOCATION));
			user.plantSize =cursor.getString(cursor.getColumnIndex(KEY_PLANTSIZE));
			user.spacing =cursor.getString(cursor.getColumnIndex(KEY_SPACING));
			user.area =cursor.getString(cursor.getColumnIndex(KEY_AREA));
			user.yieldWet =cursor.getString(cursor.getColumnIndex(KEY_YIELDWET));
			user.yieldDried =cursor.getString(cursor.getColumnIndex(KEY_YILEDDRIED));
			user.cuc =cursor.getString(cursor.getColumnIndex(KEY_CUC));
			user.ics =cursor.getString(cursor.getColumnIndex(KEY_ICS));
			user.status =cursor.getString(cursor.getColumnIndex(KEY_STATUS));
			user.idWebsol =cursor.getString(cursor.getColumnIndex(KEY_IDWEBSOL));
			user.farmerName =cursor.getString(cursor.getColumnIndex(KEY_FARMERNAME));
			user.generate =cursor.getString(cursor.getColumnIndex(KEY_GENERATE));
			listFarmer.add(user);

		}
		cursor.close();
		db.close();

		return listFarmer;
	}


	public ArrayList<Farmer> getFarmerByCode(String idFarmer) {

		ArrayList<Farmer> listFarmer= new ArrayList<>() ;
		String selectQuery = "SELECT  * FROM " + TABLE_USER +" where "+KEY_FARMERCODE+"='"+idFarmer+"'";

		Log.d(TAG, "Fetching user from Sqlite: " +selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row

		while (cursor.moveToNext()) {
			Farmer user = new Farmer();
			user.id=(cursor.getString(cursor.getColumnIndex(KEY_ID)));
			user.farmerCode=cursor.getString(cursor.getColumnIndex(KEY_FARMERCODE));
			user.fieldCode =cursor.getString(cursor.getColumnIndex(KEY_FIELDCODE));
			user.crop=cursor.getString(cursor.getColumnIndex(KEY_CROP));
			user.location =cursor.getString(cursor.getColumnIndex(KEY_LOCATION));
			user.plantSize =cursor.getString(cursor.getColumnIndex(KEY_PLANTSIZE));
			user.spacing =cursor.getString(cursor.getColumnIndex(KEY_SPACING));
			user.area =cursor.getString(cursor.getColumnIndex(KEY_AREA));
			user.yieldWet =cursor.getString(cursor.getColumnIndex(KEY_YIELDWET));
			user.yieldDried =cursor.getString(cursor.getColumnIndex(KEY_YILEDDRIED));
			user.cuc =cursor.getString(cursor.getColumnIndex(KEY_CUC));
			user.ics =cursor.getString(cursor.getColumnIndex(KEY_ICS));
			user.status =cursor.getString(cursor.getColumnIndex(KEY_STATUS));
			user.idWebsol =cursor.getString(cursor.getColumnIndex(KEY_IDWEBSOL));
			user.farmerName =cursor.getString(cursor.getColumnIndex(KEY_FARMERNAME));
			user.generate =cursor.getString(cursor.getColumnIndex(KEY_GENERATE));
			listFarmer.add(user);

		}
		cursor.close();
		db.close();

		return listFarmer;
	}

	public ArrayList<String> getFieldStringByCode(String idFarmer) {

		ArrayList<String> listFarmer= new ArrayList<>() ;
		String selectQuery = "SELECT  * FROM " + TABLE_USER +" where "+KEY_FARMERCODE+"='"+idFarmer+"'";

		Log.d(TAG, "Fetching user from Sqlite: " +selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row

		while (cursor.moveToNext()) {
			String user = new String();
			user =cursor.getString(cursor.getColumnIndex(KEY_FIELDCODE));

			listFarmer.add(user);

		}
		cursor.close();
		db.close();

		return listFarmer;
	}


	public Farmer getFieldByCode(String idFarmer, String idField) {
		Farmer user = new Farmer();
		String selectQuery = "SELECT  * FROM " + TABLE_USER +" where "+KEY_FARMERCODE+"='"+idFarmer+"'" +" and "+KEY_FIELDCODE+"='"+idField+"'";

		Log.d(TAG, "Fetching user from Sqlite: " +selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.id=(cursor.getString(cursor.getColumnIndex(KEY_ID)));
			user.farmerCode=cursor.getString(cursor.getColumnIndex(KEY_FARMERCODE));
			user.fieldCode =cursor.getString(cursor.getColumnIndex(KEY_FIELDCODE));
			user.crop=cursor.getString(cursor.getColumnIndex(KEY_CROP));
			user.location =cursor.getString(cursor.getColumnIndex(KEY_LOCATION));
			user.plantSize =cursor.getString(cursor.getColumnIndex(KEY_PLANTSIZE));
			user.spacing =cursor.getString(cursor.getColumnIndex(KEY_SPACING));
			user.area =cursor.getString(cursor.getColumnIndex(KEY_AREA));
			user.yieldWet =cursor.getString(cursor.getColumnIndex(KEY_YIELDWET));
			user.yieldDried =cursor.getString(cursor.getColumnIndex(KEY_YILEDDRIED));
			user.cuc =cursor.getString(cursor.getColumnIndex(KEY_CUC));
			user.ics =cursor.getString(cursor.getColumnIndex(KEY_ICS));
			user.status =cursor.getString(cursor.getColumnIndex(KEY_STATUS));
			user.idWebsol =cursor.getString(cursor.getColumnIndex(KEY_IDWEBSOL));
			user.farmerName =cursor.getString(cursor.getColumnIndex(KEY_FARMERNAME));
			user.generate =cursor.getString(cursor.getColumnIndex(KEY_GENERATE));

		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
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
