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
import com.cb008264.easy_pill_android.model.Cart;
import com.cb008264.easy_pill_android.model.CustomerOrder;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<Cart> items;
    CartAdapter.CartClickListener cartClickListener;

    public CartAdapter(Context context, List<Cart> items, CartAdapter.CartClickListener cartClickListener)
    {
        this.inflater =  LayoutInflater.from(context);
        this.items = items;
        this.cartClickListener = cartClickListener;
    }

    public interface CartClickListener
    {
        void increaseQty(Cart cartItem,int position);
        void reduceQty(Cart cartItem,int position);
        void removeItem(Cart cartItem,int position);
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list_layout,parent,false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Cart cartItem = items.get(position);
        holder.itemName.setText(items.get(position).getProductName());
        holder.itemAmount.setText(items.get(position).getSubTotal());
        holder.itemQty.setText(String.valueOf(items.get(position).getQuantity()));
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartClickListener.increaseQty(cartItem,position);
            }
        });
        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartClickListener.reduceQty(cartItem,position);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartClickListener.removeItem(cartItem,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  items.size();
    }
    public void filterList(ArrayList<Cart> filteredList){
        items = filteredList;
        notifyDataSetChanged();
    }

    public void removeItemFromCart(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void updateItemInCart(int position,Cart cart) {
        items.get(position).setSubTotal(cart.getSubTotal());
        items.get(position).setQuantity(cart.getQuantity());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemAmount,itemQty;
        ImageButton addBtn,minusBtn,deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.productName_cart);
            itemAmount = itemView.findViewById(R.id.productTotal_cart);
            itemQty = itemView.findViewById(R.id.productQty_cart);
            addBtn = itemView.findViewById(R.id.btn_plus_cart);
            minusBtn = itemView.findViewById(R.id.btn_minus_cart);
            deleteBtn = itemView.findViewById(R.id.btn_delete_cart);
        }
    }
}
