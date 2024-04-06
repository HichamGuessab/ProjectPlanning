package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonthEventComponentController implements Initializable {
    @FXML
    private Label nameLabel;

    @FXML
    private AnchorPane anchorPane;

    private Popup popup;
    private Color backgroundColor = null;
    private String name;
    private String type;
    private String location;
    private String popupName;
    private String popupType;
    private String popupLocation;
    private final List<Map<String, String>> additionalInformations = new ArrayList<>();

    private boolean isMouseOverPopup = false;

    private VBox content;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePopup();
        anchorPane.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showPopup();
            } else {
                hidePopupWithDelay();
            }
        });
    }

    public void updatePopupContent() {
        content.getChildren().clear();

        List<Node> nodes = new ArrayList<>();

        if(popupName != null && !popupName.isEmpty()) {
            Label nameLabel = new Label(popupName);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #334;");
            nodes.add(nameLabel);
        }
        if(popupType != null && !popupType.isEmpty()) {
            Label typeLabel = new Label(popupType);
            typeLabel.setStyle("-fx-text-fill: #667;");
            nodes.add(typeLabel);
        }
        if(popupLocation != null && !popupLocation.isEmpty()) {
            Label locationLabel = new Label(popupLocation);
            locationLabel.setStyle("-fx-text-fill: #667;");
            nodes.add(locationLabel);
        }

        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        for (Map<String, String> information : additionalInformations) {
            for (Map.Entry<String, String> entry : information.entrySet()) {
                Matcher matcher = pattern.matcher(entry.getValue());
                Node node;
                if(matcher.find()) {
                    String link = matcher.group(1);
                    String entryValue = entry.getValue().replace("[" + link + "]", "");
                    Hyperlink hyperlink = new Hyperlink(entry.getKey() + " : " + entryValue);
                    hyperlink.setOnAction(e -> {
                        if(MainController.getHostServices() != null) {
                            MainController.getHostServices().showDocument("mailto:" + link);
                        }
                    });
                    node = hyperlink;
                } else {
                    node = new Label(entry.getKey() + " : " + entry.getValue());
                }
                node.setStyle("-fx-text-fill: #667;");
                nodes.add(node);
            }
        }

        for (Node node : nodes) {
            content.getChildren().add(node);
        }
    }

    private void initializePopup() {
        popup = new Popup();
        content = new VBox(10);
        content.setStyle("-fx-padding: 10;" +
                "-fx-border-color: #333;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0.0, 0.0);");
        content.setMaxWidth(300);
        content.setMaxHeight(200);
        popup.getContent().add(content);

        content.setOnMouseEntered(e -> isMouseOverPopup = true);
        content.setOnMouseExited(e -> {
            isMouseOverPopup = false;
            hidePopupWithDelay();
        });
    }

    private void showPopup() {
        if (!popup.isShowing()) {
            updatePopupContent();
            popup.show(anchorPane.getScene().getWindow(),
                    anchorPane.localToScreen(anchorPane.getBoundsInLocal()).getMinX(),
                    anchorPane.localToScreen(anchorPane.getBoundsInLocal()).getMinY() - content.getHeight());
        }
    }

    private synchronized void hidePopupWithDelay() {
        new Thread(() -> {
            try {
                Thread.sleep(100);
                if (!isMouseOverPopup) {
                    javafx.application.Platform.runLater(() -> popup.hide());
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    public void setName(String name) {
        this.name = name;
        nameLabel.setText(name);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPopupName(String popupName) {
        this.popupName = popupName;
    }

    public void setPopupType(String popupType) {
        this.popupType = popupType;
    }

    public void setPopupLocation(String popupLocation) {
        this.popupLocation = popupLocation;
    }

    public void addAdditionalInformation(Map<String, String> information) {
        additionalInformations.add(information);
    }
    public void setBackGroundColor(Color color) {
        this.backgroundColor = color;
        anchorPane.setStyle(
                "-fx-background-color: rgba(" +
                        (int) (backgroundColor.getRed() * 255) + "," +
                        (int) (backgroundColor.getGreen() * 255) + "," +
                        (int) (backgroundColor.getBlue() * 255) + ",0.5" + ");"
        );
        anchorPane.getStyleClass().add("month-event");
    }
}
