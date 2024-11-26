package com.ooops.lms.animation;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Animation {
    private static Animation instance;
    private ImageView characterImage, talkImage;
    private Text talkText;

    private boolean isOnAnimation = true;

    public void turnOnAnimation() {
        isOnAnimation = true;
        characterImage.setVisible(true);
    }

    public void turnOffAnimation() {
        isOnAnimation = false;
        characterImage.setVisible(false);
    }

    /**
     * singleton.
     *
     * @return instance
     */
    public static Animation getInstance() {
        if (instance == null) {
            instance = new Animation();
        }
        return instance;
    }

    public void setAll(ImageView characterImage, ImageView talkImage, Text talkText) {
        this.characterImage = characterImage;
        this.talkImage = talkImage;
        this.talkText = talkText;
    }

    public void changeImage1() {
        if(!isOnAnimation) {
            return;
        }
        characterImage.setImage(new Image(getClass().getResourceAsStream("/image/customer/menu/Phi.png")));
        AnchorPane.setLeftAnchor(characterImage, 0.0);
        characterImage.toFront();
    }

    public void changeImage2() {
        if(!isOnAnimation) {
            return;
        }
        characterImage.setImage(new Image(getClass().getResourceAsStream("/image/customer/menu/animation2.gif")));
        AnchorPane.setLeftAnchor(characterImage, -40.0);
    }

    public void showMessage(String message) {
        if(!isOnAnimation) {
            return;
        }
        talkImage.setVisible(true);
        talkText.setVisible(true);
        talkText.setText(message);


        PauseTransition pause = new PauseTransition(Duration.seconds(7));

        pause.setOnFinished(event -> {
            talkImage.setVisible(false);
            talkText.setVisible(false);
        });

        pause.play();
    }

}
