package com.ooops.lms.util;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class ThemeManager {
    private static ThemeManager instance = null;
    private String currentTheme = DEFAULT_THEME;

    private static final String DEFAULT_THEME = "/style/UserMenuStyle.css";
    private static final String DARK_THEME = "/style/UserMenuDarkStyle.css";
    private static final String PINK_THEME = "/style/UserMenuPinkStyle.css";
    private static final String GOLD_THEME = "/style/UserMenuGoldStyle.css";

    private static final List<Pane> panes = new ArrayList<>();

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void applyTheme(VBox scene) {
        URL cssURL = getClass().getResource(currentTheme);
        if (cssURL == null) {
            System.out.println("khong ton tai css style nay!");
            return;
        }
        for(int i = 0;i<panes.size();i++) {
            panes.get(i).getStylesheets().clear();
            panes.get(i).getStylesheets().add(cssURL.toExternalForm());
        }
    }

    public void changeTheme(String mode) {
        switch (mode.toLowerCase()) {
            case "dark":
                currentTheme = DARK_THEME;
                break;
            case "pink" :
                currentTheme = PINK_THEME;
                break;
            case "gold" :
                currentTheme = GOLD_THEME;
                break;
            default:
                currentTheme = DEFAULT_THEME;
                break;
        }
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void addPane(Pane pane) {
        if (!panes.contains(pane)) {
            panes.add(pane);
            pane.getStylesheets().clear();
            pane.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());
        }
    }
}
