package com.cb008264.easy_pill_android.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<User> users;
    DoctorAdapter.DoctorClickListener doctorClickListener;

    public DoctorAdapter(Context context, List<User> users, DoctorAdapter.DoctorClickListener doctorClickListener)
    {
        this.inflater =  LayoutInflater.from(context);
        this.users = users;
        this.doctorClickListener = doctorClickListener;
    }



    public interface DoctorClickListener
    {
        void selectedDoctor(User user);
        void deleteDoctor(User user,int position);
    }



    @NonNull
    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doctors_list_layout,parent,false);
        return new DoctorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.doctorName.setText(users.get(position).getFirstName()+" "+users.get(position).getLastName());
        holder.doctorEmail.setText(users.get(position).getEmail());
        holder.doctorContact.setText(users.get(position).getContactNo());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorClickListener.deleteDoctor(user,position);
            }
        });

        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorClickListener.selectedDoctor(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  users.size();
    }
    public void filterList(ArrayList<User> filteredList){
        users = filteredList;
        notifyDataSetChanged();
    }
    public void addItem(User user) {
        users.add(user);
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        users.remove(position);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView doctorName,doctorEmail,doctorContact;
        ImageButton arrowBtn,deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorEmail = itemView.findViewById(R.id.doctorEmail);
            doctorContact = itemView.findViewById(R.id.doctorContact);
            arrowBtn = itemView.findViewById(R.id.doctor_btn_arrow);
            deleteBtn = itemView.findViewById(R.id.doctor_btn_delete);
        }
    }
}
