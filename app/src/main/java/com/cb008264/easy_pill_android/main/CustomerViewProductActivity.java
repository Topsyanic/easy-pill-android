package com.cb008264.easy_pill_android.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.Medicine;
import com.cb008264.easy_pill_android.utilities.RestMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

public class CustomerViewProductActivity extends AppCompatActivity {
    SharedPreferences preferences;
    TextView nameTxt,priceTxt,quantityTxt,weightTxt,descriptionTxt,qtyText;
    ImageView imageView;
    Button plusBtn,minusBtn,addToCartBtn;
    Medicine medicine;
    int counter,maxQty;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_product);
        nameTxt = findViewById(R.id.med_nameTxt_customer);
        priceTxt = findViewById(R.id.med_priceTxt_customer);
        quantityTxt = findViewById(R.id.med_quantityTxt_customer);
        weightTxt = findViewById(R.id.med_weightTxt_customer);
        descriptionTxt = findViewById(R.id.med_descriptionTxt_customer);
        imageView = findViewById(R.id.med_imageView_customer);
        qtyText = findViewById(R.id.qtyCounter);
        plusBtn = findViewById(R.id.increaseButton);
        minusBtn = findViewById(R.id.reduceButton);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        getSupportActionBar().setElevation(0);
        intent = getIntent();
        if(intent != null)
        {
            medicine = (Medicine) intent.getSerializableExtra("data");
            nameTxt.setText(medicine.getName());
            priceTxt.setText(medicine.getPrice());
            quantityTxt.setText(medicine.getQuantity());
            weightTxt.setText(medicine.getWeight());
            descriptionTxt.setText(medicine.getDescription());
            maxQty = Integer.parseInt(medicine.getQuantity());
            String medicineImageName = medicine.getImagePath().substring(medicine.getImagePath().lastIndexOf("\\")+1);
            Glide.with(imageView).load("http://192.168.8.101:43175/easy-pill-war/ProductImages/"+medicineImageName).into(imageView);
        }
        counter = Integer.parseInt(qtyText.getText().toString());

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maxQty > counter){
                    counter++;
                    qtyText.setText(String.valueOf(counter));
                }
                else
                {
                    Toast.makeText(CustomerViewProductActivity.this,"Max amount reached",Toast.LENGTH_SHORT).show();
                }
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(1 < counter){
                    counter--;
                    qtyText.setText(String.valueOf(counter));
                }else
                {
                    Toast.makeText(CustomerViewProductActivity.this,"Min amount reached",Toast.LENGTH_SHORT).show();
                }
            }
        });


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "http://" + getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.cart/"+preferences.getString("userId","")+"/"+medicine.getMedicineId()+"/"+counter;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());
                        Gson json = new Gson();
                        RestMessage message = json.fromJson(String.valueOf(response), RestMessage.class);
                        if (message.getMessage().equalsIgnoreCase("success")) {
                            Toast.makeText(getApplicationContext(), "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Failed to add to Cart", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue.add(objectRequest);
                finish();
            }
        });
    }

}