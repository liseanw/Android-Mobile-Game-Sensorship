package com.mygdx.game;

import android.graphics.Bitmap;
import android.util.Log;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.ImmutableSet;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutions.facemesh.FaceMesh;
import com.google.mediapipe.solutions.facemesh.FaceMeshResult;
import com.google.mediapipe.solutions.facemesh.FaceMeshConnections;
import com.mygdx.game.screen.ConnectionLine;
import com.mygdx.game.screen.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class FaceMeshRenderer {

    private static FaceMeshResult faceMeshResult;
    ArrayList<ConnectionLine> faceConnections;

    public void draw(FaceMeshResult result) {
        faceMeshResult = result;

        int numFaces = faceMeshResult.multiFaceLandmarks().size();
        //
        faceConnections = new ArrayList<>();
        // For each face in the result, draw the landmarks.
        for (int i = 0; i < numFaces; i++) {
            drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_TESSELATION);
            drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_RIGHT_EYE);
            drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_RIGHT_EYEBROW);
            drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_LEFT_EYE);
            drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_LEFT_EYEBROW);
            drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_FACE_OVAL);
            drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_LIPS);
            if (faceMeshResult.multiFaceLandmarks().get(i).getLandmarkCount() == FaceMesh.FACEMESH_NUM_LANDMARKS_WITH_IRISES) {
                drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_RIGHT_IRIS);
                drawLandmarks(faceMeshResult.multiFaceLandmarks().get(i).getLandmarkList(), FaceMeshConnections.FACEMESH_LEFT_IRIS);
            }
            com.mygdx.game.screen.FaceMesh.setConnectionLines(faceConnections);
        }
    }

    private void drawLandmarks(List<LandmarkProto.NormalizedLandmark> faceLandmarkList,
                               ImmutableSet<FaceMeshConnections.Connection> connections) {

        for (FaceMeshConnections.Connection c : connections) {
            Vector2 start = new Vector2(faceLandmarkList.get(c.start()).getX(), faceLandmarkList.get(c.start()).getY());
            Vector2 end = new Vector2(faceLandmarkList.get(c.end()).getX(), faceLandmarkList.get(c.end()).getY());
//            shapeRenderer.line(start, end);
            faceConnections.add(new ConnectionLine(start, end));
        }
    }

}
