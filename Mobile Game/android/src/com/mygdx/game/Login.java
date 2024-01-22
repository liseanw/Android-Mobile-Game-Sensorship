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

public class Login extends AppCompatActivity {

    TextInputEditText input_email, input_password;
    ImageButton login_btn, start_btn;
    public static FirebaseAuth mAuth;
    TextView register_here;

    // Code taken from firebase original documentation
    // https://firebase.google.com/docs/auth/android/password-auth#java_3
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Getting the values for each input
        input_email = findViewById(R.id.email);
        input_password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        register_here = findViewById(R.id.register_opt);
        start_btn = findViewById(R.id.startGameButton);

        // Checking if the "Login" button is clicked
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;

                // Obtaining the typed inputs
                email = String.valueOf(input_email.getText());
                password = String.valueOf(input_password.getText());

                // Checking for network
                if (!NetworkUtils.isNetworkAvailable(Login.this)) {
                    Toast.makeText(Login.this, "No internet connection available.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Checking if the inputs are empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Email cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Password cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                // Code taken from firebase original documentation
                // https://firebase.google.com/docs/auth/android/password-auth#java_3
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "Login Success.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Login Failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        // Checking if "Register here!" is clicked
        register_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking for network
                if (!NetworkUtils.isNetworkAvailable(Login.this)) {
                    Toast.makeText(Login.this, "No internet connection available.", Toast.LENGTH_LONG).show();
                    return;
                }
                
                // If the button is clicked, it will bring the user to the registration page
                Intent intent = new Intent(getApplicationContext(), Register.class);
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


}