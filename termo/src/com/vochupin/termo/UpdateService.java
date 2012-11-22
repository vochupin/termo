package com.vochupin.termo;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class UpdateService extends Service {

	private static final String TAG = UpdateService.class.getSimpleName();
	private static final String TERMO_SERVER = "http://termo.tomsk.ru/";
	private static final String TERMO_JSON_INFORMER = "data.json";

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "Update service started.");

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		Log.w(TAG, "Number of ids to update (from intent): " + String.valueOf(allWidgetIds.length));

		//Request JSON with temperature
		Client client = new Client(TERMO_SERVER);
		String tempJson = client.getTemperatureJson(TERMO_JSON_INFORMER);
		
		String temperature = parseTermoResponse(tempJson);
		
		updateWidgets(appWidgetManager, allWidgetIds, temperature);
		
		stopSelf();

		super.onStart(intent, startId);
	}

	private void updateWidgets(AppWidgetManager appWidgetManager, int[] allWidgetIds, String temperature) {
		for (int widgetId : allWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.main);
			remoteViews.setTextViewText(R.id.tvOutput, temperature);
			
//			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//		    String value = prefs.getString("", null);
//		    if(value != null)
//		    {
//		        // do stuff
//		    }

			enableUpdateOnClick(allWidgetIds, remoteViews);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	private void enableUpdateOnClick(int[] allWidgetIds,	RemoteViews remoteViews) {
		Intent clickIntent = new Intent(this.getApplicationContext(), TermoWidget.class);

		clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.tvOutput, pendingIntent);
	}

	private String parseTermoResponse(String temperature) {
		String retval;
		try {
			JSONObject jobj = new JSONObject(temperature);
			
			retval = "t° = " + jobj.getDouble("current_temp");
			retval += "     Δt = " + jobj.getString("current_temp_change");
			retval += "\n" + jobj.getString("current_date") + " " + jobj.getString("current_time");
			
		} catch (JSONException e) {
			e.printStackTrace();
			retval = "JSON error.";
		}
		return retval;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
} 
