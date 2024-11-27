package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.animation.Animation;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.Sound;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Random;

public class TicTacToeController {
    @FXML
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Button[] buttons;
    @FXML
    Label playModeLabel;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";


    private final Random random = new Random();
    private boolean XWin = false, OWin = false, player1Turn;

    private static final String PLAY_WITH_COMPUTER = "Chơi với máy";
    private static final String PLAY_ALONE = "Chơi một mình";


    public void initialize() {
        buttons = new Button[]{button1, button2, button3, button4, button5, button6, button7, button8, button9};
        for (int i = 0; i < 9; i++) {
            int index = i;
            buttons[i].setOnAction(event -> actionPerformed(index));
        }
        Animation.getInstance().showMessage("Chế độ mặc định là tự kỉ nhé bạn yêu ơi");
        Sound.getInstance().playSound("cacBanYeuOi.mp3");
    }


    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(SETTING_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        } else {
            System.err.println("Failed to load Information-view.fxml");
        }
    }

    public void onReplayButtonAction(ActionEvent actionEvent) {
        for (Button b : buttons) {
            b.setText("");
            b.setDisable(false);
            b.getStyleClass().clear();
            b.getStyleClass().add("button-3");
        }
        XWin = false;
        OWin = false;
        firstTurn();
    }

    public void onPlayComputerButtonAction(ActionEvent actionEvent) {
        playModeLabel.setText(PLAY_WITH_COMPUTER);
        onReplayButtonAction(actionEvent);
    }

    public void onSelfPlayButtonAction(ActionEvent actionEvent) {
        playModeLabel.setText(PLAY_ALONE);
        onReplayButtonAction(actionEvent);
    }


    /**
     * nghe các hành động của người dùng khi ấn vào các button.
     * @param index
     */
    public void actionPerformed(int index) {
        Button button = buttons[index];
        if (playModeLabel.getText().equals(PLAY_WITH_COMPUTER)) {
            if (button.getText().equals("")) {
                if (!OWin) {
                    button.getStyleClass().clear();
                    button.getStyleClass().add("button-1");
                    button.setText("X");
                    check(true);
                }
                if (!XWin) {
                    int n = computerPlay(buttons, 1, 0);
                    buttons[n].getStyleClass().clear();
                    buttons[n].getStyleClass().add("button-2");
                    buttons[n].setText("O");
                    System.out.println("haiz");
                    check(true);
                }
            }
        } else {
            if (player1Turn) {
                if (button.getText().equals("")) {
                    button.getStyleClass().clear();
                    button.getStyleClass().add("button-1");
                    button.setText("X");
                    player1Turn = false;
                    check(true);
                }
            } else {
                if (button.getText().equals("")) {
                    button.getStyleClass().clear();
                    button.getStyleClass().add("button-2");
                    button.setText("O");
                    player1Turn = true;
                    check(true);
                }
            }
        }

        // Kiểm tra hòa
        int cnt = 0;
        for (
                Button b : buttons) {
            if (!b.getText().equals("")) cnt++;
        }
        if (cnt == 9 && !OWin && !XWin) {
            CustomerAlter.showMessage("Chúng ta huề");
        }
    }


    /**
     * random chọn người đi trước.
     */
    public void firstTurn() {
        if (random.nextInt(2) == 0) {
            if (playModeLabel.getText().equals(PLAY_WITH_COMPUTER)) {
                CustomerAlter.showMessage("Mày đi trước");
            } else {
                CustomerAlter.showMessage("X đi trước");
            }
            player1Turn = true;
        } else {
            int k = random.nextInt(9);
            buttons[k].setText("O");
            buttons[k].getStyleClass().clear();
            buttons[k].getStyleClass().add("button-book2");
            if (playModeLabel.getText().equals(PLAY_WITH_COMPUTER)) {
                CustomerAlter.showMessage("Tao đi trước");
            } else {
                CustomerAlter.showMessage("O đi trước");
            }
            player1Turn = false;
        }
    }

    /**
     * điều kiện để win.
     * @param real
     */
    public void check(boolean real) {
        if (
                (buttons[0].getText().equals("X")) &&
                        (buttons[1].getText().equals("X")) &&
                        (buttons[2].getText().equals("X"))
        ) {
            XWin = true;
            if (real)
                xWins(0, 1, 2);
        }

        if (
                (buttons[3].getText().equals("X")) &&
                        (buttons[4].getText().equals("X")) &&
                        (buttons[5].getText().equals("X"))
        ) {
            XWin = true;
            if (real)
                xWins(3, 4, 5);
        }

        if (
                (buttons[6].getText().equals("X")) &&
                        (buttons[7].getText().equals("X")) &&
                        (buttons[8].getText().equals("X"))
        ) {
            XWin = true;
            if (real)
                xWins(6, 7, 8);
        }

        if (
                (buttons[0].getText().equals("X")) &&
                        (buttons[3].getText().equals("X")) &&
                        (buttons[6].getText().equals("X"))
        ) {
            XWin = true;
            if (real)
                xWins(0, 3, 6);
        }

        if (
                (buttons[1].getText().equals("X")) &&
                        (buttons[4].getText().equals("X")) &&
                        (buttons[7].getText().equals("X"))
        ) {
            XWin = true;
            if (real)
                xWins(1, 4, 7);
        }

        if (
                (buttons[2].getText().equals("X")) &&
                        (buttons[5].getText().equals("X")) &&
                        (buttons[8].getText().equals("X"))
        ) {
            XWin = true;
            if (real)
                xWins(2, 5, 8);
        }

        if (
                (buttons[0].getText().equals("X")) &&
                        (buttons[4].getText().equals("X")) &&
                        (buttons[8].getText().equals("X"))
        ) {
            XWin = true;
            if (real)
                xWins(0, 4, 8);
        }

        if (
                (buttons[2].getText().equals("X")) &&
                        (buttons[4].getText().equals("X")) &&
                        (buttons[6].getText().equals("X"))
        ) {
            XWin = true;
            if (real)
                xWins(2, 4, 6);
        }


        //Owin
        if (
                (buttons[0].getText().equals("O")) &&
                        (buttons[1].getText().equals("O")) &&
                        (buttons[2].getText().equals("O"))
        ) {
            OWin = true;
            if (real)
                oWins(0, 1, 2);
        }

        if (
                (buttons[3].getText().equals("O")) &&
                        (buttons[4].getText().equals("O")) &&
                        (buttons[5].getText().equals("O"))
        ) {
            OWin = true;
            if (real)
                oWins(3, 4, 5);
        }

        if (
                (buttons[6].getText().equals("O")) &&
                        (buttons[7].getText().equals("O")) &&
                        (buttons[8].getText().equals("O"))
        ) {
            OWin = true;
            if (real)
                oWins(6, 7, 8);
        }

        if (
                (buttons[0].getText().equals("O")) &&
                        (buttons[3].getText().equals("O")) &&
                        (buttons[6].getText().equals("O"))
        ) {
            OWin = true;
            if (real)
                oWins(0, 3, 6);
        }

        if (
                (buttons[1].getText().equals("O")) &&
                        (buttons[4].getText().equals("O")) &&
                        (buttons[7].getText().equals("O"))
        ) {
            OWin = true;
            if (real)
                oWins(1, 4, 7);
        }

        if (
                (buttons[2].getText().equals("O")) &&
                        (buttons[5].getText().equals("O")) &&
                        (buttons[8].getText().equals("O"))
        ) {
            OWin = true;
            if (real)
                oWins(2, 5, 8);
        }

        if (
                (buttons[0].getText().equals("O")) &&
                        (buttons[4].getText().equals("O")) &&
                        (buttons[8].getText().equals("O"))
        ) {
            OWin = true;
            if (real)
                oWins(0, 4, 8);
        }

        if (
                (buttons[2].getText().equals("O")) &&
                        (buttons[4].getText().equals("O")) &&
                        (buttons[6].getText().equals("O"))
        ) {
            OWin = true;
            if (real)
                oWins(2, 4, 6);
        }
    }

    public int computerPlay(Button[] buttons, int k, int o) {
        if (k == 3) {
            return checkTree();
        }

        if (k % 2 != 0) {
            int maxx = 0;
            for (int i = 0; i < 9; i++) {
                if (buttons[i].getText().equals("")) {
                    Button[] Copy = buttons.clone();

                    Copy[i].setText("O");
                    if (checkTree() == 3) {
                        Copy[i].setText("");
                        return i;
                    }
                    int n = computerPlay(Copy, k + 1, o);
                    if (maxx < n) {
                        maxx = n;
                        o = i;
                    }
                    Copy[i].setText("");
                }
            }
            return o;
        } else {
            int minn = 4;
            for (int i = 0; i < 9; i++) {
                if (buttons[i].getText().equals("")) {
                    Button[] Copy = buttons;
                    Copy[i].setText("X");
                    int n = computerPlay(Copy, k + 1, o);
                    if (n < minn) {
                        minn = n;
                    }
                    Copy[i].setText("");
                }
            }
            return minn;
        }
    }

    public int checkTree() {
        int num = 3;
        check(false);
        if (XWin) {
            num = 1;
        } else if (!OWin) {
            num = 2;
        }
        XWin = false;
        OWin = false;
        return num;
    }

    public void xWins(int a, int b, int c) {

        System.out.println("X win:" + a + " " + b + " " + c);
        buttons[a].getStyleClass().clear();
        buttons[b].getStyleClass().clear();
        buttons[c].getStyleClass().clear();

        buttons[a].getStyleClass().add("button-book1");
        buttons[b].getStyleClass().add("button-book1");
        buttons[c].getStyleClass().add("button-book1");


        for (Button button : buttons) {
            button.setDisable(true); // Không cho ấn thêm
        }

        if (playModeLabel.getText().equals(PLAY_WITH_COMPUTER)) {
            CustomerAlter.showMessage("Tao nhường mày thắng");
        } else {
            CustomerAlter.showMessage("X thắng");
        }

    }

    public void oWins(int a, int b, int c) {

        buttons[a].getStyleClass().clear();
        buttons[b].getStyleClass().clear();
        buttons[c].getStyleClass().clear();

        buttons[a].getStyleClass().add("button-book1");
        buttons[b].getStyleClass().add("button-book1");
        buttons[c].getStyleClass().add("button-book1");

        for (Button button : buttons) {
            button.setDisable(true); // Không cho ấn thêm
        }
        if (playModeLabel.getText().equals(PLAY_WITH_COMPUTER)) {
            CustomerAlter.showMessage("tao thắng");
        } else {
            CustomerAlter.showMessage("O thắng");
        }
    }
}
