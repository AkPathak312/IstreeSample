package com.example.a_k_p.practice1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SchedulePickup extends AppCompatActivity {

    TextView doorno,landmark,pincode,phone,datesisplay;
    Button changeaddress,confirm;
    ProgressDialog pd;
    String add,land,pins,ph,location;
    String userid;
    CheckBox expdel;
    int exprsval=0;

  //  Button proceed,cancel;

  //  TextView minorder1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_pickup);
        expdel=(CheckBox)findViewById(R.id.checkBox);
        doorno=(TextView)findViewById(R.id.address);
        landmark=(TextView)findViewById(R.id.landmark);
        pincode=(TextView)findViewById(R.id.pin);
        phone=(TextView)findViewById(R.id.phone);
        datesisplay=(TextView)findViewById(R.id.datedisplay);
        confirm=(Button)findViewById(R.id.confirm);
        changeaddress=(Button)findViewById(R.id.changeadress);


        if(expdel.isSelected())
        {
            exprsval=1;
        }
        else{
            exprsval=0;
        }
        pd=new ProgressDialog(SchedulePickup.this);
        pd.setMessage("Fetching your details...");
        pd.show();


        //On backing from change address . the below line is crashing it.
       // datesisplay.setText(getIntent().getExtras().getString("Date"));

        final OkHttpClient okHttpClient=new OkHttpClient();
        JSONObject data=new JSONObject();
        try {
            data.put("customerId","c0016");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), data.toString());
        Request request=new Request.Builder()
                .url(getString(R.string.BaseURL)+"getUserInfo")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String res=response.body().string();
                try {
                    JSONArray jsonArray=new JSONArray(res);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    add=jsonObject.getString("Address");
                    land=jsonObject.getString("landMark");
                    pins=jsonObject.getString("pincode");
                    ph=jsonObject.getString("phoneNo");
                    location=jsonObject.getString("pickupZone");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.cancel();
                        pd.dismiss();
                        landmark.setText(land);
                        pincode.setText(pins);
                        doorno.setText(add);
                        phone.setText(ph);
                    }
                });

                changeaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(SchedulePickup.this, ChangeAddress.class);
                        i.putExtra("address",add);
                        i.putExtra("land",land);
                        i.putExtra("pins",pins);
                        i.putExtra("ph",ph);
                        i.putExtra("location",location);
                        startActivity(i);

                    }
                });

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
                userid=preferences.getString("username",null);

                OkHttpClient okHttpClient1=new OkHttpClient();
                final JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("serviceName","ironing");
                    jsonObject.put("customerId",userid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Request request=new Request.Builder()
                        .url("http://40.124.7.240/isthreetest/index.php/services/getMinimumOrderValue")
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                        final String string=response.body().string();
                        try {
                            JSONObject jo=new JSONObject(string);
                            final Double minorder=jo.getDouble("minimumOrderValue");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    AlertDialog.Builder builder=new AlertDialog.Builder(SchedulePickup.this);
                                 //   LayoutInflater inflater=SchedulePickup.this.getLayoutInflater();
                                 //   builder.setView(inflater.inflate(R.layout.my_dialog,null));
                                    builder.setTitle("Minimum Order")
                                            .setMessage("Your Minimum order value is Rs. "+minorder+". /n Make sure your order is above the minimum value.");

                                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                SchedulePickup();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent i=new Intent(SchedulePickup.this,DashBoard.class);
                                            startActivity(i);

                                        }
                                    });
                                    AlertDialog dialog=builder.create();
                                    dialog.show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });

    }

    public  void SchedulePickup() throws JSONException {

        SharedPreferences preferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        userid=preferences.getString("username",null);

        String timeStamp  = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        //SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("JobID",timeStamp);
        String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        OkHttpClient okHttpClient=new OkHttpClient();
        JSONObject postdat=new JSONObject();
        try
        {
            postdat.put("customerId", userid);
            postdat.put("expressDelivery",exprsval);
            postdat.put("status", "PICKUP-REQUESTED");
            postdat.put("jobid", timeStamp);
            postdat.put("pickupScheduledAt",getIntent().getExtras().getString("Date"));
            postdat.put("createdAt", timeStamp2);
            postdat.put("serviceName", "ironing");
        }
            catch(JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), postdat.toString());
        Request request=new Request.Builder()
                .url("http://40.124.7.240/isthreetest/index.php/services/schedulePickup")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SchedulePickup.this,"Pick Up scheduling failed , try again...",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String str=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder=new AlertDialog.Builder(SchedulePickup.this);
                        //   LayoutInflater inflater=SchedulePickup.this.getLayoutInflater();
                        //   builder.setView(inflater.inflate(R.layout.my_dialog,null));
                        builder.setTitle("Pickup Scheduled")
                                .setMessage(str);

                        builder.setPositiveButton("Fill Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                            }
                        });
                        builder.setNegativeButton("Fill Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog=builder.create();
                        dialog.show();
                    }
                });

            }
        });


    }


}
