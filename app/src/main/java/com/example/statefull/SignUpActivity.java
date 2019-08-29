package com.example.statefull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    public static final String ALREADY_EXIST = "Username already exist";
    public static final String VALID_USERNAME = "Enter valid username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        DatabaseManager.databaseManager = new DatabaseManager(this);
        new DatabaseManager(this);

    }

    boolean userCheck(String user) {
        if (DatabaseManager.databaseManager == null) {
            Log.d("Error", "DatabaseManager is Null");
            return false;
        }

        if (user.length() > 0) {
            return !(DatabaseManager.databaseManager.userExist(user));
        } else return false;
    }

    boolean emailCheck(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    boolean keyCheck(String key, String reKey) {
        if (key.equals(reKey) && key.length() >= 5) {
            return !key.contains(" ");
        }
        return false;

    }

    public void register(View view) {
        EditText user = findViewById(R.id.usernamesign);
        EditText mail = findViewById(R.id.emailsign);
        EditText password = findViewById(R.id.password);
        EditText repassword = findViewById(R.id.rePassword);

        boolean ucheck, mailcheck;
        String username = user.getText().toString();
        String email = mail.getText().toString();

        @NonNull
        String key = password.getText().toString();
        @NonNull
        String reKey = repassword.getText().toString();

        ucheck = userCheck(username);
        mailcheck = emailCheck(email);

        @NonNull
        boolean passcheck;
        if (key.equals("") || reKey.equals("")) {
            passcheck = false;
        } else passcheck = keyCheck(key, reKey);


        if (ucheck && mailcheck && passcheck) {
            DatabaseManager.databaseManager.addUser(username, key, email);
            Log.d("Database", "Value Inserted");
            Intent intent = new Intent();
            intent.putExtra("result", "User Registered");
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (!ucheck) {
                if (username.length() > 0) {
                    TextView erruser = findViewById(R.id.error_username);
                    erruser.setText(ALREADY_EXIST);
                    Log.d("Error ", "Error in username");
                    erruser.setVisibility(View.VISIBLE);
                } else {
                    TextView erruser = findViewById(R.id.error_username);
                    erruser.setText(VALID_USERNAME);
                    erruser.setVisibility(View.VISIBLE);
                }
            } else {
                TextView erruser = findViewById(R.id.error_username);
                erruser.setVisibility(View.GONE);

                if (!mailcheck) {
                    TextView errmail = findViewById(R.id.error_email);
                    Log.d("Error ", "Error in mailID");
                    errmail.setVisibility(View.VISIBLE);
                } else {
                    TextView errmail = findViewById(R.id.error_email);
                    Log.d("Error ", "Error in mailID");
                    errmail.setVisibility(View.GONE);
                    if (!passcheck) {
                        TextView errpass = findViewById(R.id.error_password);
                        Log.d("Error ", "Error in password");
                        errpass.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}
