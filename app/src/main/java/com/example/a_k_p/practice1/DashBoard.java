package com.example.a_k_p.practice1;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DashBoard extends AppCompatActivity {
    ImageView schedule,myorders,enterorders;
    DatePickerDialog datePickerDialog;
    String jobstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        myorders=(ImageView)findViewById(R.id.imageView4);
        schedule=(ImageView)findViewById(R.id.schedule);
        enterorders=(ImageView)findViewById(R.id.imageView2);


        enterorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : Check for schedule
                Intent i=new Intent(DashBoard.this,EnterOrders.class);
                startActivity(i);
            }
        });

//        myorders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(DashBoard.this,OrdersBucket.class);
//                startActivity(i);
//            }
//        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getJobStatus();
                if(jobstatus==null)
                {
                    Intent i=new Intent(DashBoard.this,SchedulePickupButton.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(DashBoard.this,"Already one Order has been Placed",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void getJobStatus() {

        OkHttpClient okHttpClient=new OkHttpClient();
        JSONObject data=new JSONObject();
        try {
            data.put("customerId","c0016");
          //  data.put("password","12");
            //data.put("firebaseToken","0");
           // {"userName":"c0016","password":"12","firebaseToken=":"00"}

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), data.toString());
        Request request=new Request.Builder()
                .url("http://40.124.7.240/isthreetest/index.php/services/getJobStatus")
                .post(requestBody)
                .build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DashBoard.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Response response) throws IOException {

                String str=response.body().string();
                try {
                    JSONArray jsonarray=new JSONArray(str);
                    JSONObject jsonobject=jsonarray.getJSONObject(0);
                    jobstatus=jsonobject.getString("jobid");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(DashBoard.this,""+jobstatus,Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        return;
    }


}
