package com.example.realestate.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realestate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText emailText;
    private EditText contactNumberText;
    private EditText passwordText;
    private Button signUpButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LoginPage);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Registered Users");
        firebaseAuth = FirebaseAuth.getInstance();

        nameText = findViewById(R.id.registerNameID);
        emailText = findViewById(R.id.registerMailID);
        contactNumberText = findViewById(R.id.registerContactID);
        passwordText = findViewById(R.id.registerPassID);
        signUpButton = findViewById(R.id.registerSignupButtonID);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String contactNumber = contactNumberText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(contactNumber)&&!TextUtils.isEmpty(password)){
                    Log.d("ABCD","Clicked On SignUp Button and all fields filled");
                    createUser(name,email,contactNumber,password);
                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill all fields.", Toast.LENGTH_LONG).show();
                }
            }
        });



    }


    private void createUser(final String name, final String email, final String contactNumber, final String password){

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if(authResult!=null){

                    Log.d("ABCD","authResult is not null");

                    FirebaseUser user = authResult.getUser();

                    DatabaseReference userData = databaseReference.child(user.getUid());
                    userData.child("name").setValue(name);
                    userData.child("email").setValue(email);
                    userData.child("contact").setValue(contactNumber);
                    userData.child("password").setValue(password);
                    Toast.makeText(RegisterActivity.this,"SUCCESS",Toast.LENGTH_LONG).show();

                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    finish();

                }else{
                    Log.w("ABCD", "createUserWithEmail:failure");
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}