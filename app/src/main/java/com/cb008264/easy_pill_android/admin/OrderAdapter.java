package com.cb008264.easy_pill_android.admin;

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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<CustomerOrder> orders;
    OrderAdapter.OrderClickListener orderClickListener;

    public OrderAdapter(Context context, List<CustomerOrder> orders, OrderAdapter.OrderClickListener orderClickListener)
    {
        this.inflater =  LayoutInflater.from(context);
        this.orders = orders;
        this.orderClickListener = orderClickListener;
    }

    public interface OrderClickListener
    {
        void selectedOrder(CustomerOrder order);
        void confirmOrder(CustomerOrder order,int position);
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_list_layout,parent,false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        CustomerOrder order = orders.get(position);
        holder.orderId.setText(orders.get(position).getOrderId());
        holder.orderDate.setText(orders.get(position).getDate());
        holder.orderStatus.setText(orders.get(position).getStatus());
        if(order.getStatus().equalsIgnoreCase("Pending"))
        {
            holder.confirmBtn.setVisibility(View.VISIBLE);
        }
        else{
            holder.confirmBtn.setVisibility(View.INVISIBLE);
        }
        holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.confirmOrder(order,position);
            }
        });
        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.selectedOrder(order);
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

    public void updateItem(int position) {
        CustomerOrder order = orders.get(position);
        order.setStatus("Confirmed");
        orders.set(position,order);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderId,orderDate,orderStatus;
        ImageButton arrowBtn,confirmBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            arrowBtn = itemView.findViewById(R.id.order_btn_arrow);
            confirmBtn = itemView.findViewById(R.id.order_btn_confirm);
        }
    }
}
