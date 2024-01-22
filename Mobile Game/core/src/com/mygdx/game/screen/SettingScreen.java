package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AttackMode;
import com.mygdx.game.ControlMode;
import com.mygdx.game.Game;
import com.mygdx.game.Items.CustomisedButtonStyle;

public class SettingScreen implements Screen {
    private final String NOT_SELECTED_BUTTON_COLOR = "#255f85";
    private final String SELECTED_BUTTON_COLOR = "#ff6f61";
    final float BACK_BUTTON_WIDTH = Gdx.graphics.getWidth() * 0.45f;
    final float BACK_BUTTON_HEIGHT = Gdx.graphics.getHeight() * 0.07f;
    private Stage stage;
    private Game game;
    private Label descriptionLabel;
    private static ControlMode curControlMode = ControlMode.TOUCH_MODE;
    private static AttackMode curAttackMode = AttackMode.TAP_MODE;
    private TextButton faceButton;
    private TextButton touchButton;
    private TextButton gyroButton;
    private TextButton tapButton;
    private TextButton blinkButton;
    private TextButton voiceButton;
    private Texture background;

    public SettingScreen(final Game game) {
        this.game = game;
        background = new Texture("backgroundImage.png");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create a skin
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Add Font for title-labels in skin
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Karma Future.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(Gdx.graphics.getHeight() * 0.03f);; // set font size
        // parameter.color = Color.valueOf("#ffffff"); // set font color
        BitmapFont titleLabelFont = generator.generateFont(parameter);
        generator.dispose();
        skin.add("titleLabelFont", titleLabelFont);

        // Add Font for description labels in skin
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font/light_pixel-7.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(Gdx.graphics.getHeight() * 0.018f);; // set font size
        // parameter.color = Color.valueOf("#dfec11"); // set font color
        BitmapFont descLabelFont = generator.generateFont(parameter);
        generator.dispose();
        skin.add("descLabelFont", descLabelFont);

        // Set the style (with color) for title and description labels
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(skin.getFont("titleLabelFont"), Color.valueOf("#ffffff"));
        Label.LabelStyle descLabelStyle = new Label.LabelStyle(skin.getFont("descLabelFont"), Color.valueOf("#e8e288"));


        // Set the style (with font color) for button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        textButtonStyle.font = skin.getFont("descLabelFont");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.valueOf("#e8e288");
        textButtonStyle.checkedFontColor = Color.YELLOW;


        // Create UI widgets
        Table table = new Table();
        table.setFillParent(true);

        // Label label = new Label("Sample Text", labelStyle);
        Label movementLabel = new Label("Movement Control", titleLabelStyle);
        faceButton = new TextButton("Face", textButtonStyle);
        touchButton = new TextButton("Touch", textButtonStyle);
        touchButton.setChecked(true);
        gyroButton = new TextButton("Gyroscope", textButtonStyle);

        Label attackLabel = new Label("Attack Control", titleLabelStyle);
        tapButton = new TextButton("Tap", textButtonStyle);
        tapButton.setChecked(true);
        blinkButton = new TextButton("Eyes", textButtonStyle);
        voiceButton = new TextButton("Voice", textButtonStyle);


        setAllButtonColorToNotSelected();
        setButtonColor();
        // Add listeners to the buttons
        addButtonListener(faceButton, "Use face to control the spaceship", ControlMode.FACE_MODE, null, touchButton, gyroButton);
        addButtonListener(touchButton, "Touches left and right side of the screen to control the spaceship", ControlMode.TOUCH_MODE, null, faceButton, gyroButton);
        addButtonListener(gyroButton, "Use gyroscope to control the spaceship", ControlMode.GYROSCOPE_MODE, null, faceButton, touchButton);
        addButtonListener(tapButton, "Tap screen to fire laser", null, AttackMode.TAP_MODE, blinkButton, voiceButton);
        addButtonListener(blinkButton, "Blink eyes to fire laser", null, AttackMode.EYES_BLINKING_MODE, tapButton, voiceButton);
        addButtonListener(voiceButton, "Use voice to fire laser", null, AttackMode.VOICE_MODE, tapButton, blinkButton);

        // Add description
        descriptionLabel = new Label("", descLabelStyle);
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        // Add widgets to table and stage
        float buttonWidth = Gdx.graphics.getWidth() * 0.3f;;
        float buttonHeight = Gdx.graphics.getHeight() * 0.04f;;
        table.add(movementLabel).colspan(3).padBottom(100);
        table.row();
        table.add(faceButton).width(buttonWidth).height(buttonHeight).padRight(10);
        table.add(touchButton).width(buttonWidth).height(buttonHeight).padRight(10);
        table.add(gyroButton).width(buttonWidth).height(buttonHeight).padTop(0);
        table.row();
        table.add(attackLabel).colspan(3).padTop(150).padBottom(100);
        table.row();
        table.add(tapButton).width(buttonWidth).height(buttonHeight).padRight(10);
        table.add(blinkButton).width(buttonWidth).height(buttonHeight).padRight(10);
        table.add(voiceButton).width(buttonWidth).height(buttonHeight).padTop(0);
        table.row();
        table.row().width(Gdx.graphics.getWidth() - 40).padTop(100); // Add some padding
        table.add(descriptionLabel).colspan(3);

        stage.addActor(table);


        TextButton.TextButtonStyle backStyle = new CustomisedButtonStyle(BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT).getButtonStyle();
        // Display back button
        TextButton backButton = new TextButton("BACK", backStyle);

        backButton.setWidth(BACK_BUTTON_WIDTH);
        backButton.setHeight(BACK_BUTTON_HEIGHT);
        backButton.setPosition((Gdx.graphics.getWidth() - backButton.getWidth()) / 2, Gdx.graphics.getHeight() * 0.1f);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        stage.addActor(backButton);
    }

