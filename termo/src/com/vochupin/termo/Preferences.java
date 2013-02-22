package com.vochupin.termo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class Preferences {
	private final String TEMP_COLD_COLOR = "TEMP_COLD_COLOR";
	private final String TEMP_WARM_COLOR = "TEMP_WARM_COLOR";
	private final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
	private final String GRID_COLOR = "GRID_COLOR";
	private final String LINK_COLOR = "LINK_COLOR";
	private final String DATE_COLOR = "DATE_COLOR";
	private final String MAXMIN_COLOR = "MAXMIN_COLOR";
	
	private int tempColdColor;
	private int tempWarmColor;
	private int backgroundColor;
	private int gridColor;
	private int linkColor;
	private int dateColor;
	private int maxminColor;
	
	private final SharedPreferences shPref;
	
	public Preferences(Context context){
		shPref = PreferenceManager.getDefaultSharedPreferences(context);
		int tempColdColor = shPref.getInt(TEMP_COLD_COLOR, -1);
		int tempWarmColor = shPref.getInt(TEMP_WARM_COLOR, -1);
		int backgroundColor = shPref.getInt(BACKGROUND_COLOR, -1);
		int gridColor = shPref.getInt(GRID_COLOR, -1);
		int linkColor = shPref.getInt(LINK_COLOR, -1);
		int dateColor = shPref.getInt(DATE_COLOR, -1);
		int maxminColor = shPref.getInt(MAXMIN_COLOR, -1);
	}

	public int getTempColdColor() {
		return tempColdColor;
	}

	public void setTempColdColor(int tempColdColor) {
		this.tempColdColor = tempColdColor;
	}

	public int getTempWarmColor() {
		return tempWarmColor;
	}

	public void setTempWarmColor(int tempWarmColor) {
		this.tempWarmColor = tempWarmColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getGridColor() {
		return gridColor;
	}

	public void setGridColor(int gridColor) {
		this.gridColor = gridColor;
	}

	public int getLinkColor() {
		return linkColor;
	}

	public void setLinkColor(int linkColor) {
		this.linkColor = linkColor;
	}

	public int getDateColor() {
		return dateColor;
	}

	public void setDateColor(int dateColor) {
		this.dateColor = dateColor;
	}

	public int getMaxminColor() {
		return maxminColor;
	}

	public void setMaxminColor(int maxminColor) {
		this.maxminColor = maxminColor;
	}

	public void commit(){
		
	}
}
