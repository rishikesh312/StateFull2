package com.example.statefull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void login(View view) {
        EditText mail = findViewById(R.id.usernamesign);
        EditText pass = findViewById(R.id.password_sign);

        String mailString = mail.getText().toString();
        String passString = pass.getText().toString();

        if (DatabaseManager.databaseManager.isUser(mailString, passString)) {
            Intent intent = new Intent(this, MindActivity.class);
            startActivity(intent);
            DatabaseManager.databaseManager.loginSuccess();
        }

    }
    public void register(View view){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String returnString = data.getStringExtra("result");
                Snackbar.make(findViewById(R.id.constraintlayout), returnString, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
