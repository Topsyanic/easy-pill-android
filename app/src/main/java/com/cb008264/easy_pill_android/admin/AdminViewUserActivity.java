package com.cb008264.easy_pill_android.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.User;

public class AdminViewUserActivity extends AppCompatActivity {
    private static final int CALL_REQUEST=1;
    TextView userIdTxt,nameTxt,emailTxt,contactTxt,addressTxt;
    ImageButton callCusBtn;
    User user;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user);
        userIdTxt = findViewById(R.id.cus_userIdTxt);
        nameTxt = findViewById(R.id.cus_nameTxt);
        emailTxt = findViewById(R.id.cus_emailTxt);
        contactTxt = findViewById(R.id.cus_contactTxt);
        addressTxt = findViewById(R.id.cus_addressTxt);
        callCusBtn = findViewById(R.id.callCusBtn);
        getSupportActionBar().setElevation(0);
        intent = getIntent();

        if(intent != null)
        {
            user = (User) intent.getSerializableExtra("data");
            userIdTxt.setText(user.getUserId());
            nameTxt.setText(user.getFirstName()+" "+user.getLastName());
            emailTxt.setText(user.getEmail());
            contactTxt.setText(user.getContactNo());
            addressTxt.setText(user.getAddress());
        }

        callCusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
    }

    private void makeCall() {

        if(contactTxt.getText().toString().trim().length()>0) {
            if (ContextCompat.checkSelfPermission(AdminViewUserActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdminViewUserActivity.this,new String[]{Manifest.permission.CALL_PHONE},CALL_REQUEST);
            }
            else
            {
                String dial = "tel:"+contactTxt.getText().toString();
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
        else
        {
            Toast.makeText(this,"Customer has invalid phone number",Toast.LENGTH_LONG).show();
        }
        Intent intent2 = new Intent(Intent.ACTION_CALL);
        intent2.setData(Uri.parse("tel:"+contactTxt.getText().toString()));
        startActivity(intent2);
    }
}