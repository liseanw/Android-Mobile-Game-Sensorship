package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Missile {

    private Texture img_missile;
    public Vector2 mPosition;
    public Sprite mSprite;
    public float mSpeed = 1000;
    public boolean gone;

    public Missile() {
        img_missile = new Texture("missile.png");
        mSprite = new Sprite(img_missile);
        mSprite.setScale(1);
        mPosition = new Vector2(0, 1000);
        gone = false;
    }


    public void Draw(SpriteBatch batch) {
        mSprite.setPosition(mPosition.x, mPosition.y);
        mSprite.draw(batch);
        mPosition.y += Gdx.graphics.getDeltaTime()*mSpeed;
    }


    public void gone() {
        mSprite.setPosition(0, 1000);
        gone = true;
    }


}
