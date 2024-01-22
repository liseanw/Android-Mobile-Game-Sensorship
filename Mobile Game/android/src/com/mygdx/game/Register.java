package com.mygdx.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    TextInputEditText input_email, input_username, input_password;
    ImageButton reg_btn, start_btn;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    TextView login_here;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        // Getting the values for each input
        input_email = findViewById(R.id.email);
        input_username = findViewById(R.id.username);
        input_password = findViewById(R.id.password);
        reg_btn = findViewById(R.id.register_btn);
        login_here = findViewById(R.id.login_opt);
        start_btn = findViewById(R.id.startGameButton);

        // Checking if the button is clicked
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, username, password;

                // Obtaining the typed inputs
                email = String.valueOf(input_email.getText());
                username = String.valueOf(input_username.getText());
                password = String.valueOf(input_password.getText());

                // Checking if the inputs are empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Register.this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Code taken from firebase original documentation
                // https://firebase.google.com/docs/auth/android/password-auth#java_3
                myRef.child("users").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(Register.this, "Username is unavailable.", Toast.LENGTH_SHORT).show();
                        } else {
                            registerUser(email, password, username);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(Register.this, "Database error.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Checking if "login here!" is clicked
        login_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the button is clicked, it will bring the user to the login page
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                // Ends the current activity
                finish();
            }
        });

        // Checking if "X" is clicked
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }
    private void startGame() {
        // Launch your libGDX game activity
        Intent intent = new Intent(this, AndroidLauncher.class);
        startActivity(intent);
//        finish();

        // Finish the current activity
    }

    private void registerUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get the UID of the registered user
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                String uid = currentUser.getUid();

                                // Store the username in the Realtime Database under the user's UID
                                myRef.child("users").child(uid).child("username").setValue(username);
                                myRef.child("users").child(uid).child("email").setValue(email);
                                myRef.child("users").child(uid).child("highScore").setValue(0);


                                Toast.makeText(Register.this, "Account Successfully Created.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Register.this, "Account creation failed.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}