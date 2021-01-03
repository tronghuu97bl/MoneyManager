package com.tth.moneymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Helper.CommonHelper;
import com.tth.moneymanager.Model.User;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private TextView tv_register, tv_warning;
    private EditText edit_email, edit_pass;
    private String email, pass;
    private DbHelper db;
    private ImageView finger;
    private Executor executor;
    private Util util;
    private androidx.biometric.BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        util = new Util(this);
        db = new DbHelper(this);
        login = findViewById(R.id.login);
        finger = findViewById(R.id.finger);
        tv_register = findViewById(R.id.text_register);
        tv_warning = findViewById(R.id.text_warning);
        edit_email = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_pass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edit_email.getText().toString();
                pass = edit_pass.getText().toString();
                if (email.equals("") || pass.equals("")) {
                    tv_warning.setVisibility(View.VISIBLE);
                    tv_warning.setText("Please enter your email address and password!");
                } else {
                    tv_warning.setVisibility(View.GONE);
                    User user = db.userLogin(email, pass);
                    if (user != null) {
                        util.addUserToPreference(user);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        tv_warning.setVisibility(View.VISIBLE);
                        tv_warning.setText("Please check your email address and password!");
                    }
                }
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //finger
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new androidx.biometric.BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                CommonHelper.showLoading(LoginActivity.this);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Use fingerprint to login")
                .setNegativeButtonText("Use account password")
                .build();
        finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check boolean exist user login
                //TODO

                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

}