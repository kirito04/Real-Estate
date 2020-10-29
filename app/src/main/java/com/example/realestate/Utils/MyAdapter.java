package com.example.realestate.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.Constants.Property;
import com.example.realestate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private Context context;
    private List<Property> propertyList;

    public MyAdapter(Context context, List<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_card,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

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


        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);


            context = ctx;

            image = itemView.findViewById(R.id.detail_card_image);
            type = itemView.findViewById(R.id.detail_card_type);
            location = itemView.findViewById(R.id.detail_card_location);
            area = itemView.findViewById(R.id.detail_card_area);
            status = itemView.findViewById(R.id.detail_card_status);
            transaction = itemView.findViewById(R.id.detail_card_transaction);
            price = itemView.findViewById(R.id.detail_card_price);
            contact = itemView.findViewById(R.id.detail_card_contact);

        }
    }
}
