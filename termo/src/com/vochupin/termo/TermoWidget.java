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

	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	 
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
    	    
//         RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
//
//         Intent active = new Intent(context, TermoWidget.class);
//         active.setAction(ACTION_WIDGET_RECEIVER);
//         active.putExtra("msg", "Hello Habrahabr");
//
//         PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
//
//         remoteViews.setOnClickPendingIntent(R.id.btnStart, actionPendingIntent);
//         remoteViews.setTextViewText(R.id.tvOutput, "m: " + System.currentTimeMillis());
//
//         appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
//         Log.i("", "BLEAT");
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//         final String action = intent.getAction();
//         if (ACTION_WIDGET_RECEIVER.equals(action)) {
//              String msg = "null";
//              try {
//                    msg = intent.getStringExtra("msg") + System.currentTimeMillis();
//              } catch (NullPointerException e) {
//                    Log.e("Error", "msg = null");
//              }
//              Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//         }
//
//         AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//         ComponentName thisWidget = new ComponentName(context.getApplicationContext(), TermoWidget.class);
//         int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
//         
//         
//         super.onReceive(context, intent);
//   }

}
