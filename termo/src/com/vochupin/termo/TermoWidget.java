package com.vochupin.termo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TermoWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    	 Log.w("LOG", "onUpdate method called");
    	    // Get all ids
    	    ComponentName thisWidget = new ComponentName(context, TermoWidget.class);
    	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

    	    // Build the intent to call the service
    	    Intent intent = new Intent(context.getApplicationContext(), UpdateService.class);
    	    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

    	    // Update the widgets via the service
    	    context.startService(intent);
    }
}
