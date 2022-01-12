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

import static com.cb008264.easy_pill_android.main.AdminPharmacistsFragment.USER_ROLE;

public class PharmacistAdapter extends RecyclerView.Adapter<PharmacistAdapter.ViewHolder>  {
    LayoutInflater inflater;
    List<User> users;
    PharmacistAdapter.PharmacistClickListener pharmacistClickListener;

    public PharmacistAdapter(Context context, List<User> users, PharmacistAdapter.PharmacistClickListener pharmacistClickListener)
    {
        this.inflater =  LayoutInflater.from(context);
        this.users = users;
        this.pharmacistClickListener = pharmacistClickListener;
    }


    public interface PharmacistClickListener
    {
        void selectedPharmacist(User user);
        void deletePharmacist(User user,int position);
    }



    @NonNull
    @Override
    public PharmacistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pharmacist_list_layout,parent,false);
        return new PharmacistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacistAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.pharmacistName.setText(users.get(position).getFirstName()+" "+users.get(position).getLastName());
        holder.pharmacistEmail.setText(users.get(position).getEmail());
        holder.pharmacistContact.setText(users.get(position).getContactNo());
        if(USER_ROLE.equalsIgnoreCase("pharmacist")){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }
        else{
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pharmacistClickListener.deletePharmacist(user,position);
            }
        });

        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pharmacistClickListener.selectedPharmacist(user);
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
        TextView pharmacistName,pharmacistEmail,pharmacistContact;
        ImageButton arrowBtn,deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacistName = itemView.findViewById(R.id.pharmacistName);
            pharmacistEmail = itemView.findViewById(R.id.pharmacistEmail);
            pharmacistContact = itemView.findViewById(R.id.pharmacistContact);
            arrowBtn = itemView.findViewById(R.id.pharmacist_btn_arrow);
            deleteBtn = itemView.findViewById(R.id.pharmacist_btn_delete);
        }
    }
}
