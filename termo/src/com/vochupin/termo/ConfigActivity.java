package com.vochupin.termo;

import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;

public class ConfigActivity extends Activity {

	protected static final String TAG = ConfigActivity.class.getSimpleName();
	private int appWidgetId;
	private Button btnOk;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);

        setContentView(R.layout.activity_config);
        
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(btnOkClickListener);
        
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }
    
    private OnClickListener btnOkClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ConfigActivity.this.getApplicationContext());	
			int[] allWidgetIds = appWidgetManager.getAppWidgetIds(ConfigActivity.this.getComponentName());
			
			// Build the intent to call the service
			Intent intent = new Intent(ConfigActivity.this.getApplicationContext(), UpdateService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

			// Update the widgets via the service
			ConfigActivity.this.startService(intent);
			
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_config, menu);
        return true;
    }
}