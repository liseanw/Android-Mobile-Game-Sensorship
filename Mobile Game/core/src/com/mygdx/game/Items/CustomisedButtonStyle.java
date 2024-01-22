package com.mygdx.game.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CustomisedButtonStyle {
    private String BACKGROUND_UP_IMAGE = "buttonBackground.PNG";
    private String BACKGROUND_DOWN_IMAGE = "buttonBackground_down.PNG";
    private String FONT = "font/light_pixel-7.ttf";
//    private int FONT_SIZE = 55;
    private static final float FONT_SIZE_PROPORTION = 0.35f;

    private Color FONT_COLOR = Color.BLACK;
    private Color DOWN_FONT_COLOR = Color.GRAY;
    private TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();

    public CustomisedButtonStyle(float buttonWidth, float buttonHeight) {
        Texture backButtonTexture_up = new Texture(Gdx.files.internal(BACKGROUND_UP_IMAGE));
        Texture backButtonTexture_down = new Texture(Gdx.files.internal(BACKGROUND_DOWN_IMAGE));

        Drawable backDrawable_up = new TextureRegionDrawable(new TextureRegion(backButtonTexture_up));
        Drawable backDrawable_down = new TextureRegionDrawable(new TextureRegion(backButtonTexture_down));


        buttonStyle.up = backDrawable_up; // img when normal
        buttonStyle.down = backDrawable_down; // img when clicked


        // Add back button style - font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(buttonHeight * FONT_SIZE_PROPORTION);; // set font size
        BitmapFont buttonFont = generator.generateFont(parameter);
        generator.dispose();

        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = FONT_COLOR;
        buttonStyle.downFontColor = DOWN_FONT_COLOR;
    }

    public TextButton.TextButtonStyle getButtonStyle() {
        return buttonStyle;
    }

}
