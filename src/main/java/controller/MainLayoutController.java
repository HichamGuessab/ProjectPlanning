package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import model.ViewAndController;
import service.ViewLoader;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {
    @FXML
    private BorderPane mainPane;
    private Node mainView;
    private Object mainViewController;
    private MainController mainController = MainController.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeView("loginPage");
    }

    public void changeView(String viewName) {
        if (mainView == null) {
            // Premier affichage
            try {
                ViewAndController viewAndController = ViewLoader.getViewAndController(viewName);
                mainViewController = viewAndController.controller;
                mainView = viewAndController.node;

                mainPane.setCenter(mainView);
            } catch (Exception e) {
                System.err.println("ChangeView error for first page loading :" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Tous les affichages suivants
            FadeTransition fadeTransitionOut = new FadeTransition(Duration.millis(200), mainView);
            fadeTransitionOut.setFromValue(1);
            fadeTransitionOut.setToValue(0);
            fadeTransitionOut.play();
            fadeTransitionOut.setOnFinished(actionEvent -> {
                try {
                    ViewAndController viewAndController = ViewLoader.getViewAndController(viewName);
                    mainViewController = viewAndController.controller;
                    mainView = viewAndController.node;
                    FadeTransition fadeTransitionIn = new FadeTransition(Duration.millis(200), mainView);
                    fadeTransitionIn.setFromValue(0);
                    fadeTransitionIn.setToValue(1);
                    fadeTransitionIn.play();

                    mainPane.setCenter(mainView);
                } catch (Exception e) {
                    System.err.println("ChangeView error for second page loading :" + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }
}
