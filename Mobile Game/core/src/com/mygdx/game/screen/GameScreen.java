package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.AttackMode;
import com.mygdx.game.Boss;
import com.mygdx.game.Censor;
import com.mygdx.game.ControlMode;
import com.mygdx.game.FirebaseInterface;
import com.mygdx.game.Game;
import com.mygdx.game.Laser;
import com.mygdx.game.Missile;
import com.mygdx.game.Meteor;
import com.mygdx.game.Minion;
import com.mygdx.game.Life;
import com.mygdx.game.GameUI;
import com.mygdx.game.Spaceship;

import java.util.ArrayList;
import java.util.Vector;

public class GameScreen implements Screen {
    private final Game game;
    private Spaceship spaceship;
    private Meteor meteor;
    private Minion minion;
    private Life life;
    private ArrayList<Laser> lasers;
    private ArrayList<Missile> missiles;
    private ArrayList<Meteor> meteors;
    private ArrayList<Minion> minions;
    private int counter = 0;
    private Boss boss;
    public static boolean gameOver = false;
    public boolean levelingUp;
    private BitmapFont endTitle;
    private String title;
    private GameUI UI;
    public static boolean isPaused = false;
    private static int score = 0;
    public int level = 1;
    private Texture background;
    private float elapsedTime = 0;
    private static boolean shooting = false;
    private FaceMesh faceMesh;
    private FirebaseInterface firebaseInterface;
    private ArrayList<Censor> censors;
    private float levelUpTime = 0;
    private static final float LEVEL_UP_DURATION = 3.0f; // 3 seconds for level-up screen
    private boolean canContinue = false; // flag to check if we can continue after time delay
    private static boolean missile = false;

    private int screenWidth = Gdx.graphics.getWidth();
    private int screenHeight = Gdx.graphics.getHeight();

    public GameScreen(Game game) {
        this.game = game;
        faceMesh = new FaceMesh();
        background = new Texture("gameBackground.png");
    }

    @Override
    public void show() {

        spaceship = new Spaceship();
        lasers = new ArrayList<>();
        missiles = new ArrayList<>();
        meteors = new ArrayList<>();
        minions = new ArrayList<>();
        censors = new ArrayList<>();
        life = new Life();
        boss = new Boss(level);
        levelingUp = false;
        endTitle = new BitmapFont(Gdx.files.internal("titlefont.fnt"));
        endTitle.getData().setScale(Gdx.graphics.getWidth() * 0.0012f);
        title = "";
        UI = new GameUI(game, this);

        // add censors to the list with desired coordinates (can remove this after)

        Gdx.input.setInputProcessor(UI.getStage());
    }

