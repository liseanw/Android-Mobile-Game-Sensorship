package com.mygdx.game;

public interface DataCallback<T> {
    void onDataReceived(T data);
    void onError(Exception e);
}

