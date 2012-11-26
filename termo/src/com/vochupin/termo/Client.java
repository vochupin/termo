package com.vochupin.termo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Client {

    private static final String TAG = Client.class.getSimpleName();
	public static final String NO_CONNECTION = "No connection";
	private String server;

    public Client(String server) {
        this.server = server;
    }

    private String getServer() {
        return server;
    }

    public String getTemperatureJson(String str) {
        String result = NO_CONNECTION;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(getServer() + str);
            getRequest.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(getRequest);
            result = getResult(response).toString();
            httpClient.getConnectionManager().shutdown();
            Log.i(TAG, result);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private StringBuilder getResult(HttpResponse response) throws IllegalStateException, IOException {

        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())), 1024);
        String output;
        while ((output = br.readLine()) != null) 
            result.append(output);
        return result;
    }
}