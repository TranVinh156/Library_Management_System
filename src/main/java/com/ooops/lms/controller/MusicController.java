package com.ooops.lms.controller;

import com.ooops.lms.model.Music;
import com.ooops.lms.music.MusicInfoFetcher;
import com.ooops.lms.util.FXMLLoaderUtil;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

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
    private ImageView musicDanceImage, pauseImage;
    @FXML
    WebView webView;
    private Image pause, play;
    private boolean isPlayingMusic = false;

    List<Music> musicList = new ArrayList<>();
    public static int currentMusic = 0;

    private WebEngine webEngine;
    private ObservableList<HBox> filteredSuggestions = FXCollections.observableArrayList();

    public void initialize() {
        //Sound.getInstance().playSound("musicView.mp3");
        webEngine = webView.getEngine();
        String htmlPath = getClass().getResource("/html/youtubeMusic.html").toExternalForm();
        webEngine.load(htmlPath);
        try {
            pause = new Image(getClass().getResourceAsStream("/image/customer/music/pause-button.png"));
            play = new Image(getClass().getResourceAsStream("/image/customer/music/play-button.png"));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void playMusic(Music music) {
        clearSuggestions();
        webView.getEngine().executeScript("playVideo('" + music.getMusicId() + "');");
        musicNameLabel.setText(music.getTitle());
        pauseImage.setImage(pause);
        isPlayingMusic = true;
        System.out.println(music.getMusicId());
        FXMLLoaderUtil.getInstance().changMusicName(musicNameLabel.getText());

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
            List<Music> musicList2 = fetchMusicTask.getValue();
            musicList.addAll(musicList2);
            updateSuggestions(musicList2);
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

    public Image getPauseImage() {
        return pauseImage.getImage();
    }

    public void onOnOffButtonAction(ActionEvent actionEvent) {
        if (isPlayingMusic) {
            webView.getEngine().executeScript("pauseVideo();");
            pauseImage.setImage(play);
            isPlayingMusic = false;
        } else {
            webView.getEngine().executeScript("continueVideo();");
            pauseImage.setImage(pause);
            isPlayingMusic = true;
        }
        FXMLLoaderUtil.getInstance().setPauseImage(pauseImage.getImage());
    }

    public void onNextButtonAction(ActionEvent actionEvent) {
        playMusic(findMusic("previous"));
        webView.getEngine().executeScript("continueVideo();");
        FXMLLoaderUtil.getInstance().changMusicName(musicNameLabel.getText());
    }

    public void onPreviousButtonAction(ActionEvent actionEvent) {
        playMusic(findMusic("next"));
        webView.getEngine().executeScript("continueVideo();");
        FXMLLoaderUtil.getInstance().changMusicName(musicNameLabel.getText());

    }

    private Music findMusic(String status) {
        System.out.println(musicList.size());
        if (status.equals("next")) {
            currentMusic++;
            if (currentMusic == musicList.size()) {
                currentMusic = 0;
            }
        } else {
            currentMusic--;
            if (currentMusic == -1) {
                currentMusic = musicList.size() - 1;
            }
        }
        return musicList.get(currentMusic);
    }

    public void onSearchButtonAction(ActionEvent actionEvent) {
        String keyword = searchMusicText.getText();
        fetchMusicFromApi(keyword);
    }

    public void onDanceMouseClicked(MouseEvent mouseEvent) {
        Sound.getInstance().playSound("musicView.mp3");
    }
}
