package com.cb008264.easy_pill_android.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


public class AdminDoctorsFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    EditText searchTxt;
    ImageButton addDoctorBtn, removeDoctorBtn;
    List<User> doctors;
    AlertDialog.Builder builder;
    private String URL = "";
    public static DoctorAdapter doctorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_admin_doctors, container, false);
        recyclerView = v.findViewById(R.id.doctorsList);
        searchTxt = v.findViewById(R.id.search_doctor);
        addDoctorBtn = v.findViewById(R.id.addDoctorButton);
        removeDoctorBtn = v.findViewById(R.id.doctor_btn_delete);
        URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.user/doctors";
        doctors = new ArrayList<>();
        extractDoctors();
        doctorAdapter = new DoctorAdapter(getActivity().getApplicationContext(), doctors, new AdminDoctorsFragment.DoctorClickListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(doctorAdapter);
        addDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAddDoctorActivity.class));
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

        for (User user : doctors) {
            if (user.getFirstName().toLowerCase().contains(text.toLowerCase()) || user.getLastName().toLowerCase().contains(text.toLowerCase()) || user.getContactNo().toLowerCase().contains(text.toLowerCase()) || user.getEmail().toLowerCase().contains(text.toLowerCase())) {
                filteredUsers.add(user);
            }

        }
        doctorAdapter.filterList(filteredUsers);
    }

    private void extractDoctors() {

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
                        user.setExpertise(userObject.getString("expertise").toString());

                        doctors.add(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                doctorAdapter = new DoctorAdapter(getActivity().getApplicationContext(), doctors, new AdminDoctorsFragment.DoctorClickListener());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(doctorAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public class DoctorClickListener implements DoctorAdapter.DoctorClickListener {

        @Override
        public void selectedDoctor(User user) {
            startActivity(new Intent(getContext(), AdminViewDoctorActivity.class).putExtra("data", user));
        }

        @Override
        public void deleteDoctor(User user, int position) {
            builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Doctor")
                    .setMessage("Do you want to remove this Doctor?")
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
                                        doctorAdapter.removeItem(position);
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