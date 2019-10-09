package com.app.profileapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.app.profileapplication.models.User;
import com.app.profileapplication.utilities.Parameters;

public class InvoiceActivity extends AppCompatActivity {

    private TextView url;
    private Button gotoHomePage;
    private String token, invoiceUrl;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        setTitle("Invoice");
        token = getIntent().getExtras().getString(Parameters.TOKEN);
        user = (User) getIntent().getExtras().getSerializable(Parameters.USER_ID);
        invoiceUrl = getIntent().getExtras().getString("url");
        url = findViewById(R.id.invoice_url);
        gotoHomePage = findViewById(R.id.gotoHomePage);
        url.setText(invoiceUrl);
        gotoHomePage.setOnClickListener(view -> {
            Intent intent = new Intent(InvoiceActivity.this, HomeActivity.class);
            intent.putExtra(Parameters.TOKEN, token);
            startActivity(intent);
            finish();
        });
    }
}
