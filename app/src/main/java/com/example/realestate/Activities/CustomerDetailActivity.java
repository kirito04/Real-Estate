package com.example.realestate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.realestate.Constants.Constants;
import com.example.realestate.Constants.Property;
import com.example.realestate.R;
import com.example.realestate.Utils.CustAdapter;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText searchBar;
    private Spinner spinner;
    private String searchType = "Type";

    private RecyclerView recyclerView;
    private CustAdapter custAdapter;
    private List<Property> propertyList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRefUser;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        searchBar = findViewById(R.id.search_bar_id);
        spinner = findViewById(R.id.search_spinner_id);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_criteria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerViewCust);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        propertyList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference().child(Constants.addedPropertiesRef);
        mDatabaseRefUser = mDatabase.getReference().child(Constants.userDatabaseRef);
        mDatabaseRef.keepSynced(true);



        mDatabaseRefUser.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getSupportActionBar().setTitle(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterSearch(s.toString());
            }
        });

    }

    private void filterSearch(String text){
        ArrayList<Property> filterList = new ArrayList<>();
        if(searchType.equals("Type")){
            for(Property prop:propertyList){
                if(prop.getType().toLowerCase().contains(text.toLowerCase())){
                    filterList.add(prop);
                }
            }
        }else if(searchType.equals("Location")){
            for(Property prop:propertyList){
                if(prop.getLocation().toLowerCase().contains(text.toLowerCase())){
                    filterList.add(prop);
                }
            }
        }else if(searchType.equals("Status")){
            for(Property prop:propertyList){
                if(prop.getStatus().toLowerCase().contains(text.toLowerCase())){
                    filterList.add(prop);
                }
            }
        }else if(searchType.equals("Transaction")){

            for(Property prop:propertyList){
                if(prop.getTransaction().toLowerCase().contains(text.toLowerCase())){
                    filterList.add(prop);
                }
            }
        }else{
            for(Property prop:propertyList){
                if(prop.getType().toLowerCase().contains(text.toLowerCase())){
                    filterList.add(prop);
                }
            }
        }

        custAdapter.filter(filterList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_cust_signout:
                if(mUser!=null&&mAuth!=null){
                    mAuth.signOut();
                    startActivity(new Intent(CustomerDetailActivity.this,LoginActivity.class));
                    finish();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Property property = dataSnapshot.getValue(Property.class);

                propertyList.add(property);

                custAdapter = new CustAdapter(CustomerDetailActivity.this,propertyList);
                recyclerView.setAdapter(custAdapter);
                custAdapter.notifyDataSetChanged();


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
        Log.d("CustDet"," in On stop");
        propertyList.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchType = (String) parent.getItemAtPosition(position);
        searchBar.setHint("Search by "+searchType);
        Log.d("CustDet","Search type is "+searchType);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}