package com.example.se328projectalhobayb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    RequestQueue rq;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rq= Volley.newRequestQueue(this);

        Button bttn_fire=findViewById(R.id.bttn_fire);
        Button bttn_weather=findViewById(R.id.bttn_weather);
        Button bttn_sqlite=findViewById(R.id.bttn_sqlite);

//      Button bttn_sqlite_select=findViewById(R.id.bttn_sqlite);

        sp= PreferenceManager.getDefaultSharedPreferences(this);

        bttn_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FireBaseActivity.class));
            }
        });

        bttn_sqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SQLITEActivity.class) );
            }
        });

        bttn_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WeatherActivity.class) );
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        rq.add(Helper.weather(this));
    }




}