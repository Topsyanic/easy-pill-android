package com.cb008264.easy_pill_android.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.Medicine;

public class AdminViewMedicineActivity extends AppCompatActivity {
    TextView nameTxt,priceTxt,quantityTxt,weightTxt,prescriptionTxt,descriptionTxt;
    ImageView imageView;
    Medicine medicine;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_medicine);
        nameTxt = findViewById(R.id.med_nameTxt);
        priceTxt = findViewById(R.id.med_priceTxt);
        quantityTxt = findViewById(R.id.med_quantityTxt);
        weightTxt = findViewById(R.id.med_weightTxt);
        prescriptionTxt = findViewById(R.id.med_requirePresTxt);
        descriptionTxt = findViewById(R.id.med_descriptionTxt);
        imageView = findViewById(R.id.med_imageView);
        getSupportActionBar().setElevation(0);
        intent = getIntent();

        if(intent != null)
        {
            medicine = (Medicine) intent.getSerializableExtra("data");
            nameTxt.setText(medicine.getName());
            priceTxt.setText(medicine.getPrice());
            quantityTxt.setText(medicine.getQuantity());
            weightTxt.setText(medicine.getWeight());
            prescriptionTxt.setText(medicine.getRequirePres());
            descriptionTxt.setText(medicine.getDescription());
            String medicineImageName = medicine.getImagePath().substring(medicine.getImagePath().lastIndexOf("\\")+1);
            Glide.with(imageView).load("http://192.168.8.100:43175/easy-pill-war/ProductImages/"+medicineImageName).into(imageView);
        }
    }
}