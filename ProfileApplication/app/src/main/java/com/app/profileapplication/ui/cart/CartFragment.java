package com.app.profileapplication.ui.cart;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.profileapplication.MainActivity;
import com.app.profileapplication.R;
import com.app.profileapplication.adapters.CartAdapter;
import com.app.profileapplication.models.Items;
import com.app.profileapplication.utilities.Parameters;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private View view;
    private String TAG = "CartFragment";
    private int DROP_IN_REQUEST = 100;
    private Button makePayment;
    private BraintreeFragment mBraintreeFragment;
    private OkHttpClient client = new OkHttpClient();
    private SharedPreferences preferences;
    private String client_token, token;
    private ArrayList<Items> itemsArrayList;
    private RecyclerView recyclerView;

    private CartAdapter cartAdapter;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        itemsArrayList = (ArrayList<Items>) getArguments().getSerializable(Parameters.ITEM_LIST);
        Log.d("ITEMSARRAYLIST123", String.valueOf(itemsArrayList.size()));
        recyclerView = view.findViewById(R.id.fragment_cart_recyclerView);
        cartAdapter = new CartAdapter(getContext(), itemsArrayList);
        recyclerView.setAdapter(cartAdapter);

        makePayment = view.findViewById(R.id.cart_makePaymentButton);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString(Parameters.TOKEN, "");

        getClientToken(Parameters.API_URL + "/user/client_token");
        makePayment.setOnClickListener(view -> {

            DropInRequest dropInRequest = new DropInRequest()
                    .clientToken(client_token);
            startActivityForResult(dropInRequest.getIntent(getContext()), DROP_IN_REQUEST);
        });

        return view;
    }

    public void getClientToken(String url) {
        if (token != null) {
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", Parameters.BEARER + " " + token)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        String responseString = responseBody.string();
                        try {
                            JSONObject json = new JSONObject(responseString);
                            client_token = (String) json.get(Parameters.CLIENT_TOKEN);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

        } else {
            logout();
        }
    }

    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DROP_IN_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                Log.d(TAG, paymentMethodNonce);
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }
}
