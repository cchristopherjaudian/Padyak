package com.padyak.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class VolleyJson {
    String endpoint = "";
    AsyncTask<String, Void, Integer> syncTask;
    AsyncTask<String, Void, String> syncBodyTask;
    AsyncTask<String, Void, JSONObject> syncJsonTask;
    String url, type;
    JSONObject params;
    Context c;
    URL connURL;
    HttpURLConnection conn;

    public VolleyJson(String url, JSONObject params, String type, Context c) {
        this.url = url;
        this.params = params;
        this.type = type.toUpperCase();
        this.c = c;

        if (getType().contains("USER")) {
            endpoint = Constants.userURL.concat(url);
        } else if (getType().contains("ADMIN")) {
            endpoint = Constants.adminURL.concat(url);
        } else if (getType().contains("EVENT")) {
            endpoint = Constants.eventURL.concat(url);
        } else if (getType().contains("POST")) {
            endpoint = Constants.postURL.concat(url);
        } else if (getType().contains("MAP")) {
            endpoint = url;
        } else if (getType().contains("LOCATION")) {
            endpoint = Constants.locationURL.concat(url);
        } else if (getType().contains("ALERT")) {
            endpoint = Constants.alertURL.concat(url);
        } else if (getType().contains("CONTACTS")) {
            endpoint = Constants.contactURL.concat(url);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public JSONObject getJsonResponse(boolean auth) {
        syncJsonTask = new AsyncTask<String, Void, JSONObject>() {
            String data = "";
            String content = "";
            @Override
            protected JSONObject doInBackground(String... strings) {
                try {
                    Log.d(Helper.getInstance().log_code, "doInBackground: " + params);
                    connURL = new URL(endpoint);

                    conn = (HttpURLConnection) connURL.openConnection();
                    if (auth)
                        conn.setRequestProperty("Authorization", LoggedUser.getInstance().getRefreshToken());

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);


                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);


                    if (params != null) {
                        try (OutputStream os = conn.getOutputStream()) {
                            byte[] input = params.toString().getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }

                    }
                    Log.d(Helper.getInstance().log_code, "JSON URL: " + conn.getURL());
                    Log.d(Helper.getInstance().log_code, "Request Type: " + conn.getRequestMethod());
                    int responseCode = conn.getResponseCode();
                    //if (responseCode != 200) throw new Exception("Response Code: " + responseCode);
                    Log.d(Helper.getInstance().log_code, "Response Code Volley: " + responseCode);

                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        content = response.toString();
                    }
                    Log.d(Helper.getInstance().log_code, "AlertResponse: " + content);
                    if (!getType().equals("MAP")) {
                        Log.d(Helper.getInstance().log_code, "Request Type Volley: " + conn.getRequestMethod());
                        Log.d(Helper.getInstance().log_code, "content volley: " + content);
                    }
                    return new JSONObject(content);
                } catch (Exception e) {
                    Log.d(Helper.getInstance().log_code, "exception: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(JSONObject s) {
                super.onPostExecute(s);
            }
        }.execute(endpoint);

        try {
            return syncJsonTask.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
