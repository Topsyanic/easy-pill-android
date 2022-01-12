package com.cb008264.easy_pill_android.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cb008264.easy_pill_android.R;

public class PharmacistMenuFragment extends Fragment {
    View v;
    ImageButton customerBtn,doctorBtn,pharmacistBtn,medicineBtn,ordersBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.fragment_pharmacist_menu, container, false);
        customerBtn =v.findViewById(R.id.btn_customers_pharmacist);
        doctorBtn = v.findViewById(R.id.btn_doctors_pharmacist);
        pharmacistBtn = v.findViewById(R.id.btn_pharmacists_pharmacist);
        medicineBtn = v.findViewById(R.id.btn_medicines_pharmacist);
        ordersBtn = v.findViewById(R.id.btn_orders_pharmacist);

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminCustomersFragment()).addToBackStack(null).commit();
            }
        });

        pharmacistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminPharmacistsFragment()).addToBackStack(null).commit();
            }
        });

        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminDoctorsFragment()).addToBackStack(null).commit();
            }
        });

        medicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminMedicineFragment()).addToBackStack(null).commit();
            }
        });

        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminOrderFragment()).addToBackStack(null).commit();
            }
        });
        return v;
    }
}