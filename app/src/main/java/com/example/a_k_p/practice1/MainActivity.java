package com.example.a_k_p.practice1;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

//import static javax.xml.transform.OutputKeys.MEDIA_TYPE;

public class MainActivity extends AppCompatActivity {
    ProgressDialog pd;

    EditText username,password;
    TextView signup,forgot;
    Button signin;


    //private static final String ID="";

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username=(EditText) findViewById(R.id.txtUsername);
        password=(EditText)findViewById(R.id.txtPassword);
        signin=(Button)findViewById(R.id.btnSignin);
        signup=(TextView) findViewById(R.id.txtsignup);
        forgot=(TextView)findViewById(R.id.forgot);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                pd=new ProgressDialog(MainActivity.this);
                pd.setMessage("Signing In");
                pd.show();
                Log.d(TAG, "onClick: clicked");
                String id=username.getText().toString();
                String pass=password.getText().toString();
                OkHttpClient okHttpClient=new OkHttpClient();
                JSONObject dat=new JSONObject();
                try {
                    dat.put("userName",id);
                    dat.put("password",pass);
                    dat.put("firebaseToken","0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), dat.toString());

                final Request request=new Request.Builder()
                        .url("http://40.124.7.240/isthreetest/index.php/services/login")
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {

                    ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);

                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.cancel();
                                pd.dismiss();
                               // pd.hide();
                                Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_LONG).show();
                            }
                        });


                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.d(TAG, "onResponse: starts");
                        String message=response.body().string();
                        if(response.isSuccessful())
                        {
                            try {
                                JSONArray array=new JSONArray(message);
                                JSONObject jo=array.getJSONObject(0);
                                final int status= jo.getInt("status");
                                final String id=jo.getString("UserName");
                                final String name=jo.getString("Name");





                            runOnUiThread(new Runnable() {
                                @Override
                                 public void run() {
                                    Toast.makeText(MainActivity.this,""+id+"and status="+status,Toast.LENGTH_LONG).show();
                                    if(status==1)
                                    {
                                        pd.cancel();
                                        pd.dismiss();
                                        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("username",id);
                                        editor.putString("Name",name);
                                        editor.commit();
                                        Intent i=new Intent(MainActivity.this,MainDashboard.class);
                                        startActivity(i);
                                    }
                                    else if(status==0)
                                    {
                                        pd.cancel();
                                        pd.dismiss();
                                        Toast.makeText(MainActivity.this,"Error Login ID / Password",Toast.LENGTH_LONG).show();
                                    }
                                    else{}
                                   // Toast.makeText(MainActivity.this,""+status,Toast.LENGTH_LONG).show();

    }
});                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,UserSignUp.class);
                startActivity(i);

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }
}
