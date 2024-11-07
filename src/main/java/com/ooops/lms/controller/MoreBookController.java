package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class MoreBookController {

    @FXML
    VBox moreBookBox;

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            // Remove content if it's already in the scene graph
            if (moreBookBox.getChildren().contains(content)) {
                moreBookBox.getChildren().remove(content);
            }
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

}
