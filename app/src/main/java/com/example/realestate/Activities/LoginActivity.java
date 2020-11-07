package com.example.realestate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realestate.Constants.Constants;
import com.example.realestate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button loginButton;
    private Button signUpButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LoginPage);
        setContentView(R.layout.activity_login);

        final ConstraintLayout loginButtonLayout = findViewById(R.id.loginButtonLayoutID);

        email = findViewById(R.id.loginEmailID);
        password = findViewById(R.id.loginPasswordID);
        loginButton = findViewById(R.id.loginLoginButtonID);
        signUpButton = findViewById(R.id.loginSignUpButtonID);

       loginButtonLayout.setVisibility(View.INVISIBLE);
        getSupportActionBar().hide();        // hide action bar

        // set status bar color same as the activity accent
//        Window window = getWindow();
//        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.loginStatusBarColor));


        // Initialise database and reference to database
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference(Constants.userDatabaseRef);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser!=null){
                    // SIGNED IN
                    String userId = mUser.getUid();
                    mDatabaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){       // Current user exists in registered user database--CUSTOMER
                                Toast.makeText(LoginActivity.this,"Signed In as User",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,CustomerDetailActivity.class));
                            }else{                          // Current User does'nt exist in registered user database--ADMIN
                                Toast.makeText(LoginActivity.this,"Signed In as Admin",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,DetailAdminActivity.class));
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this,"Not signed in",Toast.LENGTH_LONG).show();
                }
            }
        };

        Handler loginButtonDelayHandler = new Handler();
        loginButtonDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginButtonLayout.setVisibility(View.VISIBLE);
            }
        },1000);

        // Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email.getText().toString().trim())&&!TextUtils.isEmpty(password.getText().toString().trim())){
                    setLoginButton(email.getText().toString().trim(),password.getText().toString().trim());
                }else{
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // SignUp Button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

                finish();
            }
        });


    }

    protected void setLoginButton(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){     // If login was completed
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    final String userId = currentUser.getUid();
                    mDatabaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){       // Current user exists in registered user database--CUSTOMER
                                Toast.makeText(LoginActivity.this,"Signed In as User",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,CustomerDetailActivity.class));
                            }else{                          // Current User does'nt exist in registered user database--ADMIN
                                Toast.makeText(LoginActivity.this,"Signed In as Admin",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,DetailAdminActivity.class));
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{                     // If login was not completed
                    Toast.makeText(LoginActivity.this, "Could not login. Make sure you have entered valid details.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}