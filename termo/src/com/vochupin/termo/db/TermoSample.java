package com.vochupin.termo.db;

import java.util.Date;

public class TermoSample {
	private long id;
	private float temperature;
	private int trend;
	private Date sampleTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
		return "TermoSample [id=" + id + ", temperature=" + temperature
				+ ", trend=" + trend + ", sampleTime=" + sampleTime + "]";
//		retval = "t° = " + jobj.getDouble("current_temp");
//		retval += "     Δt = " + jobj.getString("current_temp_change");
//		retval += "\n" + jobj.getString("current_date") + " " + jobj.getString("current_time");

	}
	
	
	
}
