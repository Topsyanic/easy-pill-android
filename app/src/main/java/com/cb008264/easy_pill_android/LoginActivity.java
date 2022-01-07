package com.cb008264.easy_pill_android;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cb008264.easy_pill_android.admin.AdminHomeActivity;
import com.cb008264.easy_pill_android.model.User;
import com.cb008264.easy_pill_android.utilities.PasswordHasher;
import com.google.gson.Gson;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewCustomer, textViewDoctor;
    private CheckBox rememberMe;
    private EditText mEmail;
    private EditText mPassword;
    private Button loginBtn;
    ProgressBar progressBar;
    private String checkBox;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textViewCustomer = findViewById(R.id.textViewCustomer);
        textViewCustomer.setOnClickListener(this);
        textViewDoctor = findViewById(R.id.textViewDoctor);
        textViewDoctor.setOnClickListener(this);
        rememberMe = findViewById(R.id.rememberMe);
        mEmail = findViewById(R.id.loginEmailText);
        mPassword = findViewById(R.id.loginPasswordText);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        getSupportActionBar().hide();
        preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        checkBox = preferences.getString("remember", "");
        if (checkBox.equalsIgnoreCase("true")) {
            String role = preferences.getString("role","");
            switch (role)
            {
                case "admin":{
                    Intent intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                    intent.putExtra("username", preferences.getString("username",""));
                    intent.putExtra("email", preferences.getString("email",""));
                    intent.putExtra("userId", preferences.getString("userId",""));
                    intent.putExtra("role",role );
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewCustomer:
                startActivity(new Intent(this, RegisterCustomerActivity.class));
                break;
            case R.id.textViewDoctor:
                startActivity(new Intent(this, RegisterDoctorActivity.class));
                break;
            case R.id.loginBtn:
                    login();
                break;
        }
    }

    private void login() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();


        if (email.isEmpty()) {
            mEmail.setError("Email cannot be blank");
            mEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mPassword.setError("Password cannot be blank");
            mPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please provide  a valid email address");
            mEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            mPassword.setError("Password must be at least 6 characters long");
            mPassword.requestFocus();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);

        String URL = "http://" + getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.user/user/" + mEmail.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.e("Rest Response",response.toString());
                Gson json = new Gson();
                User user = json.fromJson(String.valueOf(response), User.class);
                if (user.getPassword().equalsIgnoreCase(PasswordHasher.getHash(mPassword.getText().toString()))) {
                    switch (user.getUserRole()) {
                        case "admin": {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            if(rememberMe.isChecked())
                            {
                                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("remember", "true");
                                editor.putString("username", ""+user.getFirstName() + " " + user.getLastName());
                                editor.putString("email", ""+user.getEmail());
                                editor.putString("userId", ""+ user.getUserId());
                                editor.putString("role", ""+ user.getUserRole());
                                editor.apply();
                            }
                            else if (!rememberMe.isChecked())
                            {
                                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("remember", "false");
                                editor.putString("username", "");
                                editor.putString("email", "");
                                editor.putString("userId", "");
                                editor.putString("role", "");
                                editor.apply();
                            }
                            Intent intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                            intent.putExtra("username", user.getFirstName() + " " + user.getLastName());
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("userId", user.getUserId());
                            intent.putExtra("role", user.getUserRole());
                            startActivity(intent);
                            finish();
                            break;
                        }
                        default:
                            Toast.makeText(LoginActivity.this, "User Role Missing", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            break;
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

        );
        requestQueue.add(objectRequest);


    }

}