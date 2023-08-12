package com.padyak.utility;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class Helper {
    static Helper helper;

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
}
