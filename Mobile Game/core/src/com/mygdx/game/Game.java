package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screen.*;

import java.awt.Menu;

public class Game extends com.badlogic.gdx.Game {
	public SpriteBatch batch;
	public MenuScreen menuScreen;
	private FirebaseInterface firebaseInterface;

	public Game(FirebaseInterface firebaseInterface) {
		this.firebaseInterface = firebaseInterface;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		menuScreen = new MenuScreen(this);
		this.setScreen(menuScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public FirebaseInterface getFirebaseInterface() {
		return firebaseInterface;
	}

	public void setFirebaseInterface(FirebaseInterface firebaseInterface) {
		this.firebaseInterface = firebaseInterface;
	}
}
