package com.example.a_k_p.practice1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

public class SchedulePickupButton extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_pickup_button);
        picker=(Button)findViewById(R.id.pickup);

        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
                

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String str= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        Toast.makeText(SchedulePickupButton.this,str,Toast.LENGTH_LONG).show();
        Intent i=new Intent(SchedulePickupButton.this,SchedulePickup.class);
        i.putExtra("Date",str);
        startActivity(i);


    }
}
