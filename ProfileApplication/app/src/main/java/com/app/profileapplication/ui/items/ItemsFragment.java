package com.app.profileapplication.ui.items;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.profileapplication.R;
import com.app.profileapplication.adapters.ItemsAdapter;
import com.app.profileapplication.models.Items;
import com.app.profileapplication.ui.cart.CartFragment;
import com.app.profileapplication.ui.edit.EditFragment;
import com.app.profileapplication.utilities.Parameters;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment implements ItemsAdapter.ItemsCartInterface {

    RecyclerView recyclerView;
    ArrayList<Items> itemsAdded = new ArrayList<>();
    Button checkoutButton;

    ItemsAdapter itemsAdapter;
    public ItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        String responseString = getData(Parameters.API_URL+"/item/getitems", view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        checkoutButton = view.findViewById(R.id.fragment_items_checkout);
        checkoutButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Parameters.ITEM_LIST, itemsAdded);

            CartFragment fragment = new CartFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment).addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }

//    class GetData extends AsyncTask<String, Void, String> {
//
//        OkHttpClient client = new OkHttpClient();

    public String getData(String url, View view){
        OkHttpClient client = new OkHttpClient();
        recyclerView = view.findViewById(R.id.fragment_items_recyclerview);
        ArrayList<Items> itemsArrayList = new ArrayList();
        final String[] responseString = new String[1];

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                responseString[0] = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(responseString[0]);
                    Iterator<String> keys = jsonObject.keys();

                    while(keys.hasNext()){

                        String key= keys.next();
                        JSONObject single_item = jsonObject.getJSONObject(key);
                        Items items1 = new Items(
                                single_item.getString(Parameters.ITEMS_ITEM_NAME),
                                single_item.getString(Parameters.ITEMS_ITEM_REGION),
                                single_item.getString(Parameters.ITEMS_ITEM_ID),
                                single_item.getDouble(Parameters.ITEMS_ITEM_DISCOUNT),
                                single_item.getDouble(Parameters.ITEMS_ITEM_PRICE),
                                single_item.getString(Parameters.ITEMS_ITEM_PHOTO)
                        );
                        itemsArrayList.add(items1);

                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemsAdapter = new ItemsAdapter(getContext(), itemsArrayList, ItemsFragment.this::addToCart);
                            recyclerView.setAdapter(itemsAdapter);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return responseString[0];

    }

    @Override
    public void addToCart(Items items) {
        itemsAdded.add(items);
        for (Items i: itemsAdded)
            Log.d("Item", i.getItemName());

    }
}

