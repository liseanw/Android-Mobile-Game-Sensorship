package com.mygdx.game;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.screen.SettingScreen;

public class AudioSensor{
    private static final int SAMPLE_RATE = 44100; // Sample rate in Hz
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
    private static Context mContext;
    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private boolean aboveThreshold = false;
    private int threshold = 600; // threshold

    public AudioSensor(Context context) {
        mContext = context;
        startGame();
    }

    public void startGame() {
        if (isRecording) {
            return;
        }

        try{
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        } catch (SecurityException e) {

        }

        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            // handle this error later (initialization)
            return;
        }

        audioRecord.startRecording();
        isRecording = true;

        final Handler handler = new Handler(Looper.getMainLooper());

        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] audioBuffer = new byte[BUFFER_SIZE];
                while (isRecording) {
                    int bytesRead = audioRecord.read(audioBuffer, 0, BUFFER_SIZE);
                    if (bytesRead < 0) {
                        break;
                    }

                    // calculate volume
                    double volume = calculateVolume(audioBuffer, bytesRead);
//                    Log.d("AudioSensor", "Volume: " + volume);
                    if (volume > threshold) {
                        if (!aboveThreshold) {
                            aboveThreshold = true;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (SettingScreen.getCurAttackMode() == AttackMode.VOICE_MODE) {
                                        if (volume < 1200){
                                            GameScreen.shoot(true, false);
                                        } else if (volume >= 1200){
                                            GameScreen.shoot(true, true);
                                        }

                                        Log.d("AudioSensor", "Above threshold");
                                    }
                                }
                            });
                        }
                    } else {
                        if (SettingScreen.getCurAttackMode() == AttackMode.VOICE_MODE) {
                            GameScreen.shoot(false, false);
                        }
                        aboveThreshold = false;
                    }
                }
            }
        });
        audioThread.start();
    }

    public void stopGame() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    private double calculateVolume(byte[] audioData, int bytesRead) {
        // volume calculator
        long sumOfSquares = 0;
        for (int i = 0; i < bytesRead; i += 2) {
            short audioSample = (short) ((audioData[i] & 0xFF) | (audioData[i + 1] << 8));
            sumOfSquares += audioSample * audioSample;
        }
        double rms = Math.sqrt(sumOfSquares / (bytesRead / 2));
        return rms;
    }
}
