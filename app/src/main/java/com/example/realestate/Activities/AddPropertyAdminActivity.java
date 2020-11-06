package com.example.realestate.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.realestate.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPropertyAdminActivity extends AppCompatActivity {

    private ImageView propertyImage;
    private EditText type;
    private EditText location;
    private EditText area;
    private EditText status;
    private EditText transaction;
    private EditText price;
    private EditText contact;
    private Button postButton;

    private Uri imageUri;
    private static final int GALLERY_CODE = 1;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mPostDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_admin);

        propertyImage = findViewById(R.id.addPropImage);
        type = findViewById(R.id.addPropType);
        location = findViewById(R.id.addPropLocation);
        area = findViewById(R.id.addPropArea);
        status = findViewById(R.id.addPropStatus);
        transaction = findViewById(R.id.addPropTransaction);
        price = findViewById(R.id.addPropPrice);
        contact = findViewById(R.id.addPropContact);
        postButton = findViewById(R.id.addPropPostButton);

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("AddedProperties");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();


        propertyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });


    }

    private void startPosting(){
        final String typePost = type.getText().toString().trim();
        final String locationPost = location.getText().toString().trim();
        final String areaPost = area.getText().toString().trim();
        final String statusPost = status.getText().toString().trim();
        final String transactionPost = transaction.getText().toString().trim();
        final String pricePost = price.getText().toString().trim();
        final String contactPost = contact.getText().toString().trim();
        Log.d("AddProp","Inside start posting fn.");
        if(typePost!=null&&locationPost!=null&&areaPost!=null&&statusPost!=null&&transactionPost!=null&&pricePost!=null&&contactPost!=null&&imageUri!=null){
            StorageReference filePath = mStorage.child("Property_images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Log.d("AddProp","Image uploaded successfully");

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    DatabaseReference newPost = mPostDatabase.push();

                    Map<String,String> map = new HashMap<>();
                    map.put("area",areaPost);
                    map.put("type",typePost);
                    map.put("price",pricePost);
                    map.put("location",locationPost);
                    map.put("status",statusPost);
                    map.put("transaction",transactionPost);
                    map.put("contact",contactPost);
                    map.put("image",downloadUrl.toString());

                    Log.d("AddProp","map prepared");

                    newPost.setValue(map);

                    Log.d("AddProp","details posted to database");

                    startActivity(new Intent(AddPropertyAdminActivity.this,DetailAdminActivity.class));
                }
            });
        }else{
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            imageUri = data.getData();
            propertyImage.setImageURI(imageUri);
        }
    }
}