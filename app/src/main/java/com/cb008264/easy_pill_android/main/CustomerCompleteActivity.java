package com.cb008264.easy_pill_android.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.utilities.RestMessage;
import com.google.gson.Gson;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static com.cb008264.easy_pill_android.main.CustomerCartFragment.cartAdapter;
import static com.cb008264.easy_pill_android.main.CustomerCartFragment.finishBtn;
import static com.cb008264.easy_pill_android.main.CustomerCartFragment.totalAmount;

public class CustomerCompleteActivity extends AppCompatActivity {
    Button paymentBtn;
    TextView payment_date, payment_amount, payment_address, payment_card_number, payment_name, payment_cvv;
    String total, details, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complete);
        payment_address = findViewById(R.id.payment_address);
        payment_card_number = findViewById(R.id.payment_card_number);
        payment_cvv = findViewById(R.id.payment_cvv);
        payment_date = findViewById(R.id.payment_date);
        payment_amount = findViewById(R.id.payment_amount);
        payment_name = findViewById(R.id.payment_name);
        total = getIntent().getExtras().getString("total");
        details = getIntent().getExtras().getString("details");
        userId = getIntent().getExtras().getString("userId");
        payment_amount.setText(total);
        paymentBtn = findViewById(R.id.paymentBtn);

        payment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar today = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(CustomerCompleteActivity.this, new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        payment_date.setText((selectedMonth + 1) + "/" + selectedYear);

                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setActivatedMonth(Calendar.JANUARY)
                        .setMinYear(2022)
                        .setActivatedYear(today.get(Calendar.YEAR))
                        .setMaxYear(2035)
                        .setTitle("Select month and year")
                        .build().show();
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();
            }
        });


    }


    private void makePayment() {
        String address = payment_address.getText().toString().trim();
        String cardName = payment_name.getText().toString().trim();
        String cardNumber = payment_card_number.getText().toString().trim();
        String cardCvv = payment_cvv.getText().toString().trim();
        String expiryDate = payment_date.getText().toString().trim();

        if (address.isEmpty()) {
            payment_address.setError("Address cannot be blank");
            payment_address.requestFocus();
            return;
        }
        if (cardName.isEmpty()) {
            payment_name.setError("Card Name cannot be blank");
            payment_name.requestFocus();
            return;
        }
        if (cardNumber.isEmpty()) {
            payment_card_number.setError("Card Number cannot be blank");
            payment_card_number.requestFocus();
            return;
        }
        if (cardNumber.length() != 16) {
            payment_card_number.setError("Card Number must be 16 digits long");
            payment_card_number.requestFocus();
            return;
        }

        if (cardCvv.isEmpty()) {
            payment_cvv.setError("CVV cannot be blank");
            payment_cvv.requestFocus();
            return;
        }

        if (cardCvv.length() != 3) {
            payment_cvv.setError("CVV must be3 digits long");
            payment_cvv.requestFocus();
            return;

        }
        if (expiryDate.equalsIgnoreCase("MM/YYYY")) {
            payment_date.setError("Expiry date must be selected");
            payment_date.requestFocus();
            return;
        }

        String URL = "http://" + getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.customerorder/pay/" + details+"/"+total+"/"+userId+"/"+payment_address.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Rest Response", response.toString());
                Gson json = new Gson();
                RestMessage message = json.fromJson(String.valueOf(response), RestMessage.class);
                if (message.getMessage().equalsIgnoreCase("success")) {
                    cartAdapter.emptyCart();
                    totalAmount.setText("0");
                    finishBtn.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Order Can be visible in the order option", Toast.LENGTH_SHORT).show();
                }
                else if(message.getMessage().equalsIgnoreCase("failed")){
                    Toast.makeText(getApplicationContext(), "Order couldn't be placed", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Order Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(objectRequest);
        finish();

    }

}