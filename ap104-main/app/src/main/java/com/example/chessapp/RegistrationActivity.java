package com.example.chessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);


        EditText emailBox = findViewById(R.id.register_email);
        EditText passwordBox = findViewById(R.id.register_password);
        EditText confirmBox = findViewById(R.id.register_confirm);
        EditText UserBox = findViewById(R.id.register_User);
        Button RegistrationButton = findViewById(R.id.registerButton);
        Button Registration2Login = findViewById(R.id.register2login);
        TextView txtIncorrect = findViewById(R.id.textRegisterIncorrect);
        RegistrationButton.setOnClickListener(v -> {

            if (confirmBox.getText().toString().trim().equals(passwordBox.getText().toString().trim()))
            {
                DatabaseMethods dbMethods = new DatabaseMethods();
                dbMethods.Register(getBaseContext(),UserBox.getText().toString().trim(),passwordBox.getText().toString().trim(),emailBox.getText().toString().trim(),txtIncorrect);
            }


        });
        Registration2Login.setOnClickListener(v -> {


            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            RegistrationActivity.this.startActivity(intent);


        });
    }
}
