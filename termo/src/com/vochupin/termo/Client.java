package com.vochupin.termo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Client {

    private String server;

    public Client(String server) {
        this.server = server;
    }

    private String getBase() {
        return server;
    }

    public String getBaseURI(String str) {
        String result = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(getBase() + str);
            getRequest.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(getRequest);
            result = getResult(response).toString();
            httpClient.getConnectionManager().shutdown();
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