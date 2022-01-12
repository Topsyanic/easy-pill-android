package com.cb008264.easy_pill_android.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cb008264.easy_pill_android.R;


public class DoctorMenuFragment extends Fragment {
View v;
ImageButton browsebtn, cartBtn, patientsBtn,ordersBtn,historyBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_doctor_menu, container, false);
        browsebtn = v.findViewById(R.id.btn_browse_doctor);
        cartBtn = v.findViewById(R.id.btn_cart_doctor);
        patientsBtn = v.findViewById(R.id.btn_patients_doctor);
        ordersBtn = v.findViewById(R.id.btn_orders_doctor);
        historyBtn = v.findViewById(R.id.btn_history_doctor);

        browsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.doctorFragmentContainer, new CustomerBrowseFragment()).addToBackStack(null).commit();
            }
        });

        patientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.doctorFragmentContainer, new AdminCustomersFragment()).addToBackStack(null).commit();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.doctorFragmentContainer, new CustomerCartFragment()).addToBackStack(null).commit();
            }
        });

        return v;
    }
}