package com.cb008264.easy_pill_android.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.Cart;
import com.cb008264.easy_pill_android.model.CustomerOrder;
import com.cb008264.easy_pill_android.utilities.RestMessage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CustomerCartFragment extends Fragment {
    SharedPreferences preferences;
    View v;
    RecyclerView recyclerView;
    EditText searchTxt;
    ImageButton addBtn, minusBtn, deleteBtn;
    public static ImageButton finishBtn;
    List<Cart> items;
    AlertDialog.Builder builder;
    public static TextView totalAmount;
    private String URL = "";
    String userId;
    public static CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_customer_cart, container, false);
        recyclerView = v.findViewById(R.id.cartList);
        searchTxt = v.findViewById(R.id.search_cart);
        addBtn = v.findViewById(R.id.btn_plus_cart);
        minusBtn = v.findViewById(R.id.btn_minus_cart);
        deleteBtn = v.findViewById(R.id.btn_delete_cart);
        finishBtn = v.findViewById(R.id.finishButton);
        totalAmount = v.findViewById(R.id.total_amount);
        preferences = getActivity().getSharedPreferences("checkbox", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.cart";
        items = new ArrayList<>();
        extractCart();
        cartAdapter = new CartAdapter(getActivity().getApplicationContext(), items, new CustomerCartFragment.CartClickListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(cartAdapter);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CustomerCompleteActivity.class);
                intent.putExtra("total", totalAmount.getText().toString());
                intent.putExtra("details", cartAdapter.getOrderDetails());
                intent.putExtra("userId", userId);
                startActivity(intent);
                totalAmount.setText(String.valueOf(cartAdapter.updateTotalInCart()));
            }
        });

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
        ArrayList<Cart> filteredOrders = new ArrayList<>();

        for (Cart cartItem : items) {
            if (cartItem.getProductName().toLowerCase().contains(text.toLowerCase()) || cartItem.getSubTotal().toLowerCase().contains(text.toLowerCase())) {
                filteredOrders.add(cartItem);
            }

        }
        cartAdapter.filterList(filteredOrders);
    }

    private void extractCart() {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject cartObject = response.getJSONObject(i);
                        Cart cartItem = new Cart();
                        cartItem.setCartId(cartObject.getString("cartId").toString());
                        cartItem.setMedicineId(cartObject.getString("medicineId").toString());
                        cartItem.setProductName(cartObject.getString("productName").toString());
                        cartItem.setQuantity(cartObject.getInt("quantity"));
                        cartItem.setSubTotal(cartObject.getString("subTotal").toString());
                        cartItem.setUserId(cartObject.getString("userId").toString());
                        items.add(cartItem);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                cartAdapter = new CartAdapter(getActivity().getApplicationContext(), items, new CustomerCartFragment.CartClickListener());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(cartAdapter);
                if (cartAdapter.getItemCount() > 0 ){
                    finishBtn.setVisibility(View.VISIBLE);
                }
                else {
                    finishBtn.setVisibility(View.INVISIBLE);
                }
                totalAmount.setText(String.valueOf(cartAdapter.updateTotalInCart()));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);

    }

    public class CartClickListener implements CartAdapter.CartClickListener {

        @Override
        public void increaseQty(Cart cartItem, int position) {

            String URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.cart/plus/" + cartItem.getCartId() + "/" + cartItem.getMedicineId();
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson json = new Gson();
                    Cart cartItem = json.fromJson(String.valueOf(response), Cart.class);
                    cartAdapter.updateItemInCart(position, cartItem);
                    totalAmount.setText(String.valueOf(cartAdapter.updateTotalInCart()));
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), "Failed to increase quantity", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(objectRequest);

        }

        @Override
        public void reduceQty(Cart cartItem, int position) {
            if (cartItem.getQuantity() > 1) {
                String URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.cart/minus/" + cartItem.getCartId() + "/" + cartItem.getMedicineId();
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson json = new Gson();
                        Cart cartItem = json.fromJson(String.valueOf(response), Cart.class);
                        cartAdapter.updateItemInCart(position, cartItem);
                        totalAmount.setText(String.valueOf(cartAdapter.updateTotalInCart()));
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity().getApplicationContext(), "Failed to reduce quantity", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue.add(objectRequest);
            }
        }


        @Override
        public void removeItem(Cart cartItem, int position) {
            builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Remove Item")
                    .setMessage("Do you want to remove this item ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.cart/remove/" + cartItem.getCartId();
                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Rest Response", response.toString());
                                    Gson json = new Gson();
                                    RestMessage message = json.fromJson(String.valueOf(response), RestMessage.class);
                                    if (message.getMessage().equalsIgnoreCase("success")) {
                                        cartAdapter.removeItemFromCart(position);
                                        totalAmount.setText(String.valueOf(cartAdapter.updateTotalInCart()));
                                        updateUI();
                                        Toast.makeText(getActivity().getApplicationContext(), "Removed Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getActivity().getApplicationContext(), "Remove Failed!", Toast.LENGTH_SHORT).show();
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
        void updateUI(){
            if (cartAdapter.getItemCount() > 0 ){
                finishBtn.setVisibility(View.VISIBLE);
            }
            else {
                finishBtn.setVisibility(View.INVISIBLE);
            }
        }
    }
}