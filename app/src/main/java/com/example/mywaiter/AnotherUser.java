package com.example.mywaiter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AnotherUser extends AppCompatActivity {
    private TextView tvName, tvSecName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user);
        init();
        getIntentExtra();
    }
    private void init() {
        tvName = findViewById(R.id.tvName);
        tvSecName = findViewById(R.id.tvSecName);
        tvEmail = findViewById(R.id.tvEmail);
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            tvName.setText(intent.getStringExtra("name"));
            tvSecName.setText(intent.getStringExtra("sec_name"));
            tvEmail.setText(intent.getStringExtra("email"));
        }
    }
}