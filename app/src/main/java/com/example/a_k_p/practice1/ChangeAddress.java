package com.example.a_k_p.practice1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class ChangeAddress extends AppCompatActivity {


    EditText address,landmark,pincode;
    ProgressDialog pd;
    Spinner location;
    Button change;
    String[] item;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_change);
        address=(EditText)findViewById(R.id.editdoor);
        landmark=(EditText)findViewById(R.id.editlandmark);
        pincode=(EditText)findViewById(R.id.editpincode);
        location=(Spinner)findViewById(R.id.spinner);
        change=(Button)findViewById(R.id.change);
        address.setText(getIntent().getExtras().getString("address"));
        landmark.setText(getIntent().getExtras().getString("land"));
        pincode.setText(getIntent().getExtras().getString("pins"));

        SharedPreferences preferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        userid=preferences.getString("username",null);
        //Toast.makeText(ChangeAddress.this,str,Toast.LENGTH_LONG).show();

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
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(ChangeAddress.this,R.layout.support_simple_spinner_dropdown_item,item);
                            location.setAdapter(adapter);
                        }
                    });

                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address.getText().length()==0)
                {
                    Toast.makeText(ChangeAddress.this,"Enter a Valid Address",Toast.LENGTH_LONG).show();
                }
                else if(landmark.getText().length()==0)
                {
                    Toast.makeText(ChangeAddress.this,"Landmark Field is Empty.",Toast.LENGTH_LONG).show();
                }
                else if(location.getSelectedItem()==null)
                {
                    Toast.makeText(ChangeAddress.this,"Nothing selected in Location",Toast.LENGTH_LONG).show();
                }
                else{
                    pd=new ProgressDialog(ChangeAddress.this);
                    pd.setMessage("Changing Address...");
                    pd.show();
                    OkHttpClient okHttpClient=new OkHttpClient();
                    JSONObject postdat=new JSONObject();
                    try {
                        postdat.put("customerID",userid);
                        postdat.put("address", address.getText().toString());
                        postdat.put("city","Hyderabad");
                        postdat.put("country", "INDIA");
                        postdat.put("landMark", landmark.getText().toString());
                        postdat.put("lat","12.14");
                        postdat.put("longi","24.25");
                        postdat.put("pincode", pincode.getText().toString());
                        postdat.put("state", "Telangana");
                        postdat.put("pickupZone",location.getSelectedItem().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), postdat.toString());
                    Request request=new Request.Builder()
                            .url("http://40.124.7.240/isthreetest/index.php/services/changeAddress")
                            .post(requestBody)
                            .build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.cancel();
                                    pd.dismiss();
                                    Toast.makeText(ChangeAddress.this,"Error changing address. Try Again",Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.cancel();
                                    pd.dismiss();
                                    AlertDialog.Builder builder=new AlertDialog.Builder(ChangeAddress.this);
                                    builder.setMessage("Your address has been changed.")
                                            .setTitle("Success");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i=new Intent(ChangeAddress.this,SchedulePickup.class);
                                            startActivity(i);
                                            finish();
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
        });



    }





}
