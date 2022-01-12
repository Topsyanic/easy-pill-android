package com.cb008264.easy_pill_android.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cb008264.easy_pill_android.R;


public class CustomerMenuFragment extends Fragment {
    View v;
    ImageButton browseBtn, cartBtn,ordersBtn,historyBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_customer_menu, container, false);
        browseBtn = v.findViewById(R.id.btn_browse_customer);
        cartBtn = v.findViewById(R.id.btn_cart_customer);
        ordersBtn = v.findViewById(R.id.btn_orders_customer);
        historyBtn = v.findViewById(R.id.btn_history_customer);

        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customerFragmentContainer, new CustomerBrowseFragment()).addToBackStack(null).commit();
            }
        });


        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customerFragmentContainer, new CustomerCartFragment()).addToBackStack(null).commit();
            }
        });

        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customerFragmentContainer, new CustomerOrderFragment()).addToBackStack(null).commit();
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customerFragmentContainer, new CustomerHistoryFragment()).addToBackStack(null).commit();
            }
        });
        return v;
    }
}