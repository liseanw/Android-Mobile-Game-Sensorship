package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class FaceMesh {
    private static ShapeRenderer shapeRenderer;
    private static ArrayList<ConnectionLine> connectionLines = new ArrayList<>();

    public FaceMesh() {
        shapeRenderer = new ShapeRenderer();
    }

    public void drawFace() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 0.4f));
        Gdx.gl.glLineWidth(5f);
        for (ConnectionLine connectionLine : connectionLines) {
            // Convert normalized coordinates to screen coordinates
            float startX = connectionLine.start.x * screenWidth;
            float startY = (1 - connectionLine.start.y) * screenWidth + 500;
            float endX = connectionLine.end.x * screenWidth;
            float endY = (1 - connectionLine.end.y) * screenWidth + 500;

            shapeRenderer.line(startX, startY, endX, endY);
        }
        shapeRenderer.end();
    }

    public static void setConnectionLines(ArrayList<ConnectionLine> connectionLines) {
        FaceMesh.connectionLines = connectionLines;
    }
}
