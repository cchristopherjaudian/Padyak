package com.padyak.utility;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.padyak.R;
import com.padyak.activity.frmAlertGroup;
import com.padyak.activity.frmAlertInfo;
import com.padyak.activity.frmAlertSend;
import com.padyak.dto.AlertLevel;
import com.padyak.dto.EmergencyContact;
import com.padyak.fragment.AlertSendFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.*;
public class Helper {
    static Helper helper;
    final public String log_code = "Log_Padyak";
    static List<EmergencyContact> tempEmergencySet;
    public Helper() {
    }


    public static Helper getInstance(){
        if(helper == null) helper = new Helper();
        if(tempEmergencySet == null) tempEmergencySet = new ArrayList<>();
        return helper;
    }
    public String formatDate(String dateString){
        String formatted = "";
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            date = parser.parse(dateString);
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd yyyy");
            formatted = formatter.format(date);
        } catch (ParseException e) {
            Log.d(Helper.getInstance().log_code, "formatDate: " + e.getMessage());
        }
        return formatted;
    }
    public String formatTime(String timeString){
        String formatted = "";
        try {
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss.SSS");
            Date date = parser.parse(timeString.substring(0, 12));
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            formatted = formatter.format(date);
        } catch (ParseException e) {
            Log.d(Helper.getInstance().log_code, "formatTime: " + e.getMessage());
        }
        return formatted;
    }
    public CompletableFuture<Map<String, Object>> getCurrentLocation(Activity activity) {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        Map<String, Object> returnMap = new HashMap<>();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            returnMap.put("error","permission not granted");
            return CompletableFuture.completedFuture(returnMap);
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double _lat = location.getLatitude();
                        double _long = location.getLongitude();
                        String fromLocationURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + _lat + "," + _long + "&key=" + activity.getString(R.string.maps_publicapi);
                        VolleyHttp fromVolley = new VolleyHttp(fromLocationURL, null, "MAP", activity);
                        String alertAddress = Helper.getInstance().generateAddress(fromVolley.getResponseBody(false));
                        returnMap.put("name",alertAddress);
                        returnMap.put("latitude",_lat);
                        returnMap.put("longitude",_long);
                        future.complete(returnMap);
                    } else {
                        returnMap.put("error","location is null");
                        future.complete(returnMap);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    public String generatePinCode(){
        Random random = new Random();
        int number = random.nextInt(9000) + 1000;
        return String.valueOf(number);
    }
    public List<EmergencyContact> getTempEmergencySet(){
        return tempEmergencySet;
    }
    public void addTempEmergencyContact(EmergencyContact emergencyContact){
        tempEmergencySet.add(emergencyContact);
    }
    public void removeTempEmergencyContact(String phoneNumber){
        tempEmergencySet.removeIf(contact -> contact.getContact().equals(phoneNumber));
    }
    public boolean checkTempContact(String phoneNumber){
        return tempEmergencySet.stream().anyMatch(contact -> contact.getContact().equals(phoneNumber));
    }
    public void resetEmergencyContacts(){
        tempEmergencySet = new ArrayList<>();
    }
    public boolean checkString(String input) {
        String pattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);
        //Log.d(Helper.getInstance().log_code, "checkString: " + input + " " + m.find());
        return m.find();
    }
    public boolean validateMobileNumber(String input) {
        String pattern = "^[0-9]{11}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);
        return m.find();
    }
    public ProgressDialog progressDialog(Context c, String body){
        ProgressDialog progressDialog = new ProgressDialog(c);
        progressDialog.setTitle(c.getString(R.string.app_name));
        progressDialog.setMessage(body.concat(" Please wait..."));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
    public String dateFormat(Date date){
        int monthNum = date.getMonth() + 1;
        int dayNum = date.getDate();
        int yearNum = 1900 +  date.getYear();
        return String.format("%s %02d, %d", Month.of(monthNum).toString(),dayNum,yearNum);
    }
    public String ISOtoTime(String s){
        if(!s.contains("T")) return s;
        return s.substring(s.indexOf("T")+1);
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
    public Calendar toCalendar(String d){
        LocalDate localDate = LocalDate.parse(d);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(instant));
        return calendar;
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

    public String getAlertDescription(int level){
        List<AlertLevel> alertLevelList = new ArrayList<>();
        alertLevelList.add(new AlertLevel(1, "I am safe. I just can't maintain the pace"));
        alertLevelList.add(new AlertLevel(2, "My bike has a problem"));
        alertLevelList.add(new AlertLevel(3, "I had a bike breakdown and I don't have any tools. Please help."));
        alertLevelList.add(new AlertLevel(4, "I had an accident and I need help."));
        return alertLevelList.get(level).getDescription();
    }
}
