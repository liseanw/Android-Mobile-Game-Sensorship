package com.mygdx.game;

import android.media.FaceDetector;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.facemesh.FaceMesh;
import com.google.mediapipe.solutions.facemesh.FaceMeshOptions;
import com.google.mediapipe.solutions.facemesh.FaceMeshResult;
import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.screen.SettingScreen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class AndroidLauncher extends AndroidApplication  implements LifecycleOwner {
	private static final int RECORD_AUDIO_PERMISSION_CODE = 1;
	private AudioSensor audioSensor;
	private GyroscopeSensor gyroscopeSensor;
	private static final String TAG = "MainActivity";
	private FaceMesh facemesh;
	private static final boolean RUN_ON_GPU = true;
	private static boolean start = false;
	private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
	private FaceMeshRenderer faceMeshRenderer;

	@NonNull
	@Override
	public Lifecycle getLifecycle() {
		return lifecycleRegistry;
	}

	private enum InputSource {
		UNKNOWN,
		CAMERA,
	}
	private InputSource inputSource = InputSource.UNKNOWN;
	private CameraInput cameraInput;
	private SolutionGlSurfaceView<FaceMeshResult> glSurfaceView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		Game game = new Game(new AndroidFirebaseInterface());
		super.onCreate(savedInstanceState);
		lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
		// initialize AudioSensor here
		audioSensor = new AudioSensor(this);
		gyroscopeSensor = new GyroscopeSensor(this);
		faceMeshRenderer = new FaceMeshRenderer();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);
		// Check and request permissions
		if (checkPermission()) {
			// permissions are already granted, start game
			startSensor();
		} else {
			// request permissions
			requestPermission();
		}


	}

	private boolean checkPermission() {
		int result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
		return result == PackageManager.PERMISSION_GRANTED;
	}

	private void requestPermission() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_CODE);
	}

	private void startSensor() {
		// start game here
		gyroscopeSensor.start();
		audioSensor.startGame();
		setupLiveDemoUiComponents();
		startCamera();
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// permission granted, start the game
				startSensor();
			} else {
				// permission denied, ask user to enable for this mode, maybe return to home screen first
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		lifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
		if (inputSource == InputSource.CAMERA) {
			cameraInput = new CameraInput(this);
			cameraInput.setNewFrameListener(textureFrame -> facemesh.send(textureFrame));
		}
		gyroscopeSensor.sensorManager.registerListener(gyroscopeSensor.accelerometerListener, gyroscopeSensor.accelerometerSensor, gyroscopeSensor.sensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
		if (inputSource == InputSource.CAMERA) {
            cameraInput.close();
        }
		gyroscopeSensor.sensorManager.unregisterListener(gyroscopeSensor.accelerometerListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
	}

	/** Sets up the UI components for the live demo with camera input. */
	private void setupLiveDemoUiComponents() {
		stopCurrentPipeline();
		setupStreamingModePipeline(AndroidLauncher.InputSource.CAMERA);
	}

	@Override
	protected void onStart() {
		super.onStart();
		lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);

	}

	/** Sets up core workflow for streaming mode. */
	private void setupStreamingModePipeline(AndroidLauncher.InputSource inputSource) {
		this.inputSource = inputSource;
		facemesh =
				new FaceMesh(
						this,
						FaceMeshOptions.builder()
								.setStaticImageMode(false)
								.setRefineLandmarks(true)
								.setRunOnGpu(RUN_ON_GPU)
								.build());
		facemesh.setErrorListener((message, e) -> Log.e(TAG, "MediaPipe Face Mesh error:" + message));

		cameraInput = new CameraInput(this);
		cameraInput.setNewFrameListener(textureFrame -> facemesh.send(textureFrame));

		facemesh.setResultListener(
				faceMeshResult -> {
					logNoseLandmark(faceMeshResult, /*showPixelValues=*/ false);
					checkForBlink(faceMeshResult);
					faceMeshRenderer.draw(faceMeshResult);
				});
	}

	private void startCamera() {
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		int screenHeight = getResources().getDisplayMetrics().heightPixels;

		cameraInput.start(
				this,
				facemesh.getGlContext(),
				CameraInput.CameraFacing.FRONT,
				screenWidth,
				screenHeight);
	}

	private void stopCurrentPipeline() {
		if (cameraInput != null) {
			cameraInput.setNewFrameListener(null);
			cameraInput.close();
		}
		if (facemesh != null) {
			facemesh.close();
		}
	}

	private void logNoseLandmark(FaceMeshResult result, boolean showPixelValues) {
		if (result == null || result.multiFaceLandmarks().isEmpty()) {
			return;
		}
		LandmarkProto.NormalizedLandmark noseLandmark = result.multiFaceLandmarks().get(0).getLandmarkList().get(1);
		// For Bitmaps, show the pixel values. For texture inputs, show the normalized coordinates.
		if (showPixelValues) {
			int width = result.inputBitmap().getWidth();
			int height = result.inputBitmap().getHeight();
			Log.i(
					TAG,
					String.format(
							"MediaPipe Face Mesh nose coordinates (pixel values): x=%f, y=%f",
							noseLandmark.getX() * width, noseLandmark.getY() * height));
		} else {
			Log.i(
					TAG,
					String.format(
							"MediaPipe Face Mesh nose normalized coordinates (value range: [0, 1]): x=%f, y=%f",
							noseLandmark.getX(), noseLandmark.getY()));
		}

		if (Spaceship.getPosition() != null && SettingScreen.getCurControlMode() == ControlMode.FACE_MODE) {
			Spaceship.setPosition(noseLandmark.getX(), noseLandmark.getY());
		}

	}
	private boolean leftEyeClosed;
	private boolean rightEyeClosed;

	private void checkForBlink(FaceMeshResult result) {
		if (result == null || result.multiFaceLandmarks().isEmpty()) {
			return;
		}

		LandmarkProto.NormalizedLandmark leftEyeTop = result.multiFaceLandmarks().get(0).getLandmarkList().get(159);
		LandmarkProto.NormalizedLandmark leftEyeBottom = result.multiFaceLandmarks().get(0).getLandmarkList().get(145);
		double leftEyeDistance = Math.abs(leftEyeTop.getY() - leftEyeBottom.getY());

		LandmarkProto.NormalizedLandmark rightEyeTop = result.multiFaceLandmarks().get(0).getLandmarkList().get(386);
		LandmarkProto.NormalizedLandmark rightEyeBottom = result.multiFaceLandmarks().get(0).getLandmarkList().get(374);
		double rightEyeDistance = Math.abs(rightEyeTop.getY() - rightEyeBottom.getY());

		double blinkThreshold = 0.02;

		boolean newLeftEyeClosed = leftEyeDistance < blinkThreshold;
		boolean newRightEyeClosed = rightEyeDistance < blinkThreshold;

		if (SettingScreen.getCurAttackMode() == AttackMode.EYES_BLINKING_MODE) {

			if (newLeftEyeClosed && newRightEyeClosed) {
				GameScreen.shoot(true, true);
			} else if (newLeftEyeClosed || newRightEyeClosed){

				GameScreen.shoot(true, false);
			} else {
				GameScreen.shoot(false, false);
			}
		}

		leftEyeClosed = newLeftEyeClosed;
		rightEyeClosed = newRightEyeClosed;
	}

}
