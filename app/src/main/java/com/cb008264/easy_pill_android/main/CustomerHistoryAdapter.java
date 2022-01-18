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

public class CustomerHistoryAdapter extends RecyclerView.Adapter<CustomerHistoryAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<CustomerOrder> orders;
    CustomerHistoryAdapter.CustomerHistoryClickListener customerHistoryClickListener;

    public CustomerHistoryAdapter(Context context, List<CustomerOrder> orders, CustomerHistoryAdapter.CustomerHistoryClickListener customerHistoryClickListener)
    {
        this.inflater =  LayoutInflater.from(context);
        this.orders = orders;
        this.customerHistoryClickListener = customerHistoryClickListener;
    }

    public interface CustomerHistoryClickListener
    {
        void selectedOrder(CustomerOrder order);
    }

    @NonNull
    @Override
    public CustomerHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.history_list_layout,parent,false);
        return new CustomerHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHistoryAdapter.ViewHolder holder, int position) {
        CustomerOrder order = orders.get(position);
        holder.orderId.setText(orders.get(position).getOrderId());
        holder.orderDate.setText(orders.get(position).getDate());
        holder.orderStatus.setText(orders.get(position).getStatus());
        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerHistoryClickListener.selectedOrder(order);
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderId,orderDate,orderStatus;
        ImageButton arrowBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            arrowBtn = itemView.findViewById(R.id.order_btn_arrow);
        }
    }
}
