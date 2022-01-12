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
import com.cb008264.easy_pill_android.model.User;
import com.cb008264.easy_pill_android.utilities.RestMessage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdminCustomersFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    EditText searchTxt;
    ImageButton addCustomerBtn, removeCustomerBtn;
    List<User> customers;
    public static String USER_ROLE;
    AlertDialog.Builder builder;
    private String URL = "";
    public static CustomerAdapter customerAdapter;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_admin_customers, container, false);
        recyclerView = v.findViewById(R.id.customersList);
        searchTxt = v.findViewById(R.id.search_customer);
        addCustomerBtn = v.findViewById(R.id.addCustomerButton);
        removeCustomerBtn = v.findViewById(R.id.btn_delete);
        URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.user/customers";
        customers = new ArrayList<>();
        extractCustomers();
        customerAdapter = new CustomerAdapter(getActivity().getApplicationContext(), customers, new CustomClickListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(customerAdapter);
        preferences = getActivity().getSharedPreferences("checkbox", MODE_PRIVATE);
        USER_ROLE = preferences.getString("role","");
        if(USER_ROLE.equalsIgnoreCase("doctor")){
            addCustomerBtn.setVisibility(View.INVISIBLE);
        }
        addCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAddCustomerActivity.class));
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
        ArrayList<User> filteredUsers = new ArrayList<>();

        for (User user : customers) {
            if (user.getFirstName().toLowerCase().contains(text.toLowerCase()) || user.getLastName().toLowerCase().contains(text.toLowerCase()) || user.getContactNo().toLowerCase().contains(text.toLowerCase()) || user.getEmail().toLowerCase().contains(text.toLowerCase())) {
                filteredUsers.add(user);
            }

        }
        customerAdapter.filterList(filteredUsers);
    }

    private void extractCustomers() {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject userObject = response.getJSONObject(i);
                        User user = new User();
                        user.setFirstName(userObject.getString("firstName").toString());
                        user.setLastName(userObject.getString("lastName").toString());
                        user.setEmail(userObject.getString("email").toString());
                        user.setContactNo(userObject.getString("contactNo").toString());
                        user.setUserId(userObject.getString("userId").toString());
                        user.setAddress(userObject.getString("address").toString());
                        user.setUserRole(userObject.getString("userRole").toString());

                        customers.add(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                customerAdapter = new CustomerAdapter(getActivity().getApplicationContext(), customers, new CustomClickListener());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(customerAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public class CustomClickListener implements CustomerAdapter.CustomerClickListener {

        @Override
        public void selectedCustomer(User user) {
            startActivity(new Intent(getContext(), AdminViewUserActivity.class).putExtra("data", user));
        }

        @Override
        public void deleteCustomer(User user, int position) {
            builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Customer")
                    .setMessage("Do you want to remove this customer?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.user/delete/" + user.getUserId();
                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Rest Response", response.toString());
                                    Gson json = new Gson();
                                    RestMessage message = json.fromJson(String.valueOf(response), RestMessage.class);
                                    if (message.getMessage().equalsIgnoreCase("success")) {
                                        customerAdapter.removeItem(position);
                                        Toast.makeText(getActivity().getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getActivity().getApplicationContext(), "Delete Failed!", Toast.LENGTH_LONG).show();
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