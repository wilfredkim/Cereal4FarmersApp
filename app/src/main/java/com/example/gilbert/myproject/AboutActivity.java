package com.example.gilbert.myproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView Help,Help1,Help2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Help =(TextView)findViewById(R.id.help);
        Help1 =(TextView)findViewById(R.id.help1);
        Help2 =(TextView)findViewById(R.id.help2);


    }

}
