package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.screen.SettingScreen;

public class Spaceship {
	public static Vector2 position;
	private Texture img;
	public static Sprite sprite;
	public ArrayList<Missile> missiles;
	public ArrayList<Laser> lasers;
	public static float speed = 0;
	public int HP = 5;

	private float cooldown = 0.2f; // Set the initial cooldown time in seconds
	private float timeSinceLastShot = 0;

	private boolean isFlickering = false;
	private float flickerTime = 0;
	private float flickerDuration = 0.8f; // 1 second of flickering
	private float flickerInterval = 0.15f; // 0.1 seconds on/off intervals

	private float touchTime = 0;


	public Spaceship() {
		img = new Texture("spaceship.png");
		sprite = new Sprite(img);
		sprite.setScale(1);
		sprite.setSize(img.getWidth() * 3, img.getHeight() * 3);
		lasers = new ArrayList<>();
		missiles = new ArrayList<>();
		position = new Vector2((Gdx.graphics.getWidth()-sprite.getWidth())/2, 50);

	}

	public void Update(float deltaTime) {
		// Gyroscope control
		if (SettingScreen.getCurControlMode() == ControlMode.GYROSCOPE_MODE) {
			position.x -= deltaTime * speed;
		}

		timeSinceLastShot += deltaTime; // Increase the time since last shot

		// Touch screen control
		if(Gdx.input.isTouched() && SettingScreen.getCurControlMode() == ControlMode.TOUCH_MODE) {
			float touchX = Gdx.input.getX();

			// Check if the touch is on the left or right side of the screen
			if (touchX < Gdx.graphics.getWidth() / 2) {
				position.x -= deltaTime * 300;
			} else {
				position.x += deltaTime * 300;
			}
		}

		// Tap Attack
		if(Gdx.input.isTouched() && SettingScreen.getCurAttackMode() == AttackMode.TAP_MODE) {
			touchTime += deltaTime;
			// Fire laser
			if (Gdx.input.justTouched() && timeSinceLastShot >= cooldown) {
				Laser laser = new Laser();
				lasers.add(laser);
				float x = position.x + sprite.getWidth() / 2 - 4;
				float y = sprite.getHeight() - 10;
				laser.laserPosition.set(x, y);
				timeSinceLastShot = 0;
				touchTime = 0;
			}

			if (Gdx.input.isTouched() && touchTime > 1.5) {
				Missile missile = new Missile();
				missiles.add(missile);
				float x = position.x + sprite.getWidth() / 2 - 4;
				float y = sprite.getHeight() - 10;
				missile.mPosition.set(x, y);
				timeSinceLastShot = 0;
				touchTime = 0;
			}
		}

		if (isFlickering) {
			flickerTime += deltaTime;
			// Alternate visibility
			sprite.setAlpha((flickerTime % (flickerInterval * 2)) < flickerInterval ? 0 : 1);
			// Stop flickering after the duration ends
			if (flickerTime >= flickerDuration) {
				isFlickering = false;
				sprite.setAlpha(1); // Make sure the sprite is visible after flickering
			}
		}

		// within screen
		if (position.x <= 0) position.x = 0;
		if (position.x >= Gdx.graphics.getWidth() - sprite.getWidth()) position.x = Gdx.graphics.getWidth() - sprite.getWidth();
	}

	public void startFlickering() {
		isFlickering = true;
		flickerTime = 0; // Reset the flicker time
	}
	
	public ArrayList<Laser> DrawL(SpriteBatch batch) {
		Update(Gdx.graphics.getDeltaTime());
		sprite.setPosition(position.x, position.y);
		sprite.draw(batch);
		for(Laser laser: lasers) {
			if(!laser.gone){
				laser.Draw(batch);
				if(laser.laserPosition.y>Gdx.graphics.getHeight()+1) {
					laser.gone();
				}
			}
		}

		return lasers;
	}

	public ArrayList<Missile> DrawM(SpriteBatch batch) {
		Update(Gdx.graphics.getDeltaTime());
		sprite.setPosition(position.x, position.y);
		sprite.draw(batch);
		for(Missile missile: missiles) {
			if(!missile.gone){
				missile.Draw(batch);
				if(missile.mPosition.y>Gdx.graphics.getHeight()+1) {
					missile.gone();
				}
			}
		}

		return missiles;
	}

	public int getHP() {
		return HP;
	}

	public void shoot() {
		if (timeSinceLastShot>=cooldown) {
			Laser laser = new Laser();
			lasers.add(laser);
			float x = position.x + sprite.getWidth() / 2 - 4;
			float y = sprite.getHeight() - 15;
			laser.laserPosition.set(x, y);
			timeSinceLastShot = 0;
		}
	}

	public void shootMissile() {
		if (timeSinceLastShot>=1.2f) {
			Missile missile = new Missile();
			missiles.add(missile);
			float x = position.x + sprite.getWidth() / 2 - 4;
			float y = sprite.getHeight() - 15;
			missile.mPosition.set(x, y);
			timeSinceLastShot = 0;
		}
	}

	public static void move(double degree) {
		if (degree < 0) {
			speed = -(float) Math.pow(degree, 2);
		} else {
			speed = (float) Math.pow(degree, 2);
		}
	}

	public static void setPosition(float x, float y) {
		if (x != 0) {
			position.x = x * Gdx.graphics.getWidth() - sprite.getWidth()/2;
//			position.y = (1 - y) * Gdx.graphics.getWidth();
		}

	}

	public static Vector2 getPosition() {
		return position;
	}
}
