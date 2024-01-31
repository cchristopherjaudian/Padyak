package com.padyak.utility;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static String baseURL =  "https://v2-xzkjwxqioa-de.a.run.app/";
    public static String userURL = baseURL.concat("users");
    public static String adminURL = baseURL.concat("admins");
    public static String postURL = baseURL.concat("posts");
    public static String eventURL = baseURL.concat("events");
    public static String locationURL = baseURL.concat("locations");
    public static String alertURL = baseURL.concat("alerts");
    public static String contactURL = baseURL.concat("contacts");
    public static final Map<Integer,String> alertMap = new HashMap<Integer, String>()
    {{
        put(1,"I am safe i just can't maintain the pace.");
        put(2,"My bike have a problem");
        put(3,"I had a bike breakdown and  I don't have any tools. Please help");
        put(4,"I had an accident and I need help.");
    }};

}
