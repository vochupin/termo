package com.vochupin.termo.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TermoDataSource {

	private static final String TAG = TermoDataSource.class.getSimpleName();
	// Database fields
	private SQLiteDatabase database;
	private TermoSQLiteHelper dbHelper;
	private String[] allColumns = { 
			TermoSQLiteHelper.COLUMN_DATETIME,
			TermoSQLiteHelper.COLUMN_TEMPERATURE,
			TermoSQLiteHelper.COLUMN_TREND
			};

	public TermoDataSource(Context context) {
		dbHelper = new TermoSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public TermoSample createSample(float temperature, int trend, Date sampleTime) throws ParseException {
		ContentValues values = new ContentValues();
		long sampleTimestamp = sampleTime.getTime();
		values.put(TermoSQLiteHelper.COLUMN_DATETIME, sampleTimestamp);
		values.put(TermoSQLiteHelper.COLUMN_TEMPERATURE, temperature);
		values.put(TermoSQLiteHelper.COLUMN_TREND, trend);

		TermoSample newSample = queryTermoSample(sampleTimestamp);
		if(newSample != null) {
			Log.i(TAG, "Sample: " + sampleTimestamp + " is already in base. No need to insert.");
			return newSample;
		}
		
		long insertId = database.insert(TermoSQLiteHelper.TABLE_SAMPLES, null, values);
		newSample = queryTermoSample(insertId);
		return newSample;
	}

	private TermoSample queryTermoSample(long insertId) throws ParseException {
		Cursor cursor = database.query(TermoSQLiteHelper.TABLE_SAMPLES,	allColumns, TermoSQLiteHelper.COLUMN_DATETIME + " = " + insertId, null, null, null, null);
		if(cursor.moveToFirst() == false) return null;
		TermoSample newSample = sampleFromCursor(cursor);
		cursor.close();
		return newSample;
	}

	public void deleteSample(TermoSample sample) {
		long id = sample.getSampleTime().getTime();
		System.out.println("Sample deleted with id: " + id);
		database.delete(TermoSQLiteHelper.TABLE_SAMPLES, TermoSQLiteHelper.COLUMN_DATETIME + " = " + id, null);
	}

	public List<TermoSample> getAllSamples() throws ParseException {
		List<TermoSample> comments = new ArrayList<TermoSample>();

		Cursor cursor = database.query(TermoSQLiteHelper.TABLE_SAMPLES,	allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TermoSample sample = sampleFromCursor(cursor);
			comments.add(sample);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return comments;
	}

	private TermoSample sampleFromCursor(Cursor cursor) throws ParseException {
		Date sampleTime = new Date(cursor.getLong(0));
		TermoSample sample = new TermoSample(sampleTime, cursor.getFloat(1), cursor.getInt(2));
		return sample;
	}

	public TermoSample getLastSamples(int numberOf) throws ParseException {

		Cursor cursor = database.query(TermoSQLiteHelper.TABLE_SAMPLES,	allColumns, null, null, null, null, "1 DESC LIMIT " + numberOf);

		if(cursor.moveToFirst() == false){
			return null;
		}
		return sampleFromCursor(cursor);
	}
}

