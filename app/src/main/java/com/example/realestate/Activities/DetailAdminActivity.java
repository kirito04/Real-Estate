package com.example.realestate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.realestate.Constants.Constants;
import com.example.realestate.Constants.Property;
import com.example.realestate.R;
import com.example.realestate.Utils.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Property> propertyList;
    private MyAdapter myAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_admin);

        getSupportActionBar().setTitle("Admin");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child(Constants.addedPropertiesRef);
        mDatabaseReference.keepSynced(true);

        propertyList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_signOut:
                Log.d("MENU DEBUG","SignOutClicked");
                if(mUser!=null&&mAuth!=null){
                    mAuth.signOut();
                    startActivity(new Intent(DetailAdminActivity.this,LoginActivity.class));
                    finish();
                }
                break;
            case R.id.action_detail:
                Log.d("MENU DEBUG","Details Clicked");
                startActivity(new Intent(this,DetailAdminActivity.class));
                finish();
                break;
            case R.id.action_AddProperty:
                Log.d("MENU DEBUG","Add Property Clicked");
                startActivity(new Intent(DetailAdminActivity.this,AddPropertyAdminActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();



        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d("DetAdmin","inside child event listener for database");
                //Log.d("DetAdmin", String.valueOf(dataSnapshot.getValue()));
                Property property = dataSnapshot.getValue(Property.class);


                Log.d("DetAdmin","Values assigned to property object");

                propertyList.add(property);

                Log.d("DetAdmin","object added to list");

                Collections.reverse(propertyList);
                myAdapter = new MyAdapter(DetailAdminActivity.this,propertyList);
                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.d("CustDet"," in On stop");
        propertyList.clear();
    }
}