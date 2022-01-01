package com.cb008264.easy_pill_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cb008264.easy_pill_android.admin.AdminHomeActivity;
import com.cb008264.easy_pill_android.model.User;
import com.cb008264.easy_pill_android.utilities.PasswordHasher;
import com.cb008264.easy_pill_android.utilities.RestMessage;
import com.cb008264.easy_pill_android.utilities.UserIdGenerator;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegisterCustomerActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView firstNameText, lastNameText, emailText, passwordText, confirmPasswordText, contactText, addressText;
    private ProgressBar progressBar;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);
        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        emailText = findViewById(R.id.emailText);
        contactText = findViewById(R.id.contactText);
        addressText = findViewById(R.id.addressText);
        passwordText = findViewById(R.id.passwordText);
        confirmPasswordText = findViewById(R.id.passwordText2);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                try {
                    registerUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void registerUser() throws JSONException {

        String firstName = firstNameText.getText().toString().trim();
        String lastName = lastNameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String contact = contactText.getText().toString().trim();
        String address = addressText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmPassword = confirmPasswordText.getText().toString().trim();

        if (firstName.isEmpty()) {
            firstNameText.setError("First Name cannot be blank");
            firstNameText.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            lastNameText.setError("Last Name cannot be blank");
            lastNameText.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailText.setError("Email cannot be blank");
            emailText.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            addressText.setError("Address cannot be blank");
            addressText.requestFocus();
            return;
        }
        if(contact.length() > 10 )
        {
            contactText.setError("Contact should be 10 characters long");
            contactText.requestFocus();
            return;
        }
        if(!contact.startsWith("07"))
        {
            contactText.setError("Contact should start with '07'");
            contactText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordText.setError("Password cannot be blank");
            passwordText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordText.setError("Password cannot be blank");
            passwordText.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordText.setError("Confirm Password cannot be blank");
            confirmPasswordText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Please provide  a valid email address");
            emailText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters long");
            passwordText.requestFocus();
            return;

        }
        if (!password.equalsIgnoreCase(confirmPassword)) {
            confirmPasswordText.setError("Password is not matching");
            confirmPasswordText.requestFocus();
            return;

        }
        progressBar.setVisibility(View.GONE);
        String URL2 = "http://" + getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.user";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("address", "" + address);
        jsonBody.put("contactNo", "" + contact);
        jsonBody.put("email", "" + email);
        jsonBody.put("firstName", "" + firstName);
        jsonBody.put("lastName", "" + lastName);
        jsonBody.put("password", "" + PasswordHasher.getHash(password));
        jsonBody.put("userId", "" + new UserIdGenerator().generateNumber());
        jsonBody.put("userRole", "customer");
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                Toast.makeText(RegisterCustomerActivity.this,"Account Created Successfully",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());

            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        String URL = "http://" + getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.user/mailcheck/" + emailText.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.e("Rest Response",response.toString());
                Gson json = new Gson();
                RestMessage message = json.fromJson(String.valueOf(response), RestMessage.class);
                if (message.getMessage().equalsIgnoreCase("invalid")) {
                    emailText.setError("Email In Use");
                    emailText.requestFocus();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    requestQueue.add(stringRequest);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterCustomerActivity.this, "Invalid Details", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

        );
        requestQueue.add(objectRequest);
    }
}