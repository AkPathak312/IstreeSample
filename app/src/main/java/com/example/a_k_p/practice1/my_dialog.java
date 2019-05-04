package com.example.a_k_p.practice1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class my_dialog extends AppCompatActivity {
    Button proceed,cancel;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);
        proceed=(Button)findViewById(R.id.proceed);
        cancel=(Button)findViewById(R.id.cancel);
        tv1=(TextView)findViewById(R.id.textviewww);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(my_dialog.this,"hi",Toast.LENGTH_LONG).show();
            }
        });


    }
}
