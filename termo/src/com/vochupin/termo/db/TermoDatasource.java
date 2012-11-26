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

public class TermoDatasource {

	// Database fields
	private SQLiteDatabase database;
	private TermoSQLiteHelper dbHelper;
	private String[] allColumns = { TermoSQLiteHelper.COLUMN_ID, 
			TermoSQLiteHelper.COLUMN_TEMPERATURE,
			TermoSQLiteHelper.COLUMN_TREND,
			TermoSQLiteHelper.COLUMN_DATETIME};

	public TermoDatasource(Context context) {
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
		values.put(TermoSQLiteHelper.COLUMN_TEMPERATURE, temperature);
		values.put(TermoSQLiteHelper.COLUMN_TREND, trend);
		values.put(TermoSQLiteHelper.COLUMN_TEMPERATURE, DateFormat.getInstance().format(sampleTime.toGMTString()));
		long insertId = database.insert(TermoSQLiteHelper.TABLE_SAMPLES, null, values);
		Cursor cursor = database.query(TermoSQLiteHelper.TABLE_SAMPLES,
				allColumns, TermoSQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		TermoSample newSample = sampleFromCursor(cursor);
		cursor.close();
		return newSample;
	}

	public void deleteSample(TermoSample sample) {
		long id = sample.getId();
		System.out.println("Sample deleted with id: " + id);
		database.delete(TermoSQLiteHelper.TABLE_SAMPLES, TermoSQLiteHelper.COLUMN_ID + " = " + id, null);
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
		TermoSample sample = new TermoSample();
		sample.setId(cursor.getLong(0));
		sample.setTemperature(cursor.getFloat(1));
		sample.setTrend(cursor.getInt(2));
		
		String sampleTimeString = cursor.getString(3);
		Date sampleTime = DateFormat.getInstance().parse(sampleTimeString);
		sample.setSampleTime(sampleTime);
		
		return sample;
	}
}