    private void addButtonListener(final TextButton activeButton, final String description, final ControlMode controlMode, final AttackMode attackMode, final TextButton... otherButtons) {
        activeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeButton.setChecked(true);
//                activeButton.setColor(Color.valueOf(SELECTED_BUTTON_COLOR));
                for (TextButton button : otherButtons) {
//                    button.setColor(Color.valueOf(NOT_SELECTED_BUTTON_COLOR));
                    button.setChecked(false);
                }
                descriptionLabel.setText(description);
                if (controlMode != null) {
                    SettingScreen.curControlMode = controlMode;
                } else {
                    SettingScreen.curAttackMode = attackMode;
                }
                descriptionLabel.setText(description);
                if (controlMode != null) {
                    SettingScreen.curControlMode = controlMode;
                } else {
                    SettingScreen.curAttackMode = attackMode;
                }
            }
        });
    }

    private void setAllButtonColorToNotSelected() {
        faceButton.setColor(Color.valueOf(NOT_SELECTED_BUTTON_COLOR));
        touchButton.setColor(Color.valueOf(NOT_SELECTED_BUTTON_COLOR));
        gyroButton.setColor(Color.valueOf(NOT_SELECTED_BUTTON_COLOR));
        tapButton.setColor(Color.valueOf(NOT_SELECTED_BUTTON_COLOR));
        blinkButton.setColor(Color.valueOf(NOT_SELECTED_BUTTON_COLOR));
        voiceButton.setColor(Color.valueOf(NOT_SELECTED_BUTTON_COLOR));
    }

    public void setButtonColor() {
        setAllButtonColorToNotSelected();
        if (SettingScreen.getCurAttackMode() == AttackMode.TAP_MODE) {
            tapButton.setColor(Color.valueOf(SELECTED_BUTTON_COLOR));
        } else if (SettingScreen.getCurAttackMode() == AttackMode.EYES_BLINKING_MODE) {
            blinkButton.setColor(Color.valueOf(SELECTED_BUTTON_COLOR));
        } else {
            voiceButton.setColor(Color.valueOf(SELECTED_BUTTON_COLOR));
        }

        if (SettingScreen.getCurControlMode() == ControlMode.FACE_MODE) {
            faceButton.setColor(Color.valueOf(SELECTED_BUTTON_COLOR));
        } else if (SettingScreen.getCurControlMode() == ControlMode.TOUCH_MODE) {
            touchButton.setColor(Color.valueOf(SELECTED_BUTTON_COLOR));
        } else {
            gyroButton.setColor(Color.valueOf(SELECTED_BUTTON_COLOR));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        setButtonColor();
        game.batch.begin();
        float backgroundRatio = (float) background.getWidth() / (float) background.getHeight();
        float newBackgroundWidth = Gdx.graphics.getHeight() * backgroundRatio;
        game.batch.draw(background, (Gdx.graphics.getWidth() - newBackgroundWidth) / 2, 0, newBackgroundWidth, Gdx.graphics.getHeight());
        game.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
    }

    public static ControlMode getCurControlMode() {
        return curControlMode;
    }

    public static AttackMode getCurAttackMode() {
        return curAttackMode;
    }
}
