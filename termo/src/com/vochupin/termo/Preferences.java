package com.vochupin.termo;

import java.util.AbstractMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class Preferences {
	
	//FIXME this shit must be removed by bemap
	private static final String TEMP_COLD_COLOR = "TEMP_COLD_COLOR";
	private static final String TEMP_WARM_COLOR = "TEMP_WARM_COLOR";
	private static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
	private static final String GRID_COLOR = "GRID_COLOR";
	private static final String GRAPH_COLOR = "GRAPH_COLOR";
	private static final String LINK_COLOR = "LINK_COLOR";
	private static final String DATE_COLOR = "DATE_COLOR";
	private static final String MAXMIN_COLOR = "MAXMIN_COLOR";
	private static final String MESSAGE_COLOR = "MESSAGE_COLOR";
	
	public static class PartDescriptor{
		public final String humanReadableName;
		public final String persistanceId;
		private PartDescriptor(String humanReadableName, String persistanceId) {
			super();
			this.humanReadableName = humanReadableName;
			this.persistanceId = persistanceId;
		}
	}
	
	public static final PartDescriptor[] partDescriptors = new PartDescriptor[]{
		new PartDescriptor("÷вет температуры ниже нул€", TEMP_COLD_COLOR),
		new PartDescriptor("÷вет температуры выше нул€", TEMP_WARM_COLOR),
		new PartDescriptor("÷вет фона", BACKGROUND_COLOR),
		new PartDescriptor("÷вет сетки координат", GRID_COLOR),
		new PartDescriptor("÷вет графика", GRAPH_COLOR),
		new PartDescriptor("÷вет ссылки на сайт", LINK_COLOR),
		new PartDescriptor("÷вет времени последнего измерени€", DATE_COLOR),
		new PartDescriptor("÷вет макс/мин", MAXMIN_COLOR),
		new PartDescriptor("÷вет сообщени€ при запросе", MESSAGE_COLOR)
	};

	private static final int ID_TEMP_COLD_COLOR = 0;
	private static final int ID_TEMP_WARM_COLOR = 1;
	private static final int ID_BACKGROUND_COLOR = 2;
	private static final int ID_GRID_COLOR = 3;
	private static final int ID_GRAPH_COLOR = 4;
	private static final int ID_LINK_COLOR = 5;
	private static final int ID_DATE_COLOR = 6;
	private static final int ID_MAXMIN_COLOR = 7;
	private static final int ID_MESSAGE_COLOR = 8;

	private int[] colors = new int[partDescriptors.length];
		
	private final SharedPreferences shPref;
	private SharedPreferences.Editor editor;
		
	public Preferences(Context context){
		shPref = PreferenceManager.getDefaultSharedPreferences(context);
		colors[ID_TEMP_COLD_COLOR] = shPref.getInt(TEMP_COLD_COLOR, Color.BLUE);
		colors[ID_TEMP_WARM_COLOR] = shPref.getInt(TEMP_WARM_COLOR, Color.RED);
		colors[ID_BACKGROUND_COLOR] = shPref.getInt(BACKGROUND_COLOR, Color.LTGRAY);
		colors[ID_GRID_COLOR] = shPref.getInt(GRID_COLOR, Color.BLACK);
		colors[ID_GRAPH_COLOR] = shPref.getInt(GRAPH_COLOR, Color.GREEN);
		colors[ID_LINK_COLOR] = shPref.getInt(LINK_COLOR, Color.BLACK);
		colors[ID_DATE_COLOR] = shPref.getInt(DATE_COLOR, Color.BLACK);
		colors[ID_MAXMIN_COLOR] = shPref.getInt(MAXMIN_COLOR, Color.BLACK);
		colors[ID_MESSAGE_COLOR] = shPref.getInt(MESSAGE_COLOR, Color.BLACK);
	}
	
	public int getColorByIndex(int index){
		return colors[index];
	}

	public void setColorByIndex(int index, int color){
		colors[index] = color;
		PartDescriptor pd = partDescriptors[index];
		putInt(pd.persistanceId, color);
	}

	public int getTempColdColor() {
		return colors[ID_TEMP_COLD_COLOR];
	}

	public void setTempColdColor(int tempColdColor) {
		putInt(TEMP_COLD_COLOR, tempColdColor);
		this.colors[ID_TEMP_COLD_COLOR] = tempColdColor;
	}

	public int getTempWarmColor() {
		return colors[ID_TEMP_WARM_COLOR];
	}

	public void setTempWarmColor(int tempWarmColor) {
		putInt(TEMP_WARM_COLOR, tempWarmColor);
		this.colors[ID_TEMP_WARM_COLOR] = tempWarmColor;
	}

	public int getBackgroundColor() {
		return colors[ID_BACKGROUND_COLOR];
	}

	public void setBackgroundColor(int backgroundColor) {
		putInt(BACKGROUND_COLOR, backgroundColor);
		this.colors[ID_BACKGROUND_COLOR] = backgroundColor;
	}

	public int getGridColor() {
		return colors[ID_GRID_COLOR];
	}

	public void setGridColor(int gridColor) {
		putInt(GRID_COLOR, gridColor);
		this.colors[ID_GRID_COLOR] = gridColor;
	}

	public int getLinkColor() {
		return colors[ID_LINK_COLOR];
	}

	public void setLinkColor(int linkColor) {
		putInt(LINK_COLOR, linkColor);
		this.colors[ID_LINK_COLOR] = linkColor;
	}

	public int getDateColor() {
		return colors[ID_DATE_COLOR];
	}

	public void setDateColor(int dateColor) {
		putInt(DATE_COLOR, dateColor);
		this.colors[ID_DATE_COLOR] = dateColor;
	}

	public int getMaxminColor() {
		return colors[ID_MAXMIN_COLOR];
	}

	public void setMaxminColor(int maxminColor) {
		putInt(MAXMIN_COLOR, maxminColor);
		this.colors[ID_MAXMIN_COLOR] = maxminColor;
	}

	public int getGraphColor() {
		return colors[ID_GRAPH_COLOR];
	}

	public void setGraphColor(int graphColor) {
		putInt(GRAPH_COLOR, graphColor);
		this.colors[ID_GRAPH_COLOR] = graphColor;
	}

	public int getMessageColor() {
		return colors[ID_MESSAGE_COLOR];
	}

	public void setMessageColor(int messageColor) {
		putInt(MESSAGE_COLOR, messageColor);
		this.colors[ID_MESSAGE_COLOR] = messageColor;
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
