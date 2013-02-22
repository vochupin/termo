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
	private final String GRAPH_COLOR = "GRAPH_COLOR";
	private final String LINK_COLOR = "LINK_COLOR";
	private final String DATE_COLOR = "DATE_COLOR";
	private final String MAXMIN_COLOR = "MAXMIN_COLOR";
	private final String MESSAGE_COLOR = "MESSAGE_COLOR";
	
	private int tempColdColor;
	private int tempWarmColor;
	private int backgroundColor;
	private int gridColor;
	private int graphColor;
	private int linkColor;
	private int dateColor;
	private int maxminColor;
	private int messageColor;
	
	private final SharedPreferences shPref;
	private SharedPreferences.Editor editor;
	
	public Preferences(Context context){
		shPref = PreferenceManager.getDefaultSharedPreferences(context);
		tempColdColor = shPref.getInt(TEMP_COLD_COLOR, Color.BLUE);
		tempWarmColor = shPref.getInt(TEMP_WARM_COLOR, Color.RED);
		backgroundColor = shPref.getInt(BACKGROUND_COLOR, Color.LTGRAY);
		gridColor = shPref.getInt(GRID_COLOR, Color.BLACK);
		graphColor = shPref.getInt(GRAPH_COLOR, Color.GREEN);
		linkColor = shPref.getInt(LINK_COLOR, Color.BLACK);
		dateColor = shPref.getInt(DATE_COLOR, Color.BLACK);
		maxminColor = shPref.getInt(MAXMIN_COLOR, Color.BLACK);
		messageColor = shPref.getInt(MESSAGE_COLOR, Color.BLACK);
	}

	public int getTempColdColor() {
		return tempColdColor;
	}

	public void setTempColdColor(int tempColdColor) {
		putInt(TEMP_COLD_COLOR, tempColdColor);
		this.tempColdColor = tempColdColor;
	}

	public int getTempWarmColor() {
		return tempWarmColor;
	}

	public void setTempWarmColor(int tempWarmColor) {
		putInt(TEMP_WARM_COLOR, tempWarmColor);
		this.tempWarmColor = tempWarmColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		putInt(BACKGROUND_COLOR, backgroundColor);
		this.backgroundColor = backgroundColor;
	}

	public int getGridColor() {
		return gridColor;
	}

	public void setGridColor(int gridColor) {
		putInt(GRID_COLOR, gridColor);
		this.gridColor = gridColor;
	}

	public int getLinkColor() {
		return linkColor;
	}

	public void setLinkColor(int linkColor) {
		putInt(LINK_COLOR, linkColor);
		this.linkColor = linkColor;
	}

	public int getDateColor() {
		return dateColor;
	}

	public void setDateColor(int dateColor) {
		putInt(DATE_COLOR, dateColor);
		this.dateColor = dateColor;
	}

	public int getMaxminColor() {
		return maxminColor;
	}

	public void setMaxminColor(int maxminColor) {
		putInt(MAXMIN_COLOR, maxminColor);
		this.maxminColor = maxminColor;
	}

	public int getGraphColor() {
		return graphColor;
	}

	public void setGraphColor(int graphColor) {
		putInt(GRAPH_COLOR, graphColor);
		this.graphColor = graphColor;
	}

	public int getMessageColor() {
		return messageColor;
	}

	public void setMessageColor(int messageColor) {
		putInt(MESSAGE_COLOR, messageColor);
		this.messageColor = messageColor;
	}	

	public void commit(){
		if(editor == null) shPref.edit();
		editor.commit();		
	}

	private void putInt(String key, int value){
		if(editor == null) editor = shPref.edit();
		editor.putInt(key, value);		
	}

}