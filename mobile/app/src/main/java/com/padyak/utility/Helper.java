package com.padyak.utility;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class Helper {
    static Helper helper;
    final public String log_code = "Log_Padyak";
    public Helper() {
    }

    public static Helper getInstance(){
        if(helper == null) helper = new Helper();
        return helper;
    }

    public String dateFormat(Date date){
        int monthNum = date.getMonth() + 1;
        int dayNum = date.getDate();
        int yearNum = 1900 +  date.getYear();
        return String.format("%s %02d, %d", Month.of(monthNum).toString(),dayNum,yearNum);
    }
    public String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public double calculateDistance(LatLng startPos, LatLng endPos){
        double d2r = Math.PI / 180;

        try{
            double dlong = (endPos.longitude - startPos.longitude) * d2r;
            double dlat = (endPos.latitude - startPos.latitude) * d2r;
            double a = Math.pow(Math.sin(dlat / 2.0), 2)
                            + Math.cos(startPos.latitude * d2r)
                            * Math.cos(endPos.latitude * d2r)
                            * Math.pow(Math.sin(dlong / 2.0), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return 6367 * c;

        } catch(Exception e){
            e.printStackTrace();
            return 0d;
        }
    }
    public String generateAddress(String json){
        try {
            String foundAddress = "";
            JSONObject reader = new JSONObject(json);
            JSONArray mapArray = reader.optJSONArray("results");
            for(int i=0; i < mapArray.length(); i++){
                JSONObject addressObject = mapArray.getJSONObject(i);
                foundAddress = addressObject.getString("formatted_address");
                break;
            }
            return foundAddress;
        } catch (Exception err){
            return "";
        }
    }
    public String formatDuration(int seconds){
        int hours = (int) (seconds / 3600);
        int minutes = (int) ((seconds % 3600) / 60);
        int remainingSeconds = (int) (seconds % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}
