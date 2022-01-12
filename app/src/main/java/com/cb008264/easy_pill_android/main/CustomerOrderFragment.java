package com.cb008264.easy_pill_android.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.CustomerOrder;
import com.cb008264.easy_pill_android.utilities.RestMessage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CustomerOrderFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    EditText searchTxt;
    ImageButton cancelOrderBtn;
    List<CustomerOrder> orders;
    AlertDialog.Builder builder;
    String userId;
    private String URL = "";
    public static CustomerOrderAdapter customerOrderAdapter;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_customer_order, container, false);
        recyclerView = v.findViewById(R.id.orderList);
        searchTxt = v.findViewById(R.id.search_order);
        cancelOrderBtn = v.findViewById(R.id.customer_order_btn_cancel);
        preferences = getActivity().getSharedPreferences("checkbox", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.customerorder/userorders/"+userId;
        orders = new ArrayList<>();
        extractOrders();
        customerOrderAdapter = new CustomerOrderAdapter(getActivity().getApplicationContext(), orders, new CustomerOrderFragment.CustomerOrderClickListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(customerOrderAdapter);



        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return v;
    }
    private void filter(String text) {
        ArrayList<CustomerOrder> filteredOrders = new ArrayList<>();

        for (CustomerOrder order : orders) {
            if (order.getOrderId().toLowerCase().contains(text.toLowerCase()) || order.getDate().toLowerCase().contains(text.toLowerCase()) || order.getStatus().toLowerCase().contains(text.toLowerCase())) {
                filteredOrders.add(order);
            }

        }
        customerOrderAdapter.filterList(filteredOrders);
    }

    private void extractOrders() {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject orderObject = response.getJSONObject(i);
                        CustomerOrder order = new CustomerOrder();
                        order.setAddress(orderObject.getString("address").toString());
                        order.setAmount(orderObject.getString("amount").toString());
                        order.setDate(orderObject.getString("date").toString());
                        order.setDetails(orderObject.getString("details").toString());
                        order.setOrderId(orderObject.getString("orderId").toString());
                        order.setStatus(orderObject.getString("status").toString());
                        order.setUserId(orderObject.getString("userId").toString());

                        orders.add(order);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                customerOrderAdapter = new CustomerOrderAdapter(getActivity().getApplicationContext(), orders, new CustomerOrderFragment.CustomerOrderClickListener());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(customerOrderAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public class CustomerOrderClickListener implements CustomerOrderAdapter.CustomerOrderClickListener {

        @Override
        public void selectedOrder(CustomerOrder order) {
            startActivity(new Intent(getContext(), AdminViewOrderActivity.class).putExtra("data", order));
        }

        @Override
        public void cancelOrder (CustomerOrder order, int position) {
            builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Cancel Order")
                    .setMessage("Do you want to cancel this order?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.customerorder/cancel/" + order.getOrderId();
                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Rest Response", response.toString());
                                    Gson json = new Gson();
                                    RestMessage message = json.fromJson(String.valueOf(response), RestMessage.class);
                                    if (message.getMessage().equalsIgnoreCase("success")) {
                                        customerOrderAdapter.removeItem(position);
                                        Toast.makeText(getActivity().getApplicationContext(), "Order Cancelled Successfully", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getActivity().getApplicationContext(), "Cancel Order Failed!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                            requestQueue.add(objectRequest);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();

        }
    }
}