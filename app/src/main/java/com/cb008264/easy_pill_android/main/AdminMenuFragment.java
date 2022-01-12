package com.cb008264.easy_pill_android.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cb008264.easy_pill_android.R;

public class AdminMenuFragment extends Fragment {
    View v;
    ImageButton customerBtn,doctorBtn,pharmacistBtn,notesBtn,medicineBtn,ordersBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_admin_menu,container,false);
        customerBtn =v.findViewById(R.id.btn_customers);
        doctorBtn = v.findViewById(R.id.btn_doctors);
        pharmacistBtn = v.findViewById(R.id.btn_pharmacists);
        notesBtn = v.findViewById(R.id.btn_notes);
        medicineBtn = v.findViewById(R.id.btn_medicines);
        ordersBtn = v.findViewById(R.id.btn_orders);
        notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer, new AdminNotesFragment()).addToBackStack(null).commit();
            }
        });
        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer, new AdminCustomersFragment()).addToBackStack(null).commit();
            }
        });

        pharmacistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer, new AdminPharmacistsFragment()).addToBackStack(null).commit();
            }
        });

        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer, new AdminDoctorsFragment()).addToBackStack(null).commit();
            }
        });

        medicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer, new AdminMedicineFragment()).addToBackStack(null).commit();
            }
        });

        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer, new AdminOrderFragment()).addToBackStack(null).commit();
            }
        });

        return v;
    }
}
