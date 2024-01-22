package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Boss {
    public Vector2 position;
    public Sprite sprite;
    private Texture img_enemy1;
    private Texture img_enemy2;
    private Texture img_enemy3;
    private Texture img_enemy4;
    public int HP;
    private int bossLevel;
    public boolean right;
    public float speed;
    private Texture healthBarTexture;
    private Sprite healthBarSprite;
    private float healthBarWidthScale;
    private int maxHealth;

    public Boss(int level) {
        img_enemy1 = new Texture("Enemy_1.png");
        img_enemy2 = new Texture("Enemy_2.png");
        img_enemy3 = new Texture("Enemy_3.png");
        img_enemy4 = new Texture("Enemy_4.png");
        bossLevel = level;
        bossType(level);
    }

    public void Update(float deltaTime) {
        if(right) position.x+=deltaTime*speed;
        if(!right) position.x-=deltaTime*speed;
        if(position.x <=0) right = true;
        if(position.x >=Gdx.graphics.getWidth() - sprite.getWidth()) right = false;

        if(HP==0) position.set(0, 10000);

        healthBarWidthScale = (float) HP / maxHealth;
        healthBarSprite.setPosition(position.x, position.y - healthBarSprite.getHeight());
    }

    public void Draw(SpriteBatch batch) {
        Update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
        healthBarSprite.draw(batch);
        healthBarSprite.setSize(sprite.getWidth() * healthBarWidthScale, healthBarSprite.getHeight());
    }

    public void detectHit(Laser laser) {
        if(laser.laserSprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
            if(bossLevel<=3) {
                HP -= 10;
            }
            else{
                HP -= 1;
            }
            laser.gone();
        }

    }

    public void detectHit(Missile missile) {
        if(missile.mSprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
            if(bossLevel<=3) {
                HP -= 25;
            }
            else{
                HP -= 1;
            }
            missile.gone();
        }

    }

    public void bossType(int level){
        if(level == 1){
            sprite = new Sprite(img_enemy1);
            sprite.setScale(1);
            sprite.setSize(img_enemy1.getWidth() * 2, img_enemy1.getHeight() * 2);
            position = new Vector2((Gdx.graphics.getWidth()-sprite.getWidth())/2, Gdx.graphics.getHeight() - sprite.getHeight());
            right = true;
            healthBarTexture = new Texture("health_bar.PNG"); // Create a simple texture for the health bar
            healthBarSprite = new Sprite(healthBarTexture);
            healthBarSprite.setSize(sprite.getWidth(), 10); // Set the size of the health bar
            HP = 100;
            maxHealth = HP;
            speed = 10;
        }

        else if(level == 2){
            sprite = new Sprite(img_enemy2);
            sprite.setScale(1);
            sprite.setSize(img_enemy2.getWidth() * 2, img_enemy2.getHeight() * 2);
            position = new Vector2((Gdx.graphics.getWidth()-sprite.getWidth())/2, Gdx.graphics.getHeight() - sprite.getHeight());
            right = true;
            healthBarTexture = new Texture("health_bar.PNG"); // Create a simple texture for the health bar
            healthBarSprite = new Sprite(healthBarTexture);
            healthBarSprite.setSize(sprite.getWidth(), 10); // Set the size of the health bar
            HP = 200;
            maxHealth = HP;
            speed = 250;
        }

        else if(level == 3){
            sprite = new Sprite(img_enemy3);
            sprite.setScale(1);
            sprite.setSize(img_enemy3.getWidth() * 2, img_enemy3.getHeight() * 2);
            position = new Vector2((Gdx.graphics.getWidth()-sprite.getWidth())/2, Gdx.graphics.getHeight() - sprite.getHeight());
            right = true;
            healthBarTexture = new Texture("health_bar.PNG"); // Create a simple texture for the health bar
            healthBarSprite = new Sprite(healthBarTexture);
            healthBarSprite.setSize(sprite.getWidth(), 10); // Set the size of the health bar
            HP = 300;
            maxHealth = HP;
            speed = 500;
        }

        else {
            sprite = new Sprite(img_enemy4);
            sprite.setScale(1);
            sprite.setSize(img_enemy4.getWidth() * 2, img_enemy4.getHeight() * 2);
            position = new Vector2((Gdx.graphics.getWidth()-sprite.getWidth())/2, Gdx.graphics.getHeight() - sprite.getHeight());
            right = true;
            healthBarTexture = new Texture("health_bar.PNG"); // Create a simple texture for the health bar
            healthBarSprite = new Sprite(healthBarTexture);
            healthBarSprite.setSize(sprite.getWidth(), 10); // Set the size of the health bar
            HP = 1000000000;
            maxHealth = HP;
            speed = 750;
        }
    }
}