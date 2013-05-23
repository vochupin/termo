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
	private Spinner spnColorName;

	private ColorPickerView cpView; 
	private Preferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		prefs = new Preferences(this);

		Intent intent = getIntent();
		setResult(RESULT_CANCELED, intent);

		setContentView(R.layout.activity_config);

		spnColorName = (Spinner) findViewById(R.id.spnColorName);
		fillColorNameSpinner();
		
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
		cpView.setColor(prefs.getColorByIndex(0));
		
		ViewGroup layout = (ViewGroup) findViewById(R.id.vllConfigLayout);
		layout.addView(cpView);
	}

	private void fillColorNameSpinner(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		for(Preferences.PartDescriptor pd : Preferences.partDescriptors){
			adapter.add(pd.humanReadableName);
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spnColorName.setAdapter(adapter);
		spnColorName.setPrompt("Цвета:");
		spnColorName.setSelection(0);
		spnColorName.setOnItemSelectedListener(spinnerClickListener);    
	}
	
	private int oldPosition = 0;
	
	private OnItemSelectedListener spinnerClickListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if(Const.DEBUG) Toast.makeText(getBaseContext(), "index = " + position, Toast.LENGTH_SHORT).show();
			
			prefs.setColorByIndex(oldPosition, cpView.getColor());
			oldPosition = position;
			
			cpView.setColor(prefs.getColorByIndex(position));
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	private OnClickListener btnOkClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			
			prefs.setColorByIndex(oldPosition, cpView.getColor());
			prefs.commit();

			// Build the intent to call the service
			Intent intent = new Intent(ConfigActivity.this.getApplicationContext(), UpdateService.class);

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
