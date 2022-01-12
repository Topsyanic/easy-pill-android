package com.cb008264.easy_pill_android.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.CustomerOrder;

public class AdminViewOrderActivity extends AppCompatActivity {
    TextView orderIdTxt,userIdTxt,dateTxt,statusTxt,priceTxt,addressTxt,detailsTxt;
    CustomerOrder order;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_order);
        orderIdTxt = findViewById(R.id.order_orderIdTxt);
        userIdTxt = findViewById(R.id.order_userIdTxt);
        dateTxt = findViewById(R.id.order_dateTxt);
        statusTxt = findViewById(R.id.order_statusTxt);
        priceTxt = findViewById(R.id.order_priceTxt);
        addressTxt = findViewById(R.id.order_addressTxt);
        detailsTxt = findViewById(R.id.order_detailsTxt);
        getSupportActionBar().setElevation(0);
        intent = getIntent();

        if(intent != null)
        {
            order = (CustomerOrder) intent.getSerializableExtra("data");
            orderIdTxt.setText(order.getOrderId());
            userIdTxt.setText(order.getUserId());
            dateTxt.setText(order.getDate());
            statusTxt.setText(order.getStatus());
            priceTxt.setText(order.getAmount());
            addressTxt.setText(order.getAddress());
            detailsTxt.setText(order.getDetails());
        }
    }
}