package com.example.realestate.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.Constants.Property;
import com.example.realestate.R;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class CustAdapter extends RecyclerView.Adapter<CustAdapter.ViewHolder> {

    private Context context;
    private List<Property> propertyList;

    public CustAdapter(Context context, List<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_card_cust,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Property property = propertyList.get(position);
        String imageURL = null;

        holder.type.setText("Type: "+property.getType());
        holder.location.setText("Location: "+property.getLocation());
        holder.area.setText("Area:\n"+property.getArea());
        holder.status.setText("Status:\n"+property.getStatus());
        holder.transaction.setText("Transaction:\n"+property.getTransaction());
        holder.price.setText("Price:\nRs. "+property.getPrice());
        holder.contact.setText("Contact Number: "+property.getContact());

        imageURL = property.getImage();
        Picasso.get().load(imageURL).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView type;
        public TextView location;
        public TextView area;
        public TextView status;
        public TextView transaction;
        public TextView price;
        public TextView contact;
        private Button share;
        private Button call;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);

            context = ctx;

            image = itemView.findViewById(R.id.detail_card_cust_image);
            type = itemView.findViewById(R.id.detail_card_cust_type);
            location = itemView.findViewById(R.id.detail_card_cust_location);
            area = itemView.findViewById(R.id.detail_card_cust_area);
            status = itemView.findViewById(R.id.detail_card_cust_status);
            transaction = itemView.findViewById(R.id.detail_card_cust_transaction);
            price = itemView.findViewById(R.id.detail_card_cust_price);
            contact = itemView.findViewById(R.id.detail_card_cust_contact);
            share = itemView.findViewById(R.id.detail_card_cust_share);
            call = itemView.findViewById(R.id.detail_card_cust_call);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(type.getText().toString()+"\n");
                    stringBuilder.append(location.getText().toString()+"\n");
                    stringBuilder.append(area.getText().toString()+"\n");
                    stringBuilder.append(status.getText().toString()+"\n");
                    stringBuilder.append(transaction.getText().toString()+"\n");
                    stringBuilder.append(price.getText().toString()+"\n");
                    stringBuilder.append(contact.getText().toString()+"\n");

                    Intent shareIntent = new Intent();
                    shareIntent.putExtra(Intent.EXTRA_TEXT,stringBuilder.toString());
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");

                    context.startActivity(shareIntent);

                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<String> list = Arrays.asList(contact.getText().toString().split(" "));

                    Intent callIntent = new Intent();
                    callIntent.setAction(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+list.get(2)));
                    context.startActivity(callIntent);
                }
            });

        }
    }
}
