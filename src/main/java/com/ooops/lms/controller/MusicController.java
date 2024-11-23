package com.ooops.lms.controller;

import com.ooops.lms.model.Music;
import com.ooops.lms.music.MusicInfoFetcher;
import com.ooops.lms.util.Sound;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MusicController {
    private static final String MUSIC_SEARCH_CARD_FXML = "/com/ooops/lms/library_management_system/MusicSearchCard-view.fxml";
    @FXML
    private ListView<HBox> musicSuggestionList;
    @FXML
    private TextField searchMusicText;
    @FXML
    private AnchorPane musicContainer;
    @FXML
    private Text musicNameLabel;
    @FXML
    private ImageView musicDanceImage;
    @FXML
    WebView webView;

    List<Music> musicList = Arrays.asList(new Music("1","1","1"),new Music("1","1","1"),new Music("1","1","1"));

    private WebEngine webEngine;
    private ObservableList<HBox> filteredSuggestions = FXCollections.observableArrayList();

    public void initialize() {
        Sound.getInstance().playSound("musicView.mp3");
    }

    public void playMusic(Music music) {
        clearSuggestions();
        webEngine = webView.getEngine();

        //String youtubeUrl = "https://www.youtube.com/watch?v=uKxyLmbOc0Q";
        String youtubeUrl = "https://www.youtube.com/watch?v=" +music.getMusicId();

        webEngine.load(youtubeUrl);

        musicNameLabel.setText(music.getTitle());
    }

    private void fetchMusicFromApi(String keyword) {
        Task<List<Music>> fetchMusicTask = new Task<>() {
            @Override
            protected List<Music> call() throws Exception {
                return MusicInfoFetcher.searchVideos(keyword);
            }
        };

        // Xử lý kết quả sau khi API trả về
        fetchMusicTask.setOnSucceeded(event -> {
            List<Music> musicList = fetchMusicTask.getValue();
            updateSuggestions(musicList);
        });

        // Khởi chạy task trong một thread khác
        new Thread(fetchMusicTask).start();
    }

    private void updateSuggestions(List<Music> musicList) {
        for (int i = 0; i < musicList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(MUSIC_SEARCH_CARD_FXML));
                HBox cardBox = fxmlLoader.load();
                MusicSearchCardController cardController = fxmlLoader.getController();
                cardController.setData(musicList.get(i), this);
                filteredSuggestions.add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i == 6) break;
        }

        adjustSuggestionListHeight();
    }

    private void adjustSuggestionListHeight() {
        musicSuggestionList.setPrefHeight(filteredSuggestions.size() * 60);
        musicSuggestionList.setItems(filteredSuggestions);

        if (musicSuggestionList.getPrefHeight() > 360) {
            musicSuggestionList.setPrefHeight(360);
        }
        musicContainer.setVisible(!filteredSuggestions.isEmpty());
        musicSuggestionList.setVisible(!filteredSuggestions.isEmpty());
    }

    private void clearSuggestions() {
        musicContainer.setVisible(false);
        filteredSuggestions.clear();
        searchMusicText.clear();
    }

    public void onPreviousButtonAction(ActionEvent actionEvent) {
    }

    public void onOnOffButtonAction(ActionEvent actionEvent) {
        WebEngine webEngine = webView.getEngine();

        boolean isPlaying = (boolean) webEngine.executeScript("var video = document.querySelector('video');" +
                "if (video) { return !video.paused; } else { return false; }"); // Kiểm tra video có đang phát không

        if (isPlaying) {
            webEngine.executeScript("document.querySelector('video').pause();");
            System.out.println("Video đã tạm dừng.");
        } else {
            webEngine.executeScript("document.querySelector('video').play();");
            System.out.println("Video đã tiếp tục.");
        }
    }

    public void onNextButtonAction(ActionEvent actionEvent) {
        webEngine.executeScript("document.querySelector('video').pause();");
    }

    public void onSearchButtonAction(ActionEvent actionEvent) {
        String keyword = searchMusicText.getText();
            fetchMusicFromApi(keyword);
    }
}
