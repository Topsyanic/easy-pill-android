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
import com.cb008264.easy_pill_android.model.Medicine;
import com.cb008264.easy_pill_android.model.User;
import com.cb008264.easy_pill_android.utilities.RestMessage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


public class AdminMedicineFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    EditText searchTxt;
    List<Medicine> medicines;
    private String URL = "";
    public static MedicineAdapter medicineAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_admin_medicine, container, false);
        recyclerView = v.findViewById(R.id.medicineList);
        searchTxt = v.findViewById(R.id.search_medicine);
        URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.medicine";
        medicines = new ArrayList<>();
        extractMedicines();
        medicineAdapter = new MedicineAdapter(getActivity().getApplicationContext(), medicines, new AdminMedicineFragment.MedicineClickListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(medicineAdapter);

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
        ArrayList<Medicine> filteredMedicine = new ArrayList<>();

        for (Medicine medicine : medicines) {
            if (medicine.getName().toLowerCase().contains(text.toLowerCase()) || medicine.getDescription().toLowerCase().contains(text.toLowerCase()) || medicine.getPrice().toLowerCase().contains(text.toLowerCase())) {
                filteredMedicine.add(medicine);
            }

        }
        medicineAdapter.filterList(filteredMedicine);
    }

    private void extractMedicines() {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject medicineObject = response.getJSONObject(i);
                        Medicine medicine = new Medicine();
                        medicine.setDescription(medicineObject.getString("description").toString());
                        medicine.setImagePath(medicineObject.getString("imagePath").toString());
                        medicine.setMedicineId(medicineObject.getString("medicineId").toString());
                        medicine.setName(medicineObject.getString("name").toString());
                        medicine.setPrice(medicineObject.getString("price").toString());
                        medicine.setQuantity(medicineObject.getString("quantity").toString());
                        medicine.setRequirePres(medicineObject.getString("requirePres").toString());
                        medicine.setSupplierId(medicineObject.getString("supplierId").toString());
                        medicine.setWeight(medicineObject.getString("weight").toString());
                        Log.d("tag", "WORKING "+medicine.toString());
                        medicines.add(medicine);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                medicineAdapter = new MedicineAdapter(getActivity().getApplicationContext(), medicines, new AdminMedicineFragment.MedicineClickListener());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(medicineAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public class MedicineClickListener implements MedicineAdapter.MedicineClickListener {

        @Override
        public void selectedMedicine(Medicine medicine) {
            startActivity(new Intent(getContext(), AdminViewDoctorActivity.class).putExtra("data", medicine));
        }
    }
}