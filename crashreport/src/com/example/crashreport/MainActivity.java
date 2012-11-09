package com.example.crashreport;

import java.io.IOException;
import java.net.MalformedURLException;

import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText edbLog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        edbLog = (EditText) findViewById(R.id.edbLog);
        
        new Thread(worker).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    Runnable worker = new Runnable(){

		@Override
		public void run() {
			RssParser parser = new RssParser();
			try {
				parser.printRSSContent(parser.parseFeed("http://ru-chp.livejournal.com/data/rss"));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FeedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    };
}
