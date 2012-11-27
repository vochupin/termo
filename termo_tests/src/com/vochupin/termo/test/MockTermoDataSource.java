package com.vochupin.termo.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;

import com.vochupin.termo.db.TermoDataSource;
import com.vochupin.termo.db.TermoSQLiteHelper;
import com.vochupin.termo.db.TermoSample;

public class MockTermoDataSource extends TermoDataSource {

	public MockTermoDataSource(Context context) {
		super(context);
	}

	@Override
	public void open() throws SQLException {
	}

	@Override
	public void close() {
	}

	@Override
	public TermoSample createSample(float temperature, int trend, Date sampleTime) throws ParseException {
		TermoSample sample = new TermoSample();
		sample.setTemperature(temperature);
		sample.setTrend(trend);
		sample.setSampleTime(sampleTime);
		return sample;
	}
	
}
