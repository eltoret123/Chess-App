package com.example.chessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        EditText emailBox = findViewById(R.id.login_email);
        EditText passwordBox = findViewById(R.id.login_password);
        Button LoginButton = findViewById(R.id.loginButton);
        Button RegisterButton = findViewById(R.id.registerButtonLogin);
        TextView txtIncorrect = findViewById(R.id.textILoginIncorrect);
        LoginButton.setOnClickListener(v -> {
            DatabaseMethods dbMethods = new DatabaseMethods();
            dbMethods.checkLogin(getBaseContext(), emailBox.getText().toString().trim(), passwordBox.getText().toString().trim(), txtIncorrect);
        });
        RegisterButton.setOnClickListener(v -> {

            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

}
