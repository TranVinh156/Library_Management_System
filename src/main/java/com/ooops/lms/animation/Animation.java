package com.ooops.lms.animation;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    /**
     * nhân vật nói message.
     * @param message
     */
    public void showMessage(String message) {
        if(!isOnAnimation) {
            return;
        }
        talkImage.setVisible(true);
        talkText.setVisible(true);
        talkText.setText(message);


        PauseTransition pause = new PauseTransition(Duration.seconds(4));

        pause.setOnFinished(event -> {
            talkImage.setVisible(false);
            talkText.setVisible(false);
        });

        pause.play();
    }

    /**
     * thay đổi kích cỡ ảnh từ từ.
     * @param imageView
     * @param size
     */
    public void changeSizeImage(ImageView imageView, int size) {
        imageView.setVisible(true);

        Timeline expandImageWidth = new Timeline(
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(imageView.fitWidthProperty(), size)
                )
        );

        Timeline expandImageHeight = new Timeline(
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(imageView.fitHeightProperty(), size)
                )
        );

        expandImageWidth.play();
        expandImageHeight.play();
    }

    public void zomOutImage(ImageView imageView) {
        imageView.setFitHeight(1);
        imageView.setFitWidth(1);
    }


    /**
     * mở rộng vbox.
     * @param vBox
     * @param millis
     * @param width
     */
    public void enlargeVBox(VBox vBox,int millis, int width) {
        vBox.setVisible(true);
        Timeline expandMenu = new Timeline(
                new KeyFrame(Duration.millis(millis),
                        new KeyValue(vBox.prefWidthProperty(), width))
        );
        expandMenu.play();
    }


    /**
     * thu nhỏ phóng to ảnh liên tục.
     * @param imageView
     */
    public void zoomAnimation(ImageView imageView) {
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), imageView);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition.setAutoReverse(true);

        scaleTransition.play();
    }
}
