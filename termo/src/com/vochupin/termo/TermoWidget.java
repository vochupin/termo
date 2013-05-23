package com.vochupin.termo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TermoWidget extends AppWidgetProvider {
	private static final String TAG = TermoWidget.class.getSimpleName();

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		Log.i(TAG, "onUpdate method called");

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(), UpdateService.class);

		// Update the widgets via the service
		context.startService(intent);
	}
}
