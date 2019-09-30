package com.app.profileapplication.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    Context context;
    ArrayList<Items> items;

    public ItemsAdapter(Context context, ArrayList<Items> items){
        this.context = context;
        this.items= items;
    }
    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_items_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {

        holder.itemName.setText(items.get(position).getItemName());
        holder.price.setText(String.valueOf(items.get(position).getPrice()));
        String r =items.get(position).getImage();
        int id= context.getResources().getIdentifier(r, "drawable", context.getPackageName());
//        holder.itemImage.setImageResource(id);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, price;
        ImageView itemImage;
        Button addToCart;

        ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.fragment_items_item_name);
            price = itemView.findViewById(R.id.fragment_items_item_price);
            addToCart = itemView.findViewById(R.id.fragment_items_item_add_to_cart);
            itemImage = itemView.findViewById(R.id.fragment_items_item_image);
        }
    }
}
