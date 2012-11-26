package com.vochupin.termo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TermoSQLiteHelper extends SQLiteOpenHelper {

	private static final String TAG = TermoSQLiteHelper.class.getSimpleName();

	public static final String TABLE_SAMPLES = "termosamples";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TEMPERATURE = "temperature";
	public static final String COLUMN_TREND = "trend";
	public static final String COLUMN_DATETIME = "datetime";

	private static final String DATABASE_NAME = "commments.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SAMPLES + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_TEMPERATURE + " real not null, " + 
			COLUMN_TREND + " string not null" + 
			COLUMN_DATETIME + " string not null);";

	public TermoSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAMPLES);
		onCreate(db);
	}
}
