package com.vochupin.termo;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends Activity {

	protected static final String TAG = ConfigActivity.class.getSimpleName();
	private int appWidgetId;
	private Button btnOk;
	private Spinner spnPartName;

	private ColorPickerView cpView; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		setResult(RESULT_CANCELED, intent);

		setContentView(R.layout.activity_config);

		spnPartName = (Spinner) findViewById(R.id.spnPartName);
		fillPartNameSpinner();

		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(btnOkClickListener);

		Bundle extras = intent.getExtras();
		if (extras != null) {
			appWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID, 
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		cpView = new ColorPickerView(this);
		cpView.setAlphaSliderVisible(true);
		cpView.setAlphaSliderText("Прозрачность");
		ViewGroup layout = (ViewGroup) findViewById(R.id.vllConfigLayout);
		layout.addView(cpView);
	}

	private void fillPartNameSpinner(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.add("123");
		adapter.add("124");
		adapter.add("125");
		adapter.add("126");
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spnPartName.setAdapter(adapter);
		spnPartName.setPrompt("Заголовок");
		spnPartName.setSelection(0);
		spnPartName.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});    
	}

	private OnClickListener btnOkClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {

			SharedPreferences shPref = PreferenceManager.getDefaultSharedPreferences(ConfigActivity.this);//getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = shPref.edit();
			int color = cpView.getColor();
			editor.putInt("FORE_COLOR", color);
			editor.commit();
			Log.e(TAG, "written: " + color + " " + Color.alpha(color));

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
