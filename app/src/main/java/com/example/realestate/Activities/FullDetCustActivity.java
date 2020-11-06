package com.example.realestate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.R;
import com.squareup.picasso.Picasso;

public class FullDetCustActivity extends AppCompatActivity {

    private ImageView image;
    private TextView type;
    private TextView location;
    private TextView area;
    private TextView status;
    private TextView transaction;
    private TextView price;
    private TextView contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_det_cust);

        getSupportActionBar().hide();

        Intent extraIntent = getIntent();


        image = findViewById(R.id.full_det_image);
        type= findViewById(R.id.full_det_type);
        location = findViewById(R.id.full_det_location);
        area = findViewById(R.id.full_det_area);
        status = findViewById(R.id.full_det_status);
        transaction = findViewById(R.id.full_det_transaction);
        price = findViewById(R.id.full_det_price);
        contact= findViewById(R.id.full_det_contact);

        type.setText(extraIntent.getStringExtra("Type"));
        location.setText(extraIntent.getStringExtra("Location"));
        area.setText(extraIntent.getStringExtra("Area")+" sqft.");
        status.setText(extraIntent.getStringExtra("Status"));
        transaction.setText(extraIntent.getStringExtra("Transaction"));
        price.setText(extraIntent.getStringExtra("Price"));
        contact.setText(extraIntent.getStringExtra("Contact"));

        String imageURL = extraIntent.getStringExtra("Image");
        Picasso.get().load(imageURL).into(image);







    }
}