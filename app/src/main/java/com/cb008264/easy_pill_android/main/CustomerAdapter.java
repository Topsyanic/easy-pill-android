package com.cb008264.easy_pill_android.main;

import android.app.Activity;
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

import static com.cb008264.easy_pill_android.main.AdminCustomersFragment.USER_ROLE;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>  {

    LayoutInflater inflater;
    List<User> users;
    CustomerClickListener customerClickListener;

    public CustomerAdapter(Context context, List<User> users, CustomerClickListener customerClickListener)
    {
        this.inflater =  LayoutInflater.from(context);
        this.users = users;
        this.customerClickListener = customerClickListener;
    }



    public interface CustomerClickListener
    {
        void selectedCustomer(User user);
        void deleteCustomer(User user,int position);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customers_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.customerName.setText(users.get(position).getFirstName()+" "+users.get(position).getLastName());
        holder.customerEmail.setText(users.get(position).getEmail());
        holder.customerContact.setText(users.get(position).getContactNo());
        if(USER_ROLE.equalsIgnoreCase("doctor")){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }
        else{
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerClickListener.deleteCustomer(user,position);
            }
        });

        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerClickListener.selectedCustomer(user);
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
        TextView customerName,customerEmail,customerContact;
        ImageButton arrowBtn,deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            customerEmail = itemView.findViewById(R.id.customerEmail);
            customerContact = itemView.findViewById(R.id.customerContact);
            arrowBtn = itemView.findViewById(R.id.btn_arrow);
            deleteBtn = itemView.findViewById(R.id.btn_delete);
        }
    }
}
