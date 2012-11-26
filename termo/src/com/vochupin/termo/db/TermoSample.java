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
	
	
}
