package com.app.profileapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.profileapplication.utilities.Parameters;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.app.profileapplication.MainActivity.JSON;

public class PaymentActivity extends AppCompatActivity {

    private Stripe stripe;
    private OkHttpClient client = new OkHttpClient();
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        token = getIntent().getExtras().getString(Parameters.TOKEN);
        PaymentConfiguration.init(getApplicationContext(), "pk_test_XEnZDjQFcIvAelNPKlRcaOJ100EcD66wp4");
        stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());

        JSONObject api = new JSONObject();
        try {
            api.put("api_version", "2019-09-09");
            post(Parameters.API_URL + "/user/client_token",api.toString() );
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void post(String url, String json) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", Parameters.BEARER + " " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                }
            }
        });
    }
}

