package com.mygdx.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Game;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Items.CustomisedButtonStyle;


public class MenuScreen implements Screen {

    Game game;
//    Texture playButton;
//    Texture leaderBoardButton;
//    Texture settingsButton;
//    Texture exitButton;
    TextButton playButton;
    TextButton leaderBoardButton;
    TextButton settingsButton;
    TextButton exitButton;
    Stage stage;
    Image backgroundImage;
    Texture titleTexture;




    private static final float BUTTON_WIDTH = Gdx.graphics.getWidth() * 0.45f;
    private static final float BUTTON_HEIGHT = Gdx.graphics.getHeight() * 0.07f;
    private static final float PLAY_BUTTON_Y = Gdx.graphics.getHeight() * 0.34f;
    private static final float LEADERBOARD_BUTTON_Y = Gdx.graphics.getHeight() * 0.26f;
    private static final float SETTINGS_BUTTON_Y = Gdx.graphics.getHeight() * 0.18f;
    private static final float EXIT_BUTTON_Y = Gdx.graphics.getHeight() * 0.1f;
    private final float TITLE_WIDTH = Gdx.graphics.getWidth() * 0.95f;
    private Texture background;
    private Texture title;

    public MenuScreen(final Game game) {
//        this.game = game;
//        playButton = new Texture("PlayButton.PNG");
//        leaderBoardButton = new Texture("LeaderBoardButton.PNG");
//        settingsButton = new Texture("SettingsButton.PNG");
//        exitButton = new Texture("ExitButton.PNG");
//        background = new Texture("backgroundImage.png");
//        title = new Texture("GameTitleImage.png");

        this.game = game;
        stage = new Stage();

        // Load the background texture
        Texture bgTexture = new Texture("backgroundImage.png");
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // Create an image actor from the texture
        backgroundImage = new Image(bgTexture);
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        backgroundImage.setPosition(0, 0);
        // Add the background image to the stage as the first actor
        stage.addActor(backgroundImage);


        // Load the title texture and create an Image actor
        titleTexture = new Texture("GameTitleImage.png");
        Image titleImage = new Image(titleTexture);
        // Set the size and position of the title image
        float titleRatio = (float) titleTexture.getWidth() / (float) titleTexture.getHeight();
        float titleWidth = TITLE_WIDTH;
        float titleHeight = titleWidth / titleRatio;
        titleImage.setSize(titleWidth, titleHeight);
        titleImage.setPosition((Gdx.graphics.getWidth() - titleWidth) / 2, Gdx.graphics.getHeight() * 0.65f);
        // Add the title image to the stage
        stage.addActor(titleImage);


        // Initialize buttons with a style
        TextButton.TextButtonStyle buttonStyle = new CustomisedButtonStyle(BUTTON_WIDTH, BUTTON_HEIGHT).getButtonStyle();
        playButton = new TextButton("PLAY", buttonStyle);
        leaderBoardButton = new TextButton("Leaderboard", buttonStyle);
        settingsButton = new TextButton("Settings", buttonStyle);
        exitButton = new TextButton("Exit", buttonStyle);

        // Set positions and sizes for buttons, add them to stage, and add listeners
        setUpButton(playButton, PLAY_BUTTON_Y, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.dispose();
                game.setScreen(new GameScreen(game));
            }
        });

        setUpButton(leaderBoardButton, LEADERBOARD_BUTTON_Y, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.dispose();
                game.setScreen(new LeaderBoardScreen(game));
            }
        });

        setUpButton(settingsButton, SETTINGS_BUTTON_Y, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.dispose();
                game.setScreen(new SettingScreen(game));
            }
        });

        setUpButton(exitButton, EXIT_BUTTON_Y, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.dispose();
                Gdx.app.exit();
            }
        });

        // Add input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void setUpButton(TextButton button, float y, ChangeListener listener) {
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setPosition((Gdx.graphics.getWidth() - BUTTON_WIDTH) / 2, y);
        button.addListener(listener);
        stage.addActor(button);
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the stage
        stage.act(delta);

        // Draw the stage
        stage.draw();
    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        titleTexture.dispose();
    }
}
