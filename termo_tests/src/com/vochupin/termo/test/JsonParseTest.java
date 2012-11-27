package com.vochupin.termo.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.vochupin.termo.db.TermoSample;

import junit.framework.TestCase;

public class JsonParseTest extends AndroidTestCase {
	
	private String jsonNormal = "{" +
			"current_temp: \"-18.9\"," +
			"current_temp_change: \"+\"," + 
			"current_date: \"27.11.2012\"," +
			"current_time: \"10:29\"," +
			"url: \"http://termo.tomsk.ru/\"," +
			"description: \"Актуальная температура в г.Томск\"" +
		"}";

	private String jsonShortDateTime = "{" +
			"current_temp: \"-18.9\"," +
			"current_temp_change: \"+\"," + 
			"current_date: \"7.1.2012\"," +
			"current_time: \"0:29\"," +
			"url: \"http://termo.tomsk.ru/\"," +
			"description: \"Актуальная температура в г.Томск\"" +
		"}";
	
	public void jsonParseTest() throws ParseException, JSONException{
		MockTermoDataSource mtds = new MockTermoDataSource(getContext());

		TermoSample ts1 = TermoSample.fromJson(jsonNormal, mtds);
		assertEquals(-18.9F, ts1.getTemperature());
		assertEquals(+1, ts1.getTrend());

		TermoSample ts2 = TermoSample.fromJson(jsonShortDateTime, mtds);
		assertEquals(-18.9F, ts2.getTemperature());
		assertEquals(+1, ts2.getTrend());
	}
	
	public void testDateFormat() throws ParseException{
		Date d1 = Calendar.getInstance().getTime();
		String dstr = DateFormat.getInstance().format(d1);
		
		Date d2 = DateFormat.getInstance().parse(dstr);
	}
}
