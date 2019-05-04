package com.example.a_k_p.practice1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserSignUp extends AppCompatActivity {

    String[] item;
    Spinner spinner;
    EditText fullname, email, phone, altphone, pass, confirmpass, door, landmark;
    Button signup;
   // Double totalprice=0.0;


    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        altphone=(EditText)findViewById(R.id.altphone);
        pass=(EditText)findViewById(R.id.txtPassword);
        confirmpass=(EditText)findViewById(R.id.confirmpassword);
        door=(EditText)findViewById(R.id.doorno);
        landmark=(EditText)findViewById(R.id.landmark);
        fullname=(EditText)findViewById(R.id.fullname);
        spinner=(Spinner)findViewById(R.id.spinner);
        signup=(Button)findViewById(R.id.signup);
        final String timestamp=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);





        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

      //      if(fullname.getText().toString().isEmpty()){};
//                    Toast.makeText(UserSignUp.this,"Name field is Empty",Toast.LENGTH_LONG).show();
//                else if(email.getText().toString()=="")
//                    Toast.makeText(UserSignUp.this,"Email field is Empty",Toast.LENGTH_LONG).show();
//                else if(phone.getText().toString()=="")
//                    Toast.makeText(UserSignUp.this,"Invalid Phone Number",Toast.LENGTH_LONG).show();
//                else if(pass.getText().toString()=="")
//                    Toast.makeText(UserSignUp.this,"Email field is Empty",Toast.LENGTH_LONG).show();
//                else if(!(pass.getText().toString()==confirmpass.getText().toString()))
//                    Toast.makeText(UserSignUp.this,"Passwords do not match",Toast.LENGTH_LONG).show();
//                else if(door.getText().toString()=="")
//                    Toast.makeText(UserSignUp.this,"Address field is Empty",Toast.LENGTH_LONG).show();




                OkHttpClient okHttpClient=new OkHttpClient();
                JSONObject data=new JSONObject();
                try {
                    data.put("address",door.getText().toString());
                    data.put("altphone",altphone.getText().toString());
                    data.put("city","Hyderabad");
                    data.put("country","India");
                    data.put("email",email.getText().toString());
                    data.put("landMark",landmark.getText().toString());
                    data.put("lat","");
                    data.put("longi","");
                    data.put("name",fullname.getText().toString());
                    data.put("password","aniket@123A");
                    data.put("phoneNo",phone.getText().toString());
                    data.put("partnerId","P0005");
                    data.put("pickupZone","Madhapur");
                    data.put("createdDate",timestamp);
                    data.put("pic",imageString);
                    data.put("picFileType","jpg");
                    data.put("pincode","00000");
                    data.put("state","Telangana");
                    data.put("firebaseToken","00");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), data.toString());
                Request request=new Request.Builder()
                        .url("http://40.124.7.240/isthreetest/index.php/services/signup")
                        .post(requestBody)
                        .build();


                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserSignUp.this,"Failed",Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserSignUp.this,"Done",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
            }
        });


        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url("http://40.124.7.240/isthree/index.php/services/serviceLocations")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }


            @Override
            public void onResponse(Response response) throws IOException {
                String res=response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(res);
                    item=new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        Log.d(TAG, ""+i);
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        item[i] = jsonObject.getString("location");
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(UserSignUp.this,R.layout.support_simple_spinner_dropdown_item,item);
                            spinner.setAdapter(adapter);
                        }
                    });

                }


            }
        });

    }
}
