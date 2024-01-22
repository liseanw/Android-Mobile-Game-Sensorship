package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Censor {
    private Vector2 topLeft;
    private Vector2 bottomRight;
    private Texture censorTexture;
    private float duration = 0.1f; // Duration in seconds
    private float timer; // Timer to keep track of elapsed time
    private int shouldDraw; // Flag to control drawing

    public Censor(Vector2 topLeft, Vector2 bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;

        this.timer = 0;
        this.shouldDraw = 0;
        createCensorTexture();
    }

    private void createCensorTexture() {
        int width = (int) (bottomRight.x - topLeft.x);
        int height = (int) (bottomRight.y - topLeft.y);

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        censorTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void addTime(){
        this.shouldDraw++;
    }

    public void draw(SpriteBatch batch) {
        if (shouldDraw < 800) {
            batch.draw(censorTexture, topLeft.x, topLeft.y);
        }
    }
}
