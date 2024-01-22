package com.mygdx.game;

import java.util.Map;

public interface FirebaseInterface {

    public String getAuthUserId();

    public void sendScore(int score);

    public void getLeaderboardData(DataCallback<Map<String, Integer>> callback);
}
