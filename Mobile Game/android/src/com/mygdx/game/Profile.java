package com.mygdx.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    FirebaseAuth auth;
    ImageButton logout_btn, start_btn;
    TextView show_email;
    FirebaseUser user;
    public static DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_profile);



        auth = FirebaseAuth.getInstance();

        // Initialise
        logout_btn = findViewById(R.id.logout);
        start_btn = findViewById(R.id.startGameButton);
        show_email = findViewById(R.id.details);
        user = auth.getCurrentUser();

        // If the user is not logged in, bring up the login page
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        // If the user is logged in, show the email
        else {
            show_email.setText(user.getEmail());
        }

        // Checked if the user clicked the logout button
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

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

    private void initialFireBase(String referenceName){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(referenceName);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String inputText = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}