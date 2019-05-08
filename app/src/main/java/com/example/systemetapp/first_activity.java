package com.example.systemetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.systemetapp.domain.Product;

public class first_activity extends AppCompatActivity {

    CheckBox firstCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_activity);

        firstCheckbox=findViewById(R.id.checkBox_one);
        firstCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_one= new Intent(first_activity.this, MainActivity.class);
                startActivity(intent_one);
            }
        });
    }
}
