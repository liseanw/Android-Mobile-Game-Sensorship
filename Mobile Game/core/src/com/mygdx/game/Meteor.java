package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Meteor {
	public Vector2 position;
	public Sprite sprite;
	private Texture img_meteor;
	public float speed;
	public boolean gone;
	private Animation<TextureRegion> explosionAnimation;
	private float explosionTimer;
	private boolean exploding;
	private boolean exploded = false;
	
	public Meteor(int level) {
		img_meteor = new Texture("meteor.png");
		sprite = new Sprite(img_meteor);
		sprite.setScale(2);
		sprite.setSize(img_meteor.getWidth() * 2, img_meteor.getHeight() * 2);
		position = new Vector2((Gdx.graphics.getWidth()-sprite.getWidth())/2, Gdx.graphics.getHeight() - sprite.getHeight());
		gone = false;
		if(level == 1) {
			speed = 400;
		}
		else if(level == 2) {
			speed = 700;
		}
		else{
			speed = 800;
		}
	}
	
	public void Draw(SpriteBatch batch) {

		if (exploding) {
			// Draw the explosion animation
			TextureRegion[] explosionFrames = new TextureRegion[2];
			for (int i = 0; i < 2; i++) {
				Texture texture = new Texture(Gdx.files.internal("Animation/meterExplosion_frame_" + i + ".png"));
				explosionFrames[i] = new TextureRegion(texture);

			}

			explosionAnimation = new Animation<TextureRegion>(0.1f, explosionFrames);
			TextureRegion currentFrame = explosionAnimation.getKeyFrame(explosionTimer);
			batch.draw(currentFrame, position.x-2, position.y-2, currentFrame.getRegionWidth() * 0.3f, currentFrame.getRegionHeight() * 0.3f);
			explosionTimer += Gdx.graphics.getDeltaTime();
			if (explosionAnimation.isAnimationFinished(explosionTimer)) {
				gone = true; // Mark the meteor as gone after the explosion finishes
			}
			exploded = true;
		} else {
			sprite.setPosition(position.x, position.y);
			sprite.draw(batch);
			position.y -= Gdx.graphics.getDeltaTime() * speed;
		}
	}
	
	public void spawnMeteor() {
		float x = (float)((Math.random() * Gdx.graphics.getWidth()) + 0);
		position.set(x , Gdx.graphics.getHeight());
	}

	public void detectHit(Laser laser) {
		if(laser.laserSprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
			laser.gone();
			if (!exploded) {
				startExplosion();
			}

		}
	}

	public void detectHit(Missile missile) {
		if(missile.mSprite.getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
			sprite.setPosition(500, 1000);
			gone = true;
			if (!exploded) {
				startExplosion();
			}

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

	private void startExplosion() {
		exploding = true;
		explosionTimer = 0;
	}
}

