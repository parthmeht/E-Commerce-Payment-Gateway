package com.app.profileapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.profileapplication.R;
import com.app.profileapplication.models.Items;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    Context context;
    ArrayList<Items> items;
    public CartAdapter(Context context, ArrayList<Items> items){
        this.context = context;
        this.items= items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_cart_item, parent, false);
        ViewHolder viewHolder = new CartAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        holder.itemName.setText(items.get(position).getItemName());
        Log.d("ITEMSNAME", items.get(position).getItemName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, price;
        ImageView itemImage;
        Button remove;

        ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.fragment_cart_item_name);
            price = itemView.findViewById(R.id.fragment_cart_item_price);
            remove = itemView.findViewById(R.id.fragment_cart_delete_button);
            itemImage = itemView.findViewById(R.id.fragment_cart_item_image);
        }
    }
}
