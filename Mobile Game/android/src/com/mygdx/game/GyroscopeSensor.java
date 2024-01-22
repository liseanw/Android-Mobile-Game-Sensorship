package com.mygdx.game;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Space;

import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.screen.SettingScreen;

public class GyroscopeSensor {
    public SensorManager sensorManager;
    public Sensor accelerometerSensor;
    public SensorEventListener accelerometerListener;

    public GyroscopeSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start() {
        // Create a sensor event listener
        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float xAcceleration = event.values[0];
                float yAcceleration = event.values[1];
                float zAcceleration = event.values[2];

                // Calculate the total acceleration (magnitude)
                double totalAcceleration = Math.sqrt(
                        xAcceleration * xAcceleration +
                                yAcceleration * yAcceleration +
                                zAcceleration * zAcceleration
                );

                // Calculate the angle of tilt in degrees
                double tiltAngleDegrees = Math.toDegrees(Math.asin(xAcceleration / totalAcceleration));

                // Define thresholds for left and right tilt angles (adjust as needed)
                double leftTiltThreshold = -10.0; // Example threshold for left tilt
                double rightTiltThreshold = 10.0; // Example threshold for right tilt

                if (SettingScreen.getCurControlMode() == ControlMode.GYROSCOPE_MODE) {
                    // Check if the phone is tilted to the left
                    if (tiltAngleDegrees < leftTiltThreshold) {
                        Spaceship.move(tiltAngleDegrees);
                    }
                    // Check if the phone is tilted to the right
                    else if (tiltAngleDegrees > rightTiltThreshold) {
                        Spaceship.move(tiltAngleDegrees);
                    }
                    // If the phone is near horizontal, set the background to white
                    else {
                        Spaceship.move(0);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Handle accuracy changes if needed
            }
        };

    }


}
