package com.example.statefull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new DatabaseManager(this);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Log.d("isitLogged", isitLogged() ? "true" : "false");
                    if (isitLogged()) {
                        Intent intent = new Intent(SplashActivity.this, MindActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                }
            }
        };
        thread.start();
    }

    public boolean isitLogged() {
        return DatabaseManager.databaseManager.logged();
    }
}