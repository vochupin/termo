package com.vochupin.termo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.vochupin.termo.db.TermoDataSource;
import com.vochupin.termo.db.TermoSample;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import 	android.os.Process;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateService extends Service {

	private static final String TAG = UpdateService.class.getSimpleName();
	private static final String TERMO_SERVER = "http://termo.tomsk.ru/";
	private static final String TERMO_JSON_INFORMER = "data.json";
	private static final String ALL_WIDGET_IDS = "allWidgetIds";

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			Log.i(TAG, "Update service started.");

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UpdateService.this.getApplicationContext());
			int[] allWidgetIds = msg.getData().getIntArray(ALL_WIDGET_IDS);
			Log.w(TAG, "Number of ids to update (from intent): " + String.valueOf(allWidgetIds.length));

			//Request JSON with temperature
			Client client = new Client(TERMO_SERVER);
			String tempJson = client.getTemperatureJson(TERMO_JSON_INFORMER);

			TermoDataSource tds = new TermoDataSource(UpdateService.this);
			tds.open();

			TermoSample ts = null;
			if(Client.NO_CONNECTION.equals(tempJson) == false){
				try {
					ts = TermoSample.fromJson(tempJson, tds);
				} catch (Exception e) {
					Log.e(TAG, "Parsing error: " + e.toString() + " when parse: " + tempJson);
					e.printStackTrace();
				}
			}
			
			if(ts == null){
				try {
					ts = tds.getLastSample();
				} catch (ParseException e) {
					Log.e(TAG, "Can't read last sample from base.");
					e.printStackTrace();
					ts = new TermoSample();
				}
			}

			updateWidgets(appWidgetManager, allWidgetIds, ts.toString());

			tds.close();

			stopSelf(msg.arg1);
			Log.i(TAG, "handler done");
		}
	}

	@Override
	public void onCreate() {
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UpdateService.this.getApplicationContext());
		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		updateWidgets(appWidgetManager, allWidgetIds, "Сейчас все будет!");
		
		Bundle bundle = new Bundle();
		bundle.putIntArray(ALL_WIDGET_IDS, allWidgetIds);
		msg.setData(bundle);

		mServiceHandler.sendMessage(msg);

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		mServiceHandler.removeCallbacksAndMessages(null);
		mServiceLooper.quit();

		Log.i(TAG, "service done"); 
	}

	private void updateWidgets(AppWidgetManager appWidgetManager, int[] allWidgetIds, String temperature) {
		for (int widgetId : allWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.main);
			remoteViews.setTextViewText(R.id.tvOutput, temperature);

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
} 
