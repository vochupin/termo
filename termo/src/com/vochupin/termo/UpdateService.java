package com.vochupin.termo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.vochupin.termo.db.TermoDataSource;
import com.vochupin.termo.db.TermoSample;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import 	android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateService extends Service {

	private static final String TAG = UpdateService.class.getSimpleName();
	private static final String TERMO_SERVER = "http://termo.tomsk.ru/";
	private static final String TERMO_JSON_INFORMER = "data.json";
	private static final String ALL_WIDGET_IDS = "allWidgetIds";
	private static final long PERIOD_12H = 1000 * 60 * 60 * 12;
	
	private static final float HOR_MARGIN = 10;
	private static final float VERT_MARGIN = 10;

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
					List<TermoSample> tsamples = tds.getLastSamples(1); 
					if(tsamples.size() != 0) ts = tsamples.get(0);
				} catch (ParseException e) {
					Log.e(TAG, "Can't read last sample from base.");
					e.printStackTrace();
					ts = new TermoSample();
				}
			}

			updateWidgets(appWidgetManager, allWidgetIds, null, tds);

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

		Context context = getApplicationContext();
		ComponentName name = new ComponentName(context, TermoWidget.class);
		int [] allWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name);
		
		updateWidgets(appWidgetManager, allWidgetIds, "Запрос...", null);

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

	private void updateWidgets(AppWidgetManager appWidgetManager, int[] allWidgetIds, String message, TermoDataSource tds) {
		Preferences prefs = new Preferences(UpdateService.this);

		for (int widgetId : allWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.main);

			AppWidgetProviderInfo awi = appWidgetManager.getAppWidgetInfo(widgetId);
			if(awi == null){
				Log.i(TAG, "app widget id has not been bound to a provider yet, or application don't have access to that app widget id");
				return;
			}
			
			int h = awi.minHeight;
			int w = awi.minWidth;
			if(Const.DEBUG) Log.i(TAG, "h: " + h + " w:" + w);

			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			
			canvas.drawColor(prefs.getBackgroundColor());

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);

			drawGrid(h, w, canvas, paint, prefs);

			drawExplicitMessage(message, h, w, canvas, paint, prefs);

			if(tds != null){
				try {
					
					List<TermoSample> tsamples = readSamplesFromDB(tds);
					
					if(tsamples.size() != 0){
						
						float ymax = -Float.MAX_VALUE; float ymin = Float.MAX_VALUE;
						for(TermoSample ts : tsamples){
							if(Const.DEBUG) Log.i(TAG, "ts: " + ts);
							if(ymax < ts.getTemperature()) ymax = ts.getTemperature();
							if(ymin > ts.getTemperature()) ymin = ts.getTemperature();
						}
						float yspan = ymax - ymin;
						if(Const.DEBUG) Log.i(TAG, "ymax: " + ymax + " ymin: " + ymin + " yspan: " + yspan);
						
						float xmax = tsamples.get(0).getSampleTime().getTime();
						float xmin = xmax - PERIOD_12H;
						float xspan = xmax - xmin;
						if(Const.DEBUG) Log.i(TAG, "xmax: " + xmax + " xmin: " + xmin + " xspan: " + xspan);
						
						drawGridValues(h, w, canvas, paint, ymax, ymin, prefs);
						
						setColor(paint, prefs.getGraphColor());
						
						int oldx = Integer.MAX_VALUE; int oldy = Integer.MAX_VALUE;
						for(TermoSample ts : tsamples){
							int x = (int) ((w - 2 * HOR_MARGIN) * ((float)ts.getSampleTime().getTime() - xmin) / xspan) + (int)HOR_MARGIN;
							int y = (int) ((h - 2 * VERT_MARGIN) * (1 - (ts.getTemperature() - ymin) / yspan)) + (int)VERT_MARGIN;
							
							if(oldy == Integer.MAX_VALUE) {oldy = y; oldx = x;}
							
							canvas.drawCircle(x, y, 2, paint);
							canvas.drawLine(oldx, oldy, x, y, paint);
							
							if(Const.DEBUG) Log.i(TAG, "x: " + x + " y: " + y);
							oldx = x; oldy = y;
						}
						
						drawTemperatureAndTimestamp(h, w, canvas, paint, tsamples.get(0), prefs);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			remoteViews.setImageViewBitmap(R.id.ivInfo, bitmap);

			enableUpdateOnClick(allWidgetIds, remoteViews);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	private void drawTemperatureAndTimestamp(int h, int w, Canvas canvas, Paint paint, TermoSample ts, Preferences prefs) {
		if(ts.getTemperature() >= 0){
			setColor(paint, prefs.getTempWarmColor());
		}else{
			setColor(paint, prefs.getTempColdColor());
		}

		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(50);
		String tstr = Float.toString(ts.getTemperature()) + "°";
		float tw = paint.measureText(tstr);
		canvas.drawText(tstr, w / 2 - tw / 2, 25 + h / 2, paint);
		
		setColor(paint, prefs.getDateColor());

		paint.setTypeface(Typeface.DEFAULT);
		paint.setTextSize(10);
		canvas.drawText(ts.getSampleTime().toLocaleString(), 10, h, paint);
	}

	private void setColor(Paint paint, int color) {
		paint.setColor(color);
		paint.setAlpha(Color.alpha(color));
	}

	private void drawGridValues(int h, int w, Canvas canvas, Paint paint, float ymax, float ymin, Preferences prefs) {
		setColor(paint, prefs.getMaxminColor());
		
		paint.setTextSize(8);
		String maxTemp = Float.toString(ymax);
		float tw = paint.measureText(maxTemp);
		canvas.drawText(maxTemp, w - HOR_MARGIN - tw, 8, paint);

		String minTemp = Float.toString(ymin);
		tw = paint.measureText(minTemp);
		canvas.drawText(minTemp, w - HOR_MARGIN - tw, h, paint);
	}

	private List<TermoSample> readSamplesFromDB(TermoDataSource tds) throws ParseException {
		List<TermoSample> tsamples = tds.getLastSamplesForPeriod(PERIOD_12H);
		tsamples.add(tds.getNextSample(tsamples.get(tsamples.size() - 1)));
		return tsamples;
	}

	private void drawExplicitMessage(String message, int h, int w, Canvas canvas, Paint paint, Preferences prefs) {
		if(message != null){
			setColor(paint, prefs.getMessageColor());
			
			paint.setTextSize(40);
			float tw = paint.measureText(message);
			canvas.drawText(message, w/2  - tw / 2, h / 2 + 20, paint);
		}
	}

	private void drawGrid(int h, int w, Canvas canvas, Paint paint, Preferences prefs) {
		setColor(paint, prefs.getGridColor());
		for(int i = 0; i < 5; i++){
			float x = HOR_MARGIN + i * (w - 2 * HOR_MARGIN) / 4;
			canvas.drawLine(x, VERT_MARGIN, x, h - VERT_MARGIN, paint);
		}

		for(int i = 0; i < 3; i++){
			float y = VERT_MARGIN + i * (h - 2 * VERT_MARGIN) / 2;
			canvas.drawLine(HOR_MARGIN, y, w - HOR_MARGIN, y, paint);
		}

		setColor(paint, prefs.getLinkColor());
		paint.setTextSize(10);
		float tw = paint.measureText(TERMO_SERVER);
		canvas.drawText(TERMO_SERVER, w / 2 - tw / 2, 10, paint);
	}

	private void enableUpdateOnClick(int[] allWidgetIds,	RemoteViews remoteViews) {
		Intent clickIntent = new Intent(this.getApplicationContext(), TermoWidget.class);

		clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.ivInfo, pendingIntent);
	}
} 
