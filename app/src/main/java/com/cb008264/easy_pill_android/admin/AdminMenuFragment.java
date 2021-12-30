package com.cb008264.easy_pill_android.admin;

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
    TextView greetings;
    ImageButton rideButton,bookingsButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_admin_menu,container,false);

        return v;
    }
}