    @Override
    public void render(float delta) {
        for (int i = censors.size() -1; i>=0; i--){
            Censor censor = censors.get(i);
            censor.addTime();
        }
        ScreenUtils.clear(0, 0, 0, 1);

        game.batch.begin();
        renderBackground();
        game.batch.end();

        if (!isPaused) {
            faceMesh.drawFace();
        }

        game.batch.begin();

        if (!isPaused) {
            if(!gameOver) {
                if(!levelingUp) {
                    scoreCount(delta);
                    if (shooting && spaceship != null) {
                        if (missile == true){
                            spaceship.shootMissile();
                        } else{
                            spaceship.shoot();
                        }

                    }

                    for (int i = meteors.size() - 1; i >= 0; i--) {
                        Meteor meteor = meteors.get(i);

                        if (!meteor.gone) {
                            meteor.Draw(game.batch);
                            meteor.hitShip(spaceship);
                        } else {
                            meteors.remove(i);
                        }
                    }



                    for (int i = minions.size() - 1; i >= 0; i--) {
                        Minion minion = minions.get(i);

                        if (!minion.gone) {
                            minion.Draw(game.batch);
                            minion.hitShip(spaceship);
                            if (minion.gone == true){
                                float topLeftX = MathUtils.random(0, screenWidth - 100); // Adjust the range as needed
                                float topLeftY = MathUtils.random(0, screenHeight - 100); // Adjust the range as needed
                                float bottomRightX = topLeftX + 300; // Adjust the width as needed
                                float bottomRightY = topLeftY + 300; // Adjust the height as needed
                                censors.add(new Censor(new Vector2(topLeftX, topLeftY), new Vector2(bottomRightX, bottomRightY)));
                            }
                        } else {
                            minions.remove(i);
                        }
                    }
                    if (spaceship.HP > 0) {
                        lasers = spaceship.DrawL(game.batch);
                        missiles = spaceship.DrawM(game.batch);
                    }

                    if (spaceship.HP <= 0) {
                        spaceship.sprite.setPosition(-1000, 1000);
                        loseScreen();
                    }

                    for (Laser laser : lasers) {
                        boss.detectHit(laser);
                        for (Meteor meteor : meteors) {
                            meteor.detectHit(laser);
                        }
                        for (Minion minion : minions) {
                            minion.detectHit(laser);
                        }
                    }

                    for (Missile missile : missiles) {
                        boss.detectHit(missile);
                        for (Meteor meteor : meteors) {
                            meteor.detectHit(missile);
                        }
                        for (Minion minion : minions) {
                            minion.detectHit(missile);
                        }
                    }

                    counter++;
                    if (counter % (90 - 15 * level) == 0) {
                        meteor = new Meteor(level);
                        meteors.add(meteor);
                        meteor.spawnMeteor();
                    }
                    if(level >= 3){
                        if (counter % 60 == 0) {
                            minion = new Minion();
                            minions.add(minion);
                            minion.spawnMinion();
                        }
                    }

                    if(level > 3){
                        if(life.gone){
                            double r = Math.random() * 10;
                            if(r <= 1.0){
                                life.spawnLife();
                            }
                        }
                        life.Draw(game.batch);
                        life.hitShip(spaceship);
                    }

                    if (boss.HP > 0) {
                        boss.Draw(game.batch);
                    }

                    if (boss.HP <= 0) {
                        boss.sprite.setPosition(1000, 1000);
                        levelUpScreen();
                    }
                    for (Censor censor : censors) {
                        censor.draw(game.batch);
                    }
                    UI.render(spaceship);
                }
                else {
//                    endTitle.draw(game.batch, title, Gdx.graphics.getWidth()/2 - 350, Gdx.graphics.getHeight()/2);
//                    UI.checkLevelUp(this);
                    levelUpTime += delta; // increment level up time

                    if(levelUpTime < LEVEL_UP_DURATION) {
                        float timeLeft = LEVEL_UP_DURATION - levelUpTime;
                        String message = String.format("Leveling Up! Continue in %ds", (int)Math.ceil(timeLeft));
                        GlyphLayout layout = new GlyphLayout(); // GlyphLayout is used to calculate the width of the text
                        layout.setText(endTitle, message);

                        float x = (Gdx.graphics.getWidth() - layout.width) / 2; // Center the text horizontally
                        float y = (Gdx.graphics.getHeight() + layout.height) / 2; // Center the text vertically

                        endTitle.draw(game.batch, layout, x, y);
                    } else {
                        String message = "Touch anywhere to proceed!";
                        GlyphLayout layout = new GlyphLayout(); // GlyphLayout is used to calculate the width of the text
                        layout.setText(endTitle, message);

                        float x = (Gdx.graphics.getWidth() - layout.width) / 2; // Center the text horizontally
                        float y = (Gdx.graphics.getHeight() + layout.height) / 2; // Center the text vertically

                        endTitle.draw(game.batch, layout, x, y);

                        canContinue = true; // enable continuation after 3 seconds
                        UI.checkLevelUp(this);
                    }

                }
            }
            else {
//                endTitle.draw(game.batch, title, Gdx.graphics.getWidth()/2 - 75, Gdx.graphics.getHeight()/2);
                UI.renderLose(this);
            }
        } else {
            UI.renderPauseMenu(this);
        }

        game.batch.end();
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
        spaceship = new Spaceship();
        lasers = new ArrayList<>();
        meteors = new ArrayList<>();
        minions = new ArrayList<>();
        boss = new Boss(level);
        levelingUp = false;
        gameOver = false;
        endTitle = new BitmapFont(Gdx.files.internal("titlefont.fnt"));
        endTitle.getData().setScale(Gdx.graphics.getWidth() * 0.003f);
        title = "";
        score = 0;
    }

    @Override
    public void dispose() {
        game.batch.dispose();
    }

    public void levelUpScreen() {
        levelingUp = true;
        title = "Leveling Up! Touch anywhere to proceed";
    }

    public void loseScreen() {
        gameOver = true;
        title = "You Lose!";
        game.getFirebaseInterface().sendScore(score);
    }

    public void scoreCount(float delta) {
        elapsedTime += delta;

        if (elapsedTime >= 1.0f) {
            score += 10;
            elapsedTime -= 1.0f;
        }
        UI.renderScore(score);
    }

    public static void shoot(boolean state, boolean isMissile) {
        shooting = state;
        missile = isMissile;
    }


    public void newScreen(){
        level += 1;
        spaceship = new Spaceship();
        lasers = new ArrayList<>();
        meteors = new ArrayList<>();
        minions = new ArrayList<>();
        boss = new Boss(level);
        levelUpTime = 0;
        levelingUp = false;
        canContinue = false; // reset the flag
    }

    public void renderBackground() {
        float backgroundRatio = (float) background.getWidth() / (float) background.getHeight();
        float newBackgroundWidth = Gdx.graphics.getHeight() * backgroundRatio;
        game.batch.draw(background, (Gdx.graphics.getWidth() - newBackgroundWidth) / 2, 0, newBackgroundWidth, Gdx.graphics.getHeight());

        game.batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        game.batch.begin();
    }

    public static int getScore() {
        return score;
    }

}
