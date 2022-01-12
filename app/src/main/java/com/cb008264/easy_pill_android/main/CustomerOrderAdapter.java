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
import com.cb008264.easy_pill_android.model.CustomerOrder;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<CustomerOrder> orders;
    CustomerOrderAdapter.CustomerOrderClickListener customerOrderClickListener;

    public CustomerOrderAdapter(Context context, List<CustomerOrder> orders, CustomerOrderAdapter.CustomerOrderClickListener customerOrderClickListener)
    {
        this.inflater =  LayoutInflater.from(context);
        this.orders = orders;
        this.customerOrderClickListener = customerOrderClickListener;
    }

    public interface CustomerOrderClickListener
    {
        void selectedOrder(CustomerOrder order);
        void cancelOrder(CustomerOrder order,int position);
    }

    @NonNull
    @Override
    public CustomerOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customer_order_list_layout,parent,false);
        return new CustomerOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrderAdapter.ViewHolder holder, int position) {
        CustomerOrder order = orders.get(position);
        holder.orderId.setText(orders.get(position).getOrderId());
        holder.orderDate.setText(orders.get(position).getDate());
        holder.orderStatus.setText(orders.get(position).getStatus());
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerOrderClickListener.cancelOrder(order,position);
            }
        });
        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerOrderClickListener.selectedOrder(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  orders.size();
    }
    public void filterList(ArrayList<CustomerOrder> filteredList){
        orders = filteredList;
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        orders.remove(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderId,orderDate,orderStatus;
        ImageButton arrowBtn,cancelBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.customer_orderId);
            orderDate = itemView.findViewById(R.id.customer_orderDate);
            orderStatus = itemView.findViewById(R.id.customer_orderStatus);
            arrowBtn = itemView.findViewById(R.id.customer_order_btn_arrow);
            cancelBtn = itemView.findViewById(R.id.customer_order_btn_cancel);
        }
    }
}
