package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.utility.CustomTimePicker;

import java.util.Calendar;

public class frmAddEvent extends AppCompatActivity {

    Button btnEventRegister, btnEventCancel;
    EditText txAddEventDate,txAddEventStart,txAddEventEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_add_event);
        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventCancel = findViewById(R.id.btnEventCancel);

        txAddEventDate = findViewById(R.id.txAddEventDate);
        txAddEventStart = findViewById(R.id.txAddEventStart);
        txAddEventEnd = findViewById(R.id.txAddEventEnd);

        btnEventCancel.setOnClickListener(v-> finish());
        txAddEventDate.setInputType(InputType.TYPE_NULL);
        txAddEventStart.setInputType(InputType.TYPE_NULL);
        txAddEventEnd.setInputType(InputType.TYPE_NULL);

        txAddEventDate.setOnClickListener(v->{
            final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    txAddEventDate.setText(year + "-" + String.format("%02d",monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth));
                }
            };
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    frmAddEvent.this, datePickerListener,
                    mYear, mMonth, mDay);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            c.add(Calendar.MONTH, +1);
            long oneMonthAhead = c.getTimeInMillis();
            datePicker.setMaxDate(oneMonthAhead);
            datePicker.setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });
        txAddEventStart.setOnClickListener(v->{
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            int mMonth = mcurrentTime.get(Calendar.MONTH);
            int mDay = mcurrentTime.get(Calendar.DAY_OF_MONTH);
            int mYear = mcurrentTime.get(Calendar.YEAR);
            String catDate = String.valueOf(mYear) + "-" + String.format("%02d", mMonth+1) + "-" + String.format("%02d", mDay);
            CustomTimePicker mTimePicker;
            mTimePicker = new CustomTimePicker(frmAddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    if(catDate.equals(txAddEventStart.getText().toString().trim())){
                        if(selectedHour < hour){
                            Toast.makeText(frmAddEvent.this, "Please select a valid time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(selectedHour == hour && selectedMinute <= minute){
                            Toast.makeText(frmAddEvent.this, "Please select a valid time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    String timeSuffix = "";
                    timeSuffix = (selectedHour >= 12) ? "PM" : "AM";
                    selectedHour = (selectedHour > 12) ? selectedHour - 12 : selectedHour;
                    txAddEventStart.setText( String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + timeSuffix);

                }
            }, hour, minute, false);
            mTimePicker.show();
        });
        txAddEventEnd.setOnClickListener(v->{
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            int mMonth = mcurrentTime.get(Calendar.MONTH);
            int mDay = mcurrentTime.get(Calendar.DAY_OF_MONTH);
            int mYear = mcurrentTime.get(Calendar.YEAR);
            String catDate = String.valueOf(mYear) + "-" + String.format("%02d", mMonth+1) + "-" + String.format("%02d", mDay);
            CustomTimePicker mTimePicker;
            mTimePicker = new CustomTimePicker(frmAddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    if(catDate.equals(txAddEventEnd.getText().toString().trim())){
                        if(selectedHour < hour){
                            Toast.makeText(frmAddEvent.this, "Please select a valid time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(selectedHour == hour && selectedMinute <= minute){
                            Toast.makeText(frmAddEvent.this, "Please select a valid time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    String timeSuffix = "";
                    timeSuffix = (selectedHour >= 12) ? "PM" : "AM";
                    selectedHour = (selectedHour > 12) ? selectedHour - 12 : selectedHour;
                    txAddEventEnd.setText( String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + timeSuffix);

                }
            }, hour, minute, false);
            mTimePicker.show();
        });
    }
}