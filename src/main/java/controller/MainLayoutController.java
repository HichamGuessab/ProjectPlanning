package controller;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.ViewAndController;
import service.ViewLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    private BorderPane mainPane;
    private Node mainView;
    private Object mainViewController;
    private MainController mainController = MainController.getInstance();
    @FXML
    private Button themeButton;

    private boolean lightTheme = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeView("loginPage");
        themeButton.setOnAction(actionEvent -> changeTheme());
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

    public void applyThemeToNodeAndChildren(Node node, String themeCssFile) {
        node.getScene().getStylesheets().clear();
        node.getScene().getStylesheets().add(themeCssFile);

        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                applyThemeToNodeAndChildren(child, themeCssFile);
            }
        }
    }

    public void changeTheme() {
        Scene scene = anchorPane.getScene();
        if (scene != null) {
            lightTheme = !lightTheme;
            String themeCssFile = lightTheme ? getClass().getResource("/lightTheme.css").toExternalForm() : getClass().getResource("/darkTheme.css").toExternalForm();
            applyThemeToNodeAndChildren(scene.getRoot(), themeCssFile);
        }
    }

    public void openModalWindow(String windowName, String fxmlFileName) throws IOException {
        ViewAndController viewAndController = ViewLoader.getViewAndController(fxmlFileName);
        Object controller = viewAndController.controller;
        Scene scene = new Scene((AnchorPane) viewAndController.node);
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(windowName);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                enableMainWindow();
            }
        });

        stage.show();
        mainController.getRootAnchorPane().getStyle();
        disableMainWindow();
    }

    public void disableMainWindow(){
        mainController.getRootAnchorPane().setStyle("-fx-background-color: rgba(0, 0, 0, 0.8)");
    }

    public void enableMainWindow(){
        mainController.getRootAnchorPane().setStyle("-fx-background-color: rgba(0, 0, 0, 0.05)");
    }
}
