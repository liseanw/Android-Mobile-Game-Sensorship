package com.mygdx.game;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidFirebaseInterface implements FirebaseInterface{

    private FirebaseAuth mAuth;
    private static DatabaseReference myRef;

    @Override
    public String getAuthUserId() {
        FirebaseUser currentUser = Login.mAuth.getCurrentUser();
        return currentUser.getUid();
    }

    @Override
    public void sendScore(int score) {
        // if user already has an account and register
        if (Profile.myRef == null) {
            myRef = FirebaseDatabase.getInstance().getReference();
        }

        if (Login.mAuth != null) {
//            Log.d("score sent", String.valueOf(score));
//            Log.d("uid", String.valueOf(score));
            FirebaseUser currentUser = Login.mAuth.getCurrentUser();
            if(currentUser != null) {
                String uid = currentUser.getUid();
                final DatabaseReference scoreRef = myRef.child("users").child(uid).child("highScore");


                scoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Integer currentHighScore = dataSnapshot.getValue(Integer.class);
                            if (score > currentHighScore) {
                                // Update the "highScore" if the new score is higher
                                scoreRef.setValue(score);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occurred.
                        Log.e("Database Read Error", "Error: " + databaseError.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void getLeaderboardData(DataCallback<Map<String, Integer>> callback) {
        myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = myRef.child("users");
        final Map<String, Integer> leaderboardData = new HashMap<>();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // read data from database and store in leaderboardData
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    int highScore = userSnapshot.child("highScore").getValue(Integer.class);
                    leaderboardData.put(username, highScore);
                }
                callback.onDataReceived(leaderboardData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }


}
