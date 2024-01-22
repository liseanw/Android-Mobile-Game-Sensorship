package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Laser {
	private Texture img_laser;
	public Vector2 laserPosition;
	public Sprite laserSprite;
	public float laserSpeed = 600;
	public boolean gone;
	
	public Laser() {
		img_laser = new Texture("laser.png");
		laserSprite = new Sprite(img_laser);
		laserSprite.setScale(1);
		laserPosition = new Vector2(0, 1000);
		gone = false;
	}
	
	
	public void Draw(SpriteBatch batch) {
		if(!gone) {
			laserSprite.setPosition(laserPosition.x, laserPosition.y);
			laserSprite.draw(batch);
			laserPosition.y += Gdx.graphics.getDeltaTime() * laserSpeed;
		}
	}


	public void gone() {
		laserSprite.setPosition(0, 1000);
		gone = true;
	}
	
}
