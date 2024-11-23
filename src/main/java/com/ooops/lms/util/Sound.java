package com.ooops.lms.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    private static Sound instance = null;

    public static Sound getInstance() {
        if (instance == null) {
            instance = new Sound();
        }
        return instance;
    }

    public void playSound(String soundName) {
        String audioFilePath = getClass().getResource("/sound/" + soundName).toExternalForm();

        // Tạo Media và MediaPlayer
        Media media = new Media(audioFilePath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        // Phát âm thanh
        mediaPlayer.setOnReady(() -> {
            System.out.println("Phát âm thanh...");
            mediaPlayer.play();
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("Kết thúc phát âm thanh.");
        });

        mediaPlayer.setOnError(() -> {
            System.err.println("Lỗi phát âm thanh: " + mediaPlayer.getError());
        });
    }
}
