package com.example.a_k_p.practice1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainDashboard extends AppCompatActivity {

    ImageView ironing,drclean,washpress;
    TextView welcome;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        welcome=(TextView)findViewById(R.id.name);
        ironing=(ImageView)findViewById(R.id.schedule);
        washpress=(ImageView)findViewById(R.id.imageView2);
        drclean=(ImageView)findViewById(R.id.imageView4);
        SharedPreferences preferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        name=preferences.getString("Name",null);
        welcome.setText("Welcome "+name);

        ironing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainDashboard.this,DashBoard.class);
                startActivity(i);
            }
        });

        drclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainDashboard.this);
                LayoutInflater inflater=MainDashboard.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.coming_soon,null));
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        washpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainDashboard.this);
                LayoutInflater inflater=MainDashboard.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.coming_soon,null));
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }
}
