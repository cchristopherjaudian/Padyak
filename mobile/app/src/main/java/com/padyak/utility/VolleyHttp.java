package com.padyak.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.content.AsyncTaskLoader;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class VolleyHttp {
    String endpoint = "";
    AsyncTask<String, Void, Integer> syncTask;
    AsyncTask<String, Void, String> syncBodyTask;
    String url,type;
    Map<String, Object> params;
    Context c;
    URL connURL;
    HttpURLConnection conn;
    public VolleyHttp(String url, Map<String, Object> params, String type, Context c) {
        this.url = url;
        this.params = params;
        this.type = type.toUpperCase();
        this.c = c;

        if (getType().equals("USER")) {
            endpoint = Constants.userURL.concat(url);
        } else if (getType().equals("EVENT")) {
            endpoint = Constants.eventURL.concat(url);
        } else if (getType().equals("POST")) {
            endpoint = Constants.postURL.concat(url);
        } else if(getType().equals("MAP")){
            endpoint = url;
        }
    }
    @SuppressLint("StaticFieldLeak")
    public String getResponseBody(boolean auth){
        syncBodyTask = new AsyncTask<String, Void, String>() {
            String data = "";
            @Override
            protected String doInBackground(String... strings) {
                try {

                    connURL = new URL(endpoint);


                    Log.d("Log_Padyak", "Volley URL: " + connURL);
                    conn = (HttpURLConnection) connURL.openConnection();
                    if(auth) conn.setRequestProperty("Authorization",LoggedUser.getInstance().getRefreshToken());
                    if(params != null){
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                    } else{
                        conn.setRequestMethod("GET");
                    }


                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);


                    if(params != null){
                        Log.d("Log_Padyak", "Volley Payload: " + params);
                        params.forEach((k,v)->{
                            try {
                                data = data.concat("&").concat(URLEncoder.encode(k, "UTF-8").concat( "=").concat(URLEncoder.encode(String.valueOf(v), "UTF-8")));
                            } catch (UnsupportedEncodingException e) {
                                Log.d("Log_Padyak",e.getMessage());
                            }
                        });
                        data = data.substring(1);

                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        wr.write(data);
                        wr.flush();
                    }

                    int responseCode=conn.getResponseCode();

                    Log.d("Log_Padyak", "Response Code Volley: " + responseCode);
                    Log.d("Log_Padyak", "Request Type Volley: " + conn.getRequestMethod());
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String content = "", line;
                    while ((line = rd.readLine()) != null) {
                        content += line + "\n";
                    }
                    Log.d("Log_Padyak", "content volley: " + content);
                    return content;
                } catch (Exception e) {
                    Log.d("Log_Padyak", "exception: " + e.getMessage());
                    return "";
                }
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("Log_Padyak", s);
            }
        }.execute(endpoint);

        try {
            return syncBodyTask.get();
        } catch (ExecutionException | InterruptedException e) {
            return "";
        }
    }
    @SuppressLint("StaticFieldLeak")
    public int getResponseStatus() {
        syncTask = new AsyncTask<String,Void, Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                try {
                    connURL = new URL(endpoint);
                    Log.d("Log_Padyak", "doInBackground: " + connURL);
                    conn = (HttpURLConnection) connURL.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    return conn.getResponseCode();
                } catch (Exception e) {
                    Log.d("Log_Padyak", "doInBackground: " + e.getMessage());
                    return 500;
                }

            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d("Log_Padyak", String.valueOf(integer));
            }
        }.execute(endpoint);
        try {
            return syncTask.get();
        } catch (ExecutionException | InterruptedException e) {
            return 500;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
