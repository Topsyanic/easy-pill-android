package com.cb008264.easy_pill_android.main;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerBrowseFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    EditText searchTxt;
    List<Medicine> medicines;
    private String URL = "";
    public static MedicineAdapter medicineAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_customer_browse, container, false);
        recyclerView = v.findViewById(R.id.productList);
        searchTxt = v.findViewById(R.id.search_product);
        URL = "http://" + getActivity().getApplicationContext().getResources().getString(R.string.ip_address) + ":43175/easy-pill-war/webresources/entities.medicine/all";
        medicines = new ArrayList<>();
        extractProducts();
        medicineAdapter = new MedicineAdapter(getActivity().getApplicationContext(), medicines, new CustomerBrowseFragment.MedicineClickListener());
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

    private void extractProducts() {

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
                        medicines.add(medicine);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                medicineAdapter = new MedicineAdapter(getActivity().getApplicationContext(), medicines, new CustomerBrowseFragment.MedicineClickListener());
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
            startActivity(new Intent(getContext(), CustomerViewProductActivity.class).putExtra("data", medicine));
        }
    }
}