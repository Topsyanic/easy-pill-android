package com.cb008264.easy_pill_android.main;

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

public class AdminViewDoctorActivity extends AppCompatActivity {
    private static final int CALL_REQUEST=1;
    TextView userIdTxt,nameTxt,emailTxt,contactTxt,addressTxt,expertiseTxt;
    ImageButton callBtn;
    User user;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_doctor);
        userIdTxt = findViewById(R.id.doc_userIdTxt);
        nameTxt = findViewById(R.id.doc_nameTxt);
        emailTxt = findViewById(R.id.doc_emailTxt);
        contactTxt = findViewById(R.id.doc_contactTxt);
        addressTxt = findViewById(R.id.doc_addressTxt);
        expertiseTxt = findViewById(R.id.doc_expertiseTxt);
        callBtn = findViewById(R.id.callDocBtn);
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
            expertiseTxt.setText(user.getExpertise());
        }

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
    }
    private void makeCall() {

        if(contactTxt.getText().toString().trim().length()>0) {
            if (ContextCompat.checkSelfPermission(AdminViewDoctorActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdminViewDoctorActivity.this,new String[]{Manifest.permission.CALL_PHONE},CALL_REQUEST);
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