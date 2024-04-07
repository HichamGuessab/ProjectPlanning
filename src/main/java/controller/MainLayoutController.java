package controller;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.Main;
import model.Themes;
import model.ViewAndController;
import service.ThemeManager;
import service.UserManager;
import service.ViewLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable, ThemeApplyer {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button disconnectButton;
    @FXML
    private ChoiceBox<Themes> themeChoiceBox;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private StackPane layoutStackPane;

    private Node mainView;
    private Object mainViewController;
    private MainController mainController = MainController.getInstance();
    private UserManager userManager = UserManager.getInstance();
    private ThemeManager themeManager = ThemeManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeView("loginPage");

        themeManager.registerThemeApplyer(this);
        initDisconnectButton();
        initThemeChoiceBox();
    }

    private void initThemeChoiceBox() {
        themeChoiceBox.getItems().addAll(Arrays.asList(Themes.values()));
        themeChoiceBox.setValue(Themes.LIGHT);
        themeChoiceBox.setOnAction(actionEvent -> {
            themeManager.applyTheme(themeChoiceBox.getValue());
        });
    }

    private void initDisconnectButton() {
        try {
            URL url = Main.class.getResource("/images/icons/logout.png");
            ImageView imageView = new ImageView(new Image(url.toString()));
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(15);
            disconnectButton.setGraphic(imageView);
        } catch (Exception e) {
            System.err.println("Error while setting the disconnect button icon: " + e.getMessage());
            disconnectButton.setText("Disconnect");
        }

        disconnectButton.setOnAction(actionEvent -> {
            userManager.disconnect();
            changeView("loginPage");
        });
    }

    public void changeView(String viewName) {
        if (mainView == null) {
            // Premier affichage
            try {
                ViewAndController viewAndController = ViewLoader.getViewAndController(viewName);
                mainViewController = viewAndController.controller;
                System.out.println("MainViewController: " + mainViewController.getClass().getName());
                if(mainViewController instanceof ThemeApplyer) {
                    themeManager.registerThemeApplyer((ThemeApplyer) mainViewController);
                }
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

                    if(mainViewController instanceof ThemeApplyer) {
                        themeManager.removeThemeApplyer((ThemeApplyer) mainViewController);
                    }
                    mainViewController = viewAndController.controller;
                    if(mainViewController instanceof ThemeApplyer) {
                        themeManager.registerThemeApplyer((ThemeApplyer) mainViewController);
                    }

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

    public void openModalWindow(String windowName, String fxmlFileName) throws IOException {
        ViewAndController viewAndController = ViewLoader.getViewAndController(fxmlFileName);
        Object controller = viewAndController.controller;
        if(controller instanceof ThemeApplyer) {
            themeManager.registerThemeApplyer((ThemeApplyer) controller);
        }
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
                if(controller instanceof ThemeApplyer) {
                    themeManager.removeThemeApplyer((ThemeApplyer) controller);
                }
                enableMainWindow();
            }
        });

        stage.show();
        mainController.getRootAnchorPane().getStyle();
        disableMainWindow();
    }

    public void disableMainWindow(){
        layoutStackPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8)");
    }

    public void enableMainWindow(){
        layoutStackPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0)");
    }

    @Override
    public void applyTheme(Color[] colors, Themes theme) {
        anchorPane.getStylesheets().clear();
        if(theme == Themes.LIGHT) {
            anchorPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } else {
            anchorPane.getStylesheets().add(getClass().getResource("/styleDark.css").toExternalForm());
        }
    }
}
