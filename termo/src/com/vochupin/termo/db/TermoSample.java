package com.vochupin.termo.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class TermoSample {
	private boolean initialized;
	private float temperature;
	private int trend;
	private Date sampleTime;

	private static Map<String, Integer> trendMap = new HashMap<String, Integer>(){{
		put("+", +1); put("-", -1);
	}};
	
	public TermoSample() {
		initialized = false;
		sampleTime = Calendar.getInstance().getTime();
	}

	public TermoSample(Date sampleTime, float temperature, int trend) {
		initialized = true;
		this.sampleTime = sampleTime;
		this.temperature = temperature;
		this.trend = trend;		
	}

	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public int getTrend() {
		return trend;
	}
	public void setTrend(int trend) {
		this.trend = trend;
	}
	public Date getSampleTime() {
		return sampleTime;
	}
	public void setSampleTime(Date sampleTime) {
		this.sampleTime = sampleTime;
	}
	@Override
	public String toString() {
		if(initialized){
			String retval = "t° = " + temperature;
			retval += " Δt = " + trend;
			retval += " " + sampleTime.toLocaleString();
			return retval;
		}else{
			return "Температура неизвестна.";
		}

	}
	
	public static TermoSample fromJson(String json, TermoDataSource tds) throws ParseException, JSONException {
		TermoSample retval = null;

		JSONObject jobj = new JSONObject(json);

		float temperature = (float)jobj.getDouble("current_temp");

		String trendString = jobj.getString("current_temp_change").trim();
		int trend = trendMap.get(trendString);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:dd.MM.yyyy");
		String sampleTimeString = jobj.getString("current_time") + ":" + jobj.getString("current_date");

		Date sampleTime = sdf.parse(sampleTimeString);

		retval = tds.createSample(temperature, trend, sampleTime);
		return retval;
	}
}
