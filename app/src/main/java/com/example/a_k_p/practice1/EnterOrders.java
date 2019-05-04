package com.example.a_k_p.practice1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a_k_p.practice1.Model.AllTarrif;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EnterOrders extends AppCompatActivity {

    Spinner spinner2;
    Button add,confirm,cancel;
    EditText quantity;
    ListView listView;
    Double totalprice=0.0;
    CheckBox hanger;

    boolean isHanger=false;
    ArrayList<String> itemsselected;
    ArrayAdapter<String> itemadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_orders);
        spinner2=(Spinner)findViewById(R.id.spinner2);
        add=(Button)findViewById(R.id.btnAdd);
        quantity=(EditText)findViewById(R.id.quantity);
        listView=(ListView)findViewById(R.id.listview);
        hanger=(CheckBox)findViewById( R.id.chkboxhanger);
        confirm=(Button)findViewById(R.id.pay);
        cancel=(Button)findViewById(R.id.cancel);
      //  int qty=Integer.parseInt(quantity.getText().toString());

        final List<AllTarrif> tarrifList=new ArrayList<AllTarrif>();
            final OkHttpClient okHttpClient=new OkHttpClient();
            final JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("serviceName","ironing");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            final Request request=new Request.Builder()
                    .url("http://40.124.7.240/isthreetest/index.php/services/allTariff")
                    .post(requestBody)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {


                    if(response.isSuccessful()) {
                        String str = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(str);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                AllTarrif allTarrif = new AllTarrif(jsonObject1.getString("category"), jsonObject1.getDouble("price"), jsonObject1.getDouble("hangerPrice"));
                                tarrifList.add(allTarrif);
                                Log.d("TAG", allTarrif.toString());
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ArrayAdapter<AllTarrif> allTarrifArrayAdapter=new ArrayAdapter<AllTarrif>(EnterOrders.this,R.layout.support_simple_spinner_dropdown_item,tarrifList);
                                    allTarrifArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                    spinner2.setAdapter(allTarrifArrayAdapter);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

      //  allTarrifArrayAdapter.notifyDataSetChanged();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EnterOrders.this,"onCLick",Toast.LENGTH_LONG).show();
                SharedPreferences preferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
                String userid=preferences.getString("username",null);
                String jobid=preferences.getString("JobID",null);
                OkHttpClient okHttpClient1=new OkHttpClient();

                JSONObject data=new JSONObject();
                try {
                    data.put("customerId",userid);
                    data.put("jobId",jobid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), data.toString());
                Request request=new Request.Builder()
                        .url("http://40.124.7.240/isthreetest/index.php/services/cancelJobOrder")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        String str=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EnterOrders.this,"Job Order Cancelled Successfully",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    confirmOrder();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        itemsselected=new ArrayList<String>();
        itemadapter= new ArrayAdapter<String>(EnterOrders.this,android.R.layout.simple_list_item_1,itemsselected);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AllTarrif allTarrif= (AllTarrif)spinner2.getSelectedItem();
                int number=Integer.parseInt(quantity.getText().toString());
                String cat=allTarrif.getCategory();
                Double pri=allTarrif.getPrice();
                Double hangpri=allTarrif.getHangerprice();
                Double singleprice;
              // (hanger.isSelected()==true)? (totalprice=totalprice+number*hangpri) : (totalprice=totalprice+number*pri);

                if(hanger.isChecked()){
                    totalprice=totalprice+number*hangpri;
                    singleprice=number*hangpri;
                    itemsselected.add(cat+"    \t"+number+"    \t"+hangpri+"    \t"+singleprice);
                }else{
                    totalprice=totalprice+number*pri;
                    singleprice=number*pri;
                    itemsselected.add(cat+"    \t"+number+"    \t"+pri+"    \t"+singleprice);
                }


              //  Toast.makeText(EnterOrders.this,""+cat,Toast.LENGTH_LONG).show();

                itemadapter.notifyDataSetChanged();
                listView.setAdapter(itemadapter);
                Toast.makeText(EnterOrders.this,totalprice.toString(),Toast.LENGTH_LONG).show();






            }
        });

//        hanger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(hanger.isChecked())
//            }
//        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void confirmOrder() throws JSONException {
        final OkHttpClient[] okHttpClient1 = {new OkHttpClient()};
        final JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("serviceName","ironing");
            jsonObject.put("customerId","c0016");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Request request=new Request.Builder()
                .url("http://40.124.7.240/isthreetest/index.php/services/getMinimumOrderValue")
                .post(requestBody)
                .build();

        //final Double[] minimum = new Double[1];
        okHttpClient1[0].newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }


            @Override
            public void onResponse(final Response response) throws IOException {
                final String string=response.body().string();
                try {
                    JSONObject jo=new JSONObject(string);
                    final Double minimum =jo.getDouble("minimumOrderValue");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EnterOrders.this,""+minimum,Toast.LENGTH_LONG).show();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
//        Double minimum=getMinimumOrder();
//
//        Toast.makeText(EnterOrders.this,""+minimum,Toast.LENGTH_LONG).show();





    }

    private Double getMinimumOrder() throws JSONException {

//        final OkHttpClient[] okHttpClient1 = {new OkHttpClient()};
//        final JSONObject jsonObject=new JSONObject();
//        try {
//            jsonObject.put("serviceName","ironing");
//            jsonObject.put("customerId","c0016");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
//        Request request=new Request.Builder()
//                .url("http://40.124.7.240/isthreetest/index.php/services/getMinimumOrderValue")
//                .post(requestBody)
//                .build();
//
//        final Double[] minimum = new Double[1];
//        okHttpClient1[0].newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//
//            @Override
//            public void onResponse(final Response response) throws IOException {
//                final String string=response.body().string();
//                try {
//                    JSONObject jo=new JSONObject(string);
//                    final Double minimum =jo.getDouble("minimumOrderValue");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(EnterOrders.this,""+minimum,Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        });
        return 0.0;
    }
}
