package com.ooops.lms.controller;

import com.ooops.lms.model.Music;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class MusicSearchCardController {
    @FXML
    private Label musicNameLabel;

    private MusicController musicController;
    private Music music;

    public void onMusicMouseClicked(MouseEvent mouseEvent) {
        musicController.playMusic(music);
    }

    public void setData(Music music,MusicController musicController) {
        musicNameLabel.setText(music.getTitle());
        this.musicController = musicController;
        this.music = music;
    }
}
