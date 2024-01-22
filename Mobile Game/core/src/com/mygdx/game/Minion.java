package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Minion {
    public Vector2 position;
    public Sprite sprite;
    private Texture img_minion;
    public float speed = 800;
    public boolean gone;

    public Minion() {
        img_minion = new Texture("minion.png");
        sprite = new Sprite(img_minion);
        sprite.setScale(2);
        sprite.setSize(img_minion.getWidth() * 2, img_minion.getHeight() * 2);
        position = new Vector2((Gdx.graphics.getWidth()-sprite.getWidth())/2, Gdx.graphics.getHeight() - sprite.getHeight());
        gone = false;
    }

    public void Draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
        position.y -= Gdx.graphics.getDeltaTime()*speed;
    }

    public void spawnMinion() {
        float x = (float)((Math.random() * Gdx.graphics.getWidth()) + 0);
        position.set(x , Gdx.graphics.getHeight());
    }

    public void detectHit(Laser laser) {
        if(laser.laserSprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
            laser.gone();
        }
    }

    public void detectHit(Missile missile) {
        if(missile.mSprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
            missile.gone();
            sprite.setPosition(500, 1000);
            gone = true;
        }
    }

    public void hitShip(Spaceship ship) {
        if(ship.sprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
            ship.HP -= 1;
            ship.startFlickering();
            sprite.setPosition(500, 1000);
            gone = true;
        }
    }
}
