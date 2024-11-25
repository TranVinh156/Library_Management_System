package com.ooops.lms.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    private static Sound instance = null;

    private boolean isOnSound = true;

    public static Sound getInstance() {
        if (instance == null) {
            instance = new Sound();
        }
        return instance;
    }

    private MediaPlayer mediaPlayer;

    public void turnOnSound() {
        isOnSound = true;
    }

    public void turnOffSound() {
        isOnSound = false;
    }

    public void playSound(String soundName) {
        if (!isOnSound) {
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        String audioFilePath = getClass().getResource("/sound/" + soundName).toExternalForm();
        Media media = new Media(audioFilePath);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> mediaPlayer.play());
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.dispose());
        mediaPlayer.setOnError(() -> System.err.println("Lỗi: " + mediaPlayer.getError()));
    }
}
