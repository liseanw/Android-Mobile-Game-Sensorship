package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Life {
    public Vector2 position;
    public Sprite sprite;
    private Texture img_up;
    public float speed = 300;
    public boolean gone;

    public Life() {
        img_up = new Texture("up.png");
        sprite = new Sprite(img_up);
        sprite.setScale(2);
        sprite.setSize(img_up.getWidth() * 2, img_up.getHeight() * 2);
        position = new Vector2((Gdx.graphics.getWidth()-sprite.getWidth())/2, Gdx.graphics.getHeight() - sprite.getHeight());
        gone = true;
    }

    public void Draw(SpriteBatch batch) {
        if(!gone) {
            sprite.setPosition(position.x, position.y);
            sprite.draw(batch);
            position.y -= Gdx.graphics.getDeltaTime() * speed;
            if (position.y <= 0) {
                sprite.setPosition(500, 1000);
                gone = true;
            }
        }
    }

    public void spawnLife() {
        float x = (float)((Math.random() * Gdx.graphics.getWidth()) + 0);
        position.set(x , Gdx.graphics.getHeight());
        gone = false;
    }

    public void hitShip(Spaceship ship) {
        if(ship.sprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
            if(ship.HP <= 5) {
                ship.HP += 1;
                sprite.setPosition(500, 1000);
                gone = true;
            }
        }
    }
}
