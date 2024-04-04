package controller;

import entity.Event;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.EventType.*;

import entity.CourseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import main.Main;
import model.EventType;

public class EventComponentController implements Initializable {
    @FXML
    private Label name;

    @FXML
    private Label type;

    @FXML
    private Label room;

    @FXML
    private AnchorPane anchorPane;

    private Event event;

    private Popup popup;
    private VBox content;
    private boolean isMouseOverPopup = false;

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

    public void setEvent(Event event) {
        this.event = event;
    }

    public void updatePopupContent() {
        content.getChildren().clear();

        Label nameLabel = new Label("");
        Label typeLabel = new Label("");
        Label roomLabel = new Label("");
        Hyperlink teacherLabel = new Hyperlink("");
        Label promotionsLabel = new Label("");
        Label formationsLabel = new Label("");

        if (event != null && event.getClass() == CourseEvent.class) {
            CourseEvent courseEvent = (CourseEvent) event;
            nameLabel.setText("Matière : " + courseEvent.getName());
            typeLabel.setText("Type : " + courseEvent.getCourseType());
            roomLabel.setText("Salle : " + courseEvent.getLocation());
            teacherLabel.setText("Enseignant : " + courseEvent.getTeacher());
            teacherLabel.setOnAction(e -> {
                if(MainController.getHostServices() != null) {
                    MainController.getHostServices().showDocument("mailto:" + courseEvent.getTeacherEmail());
                }
            });
            promotionsLabel.setText("Promotions : " + String.join(", ", courseEvent.getPromotions()));
            formationsLabel.setText("Formations : " + String.join(", ", courseEvent.getFormations()));

            teacherLabel.setStyle("-fx-text-fill: #666;");
            promotionsLabel.setStyle("-fx-text-fill: #666;");
            formationsLabel.setStyle("-fx-text-fill: #666;");

            content.getChildren().addAll(nameLabel, typeLabel, roomLabel, teacherLabel, promotionsLabel, formationsLabel);
        } else {
            nameLabel.setText("Matière : " + getName());
            typeLabel.setText("Type : " + getType());
            roomLabel.setText("Salle : " + getLocation());

            content.getChildren().addAll(nameLabel, typeLabel, roomLabel);
        }

        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        typeLabel.setStyle("-fx-text-fill: #666;");
        roomLabel.setStyle("-fx-text-fill: #666;");
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

    public void setType(String type) {
        this.type.setText(type);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setRoom(String room) {
        this.room.setText(room);
    }

    public String getType() {
        return this.type.getText();
    }

    public String getName() {
        return this.name.getText();
    }

    public String getLocation() {
        return this.room.getText();
    }

    public void setBackGroundColors(Event event) {
        Enum<EventType> courseType;
        if(event.getClass() == CourseEvent.class) {
            courseType = ((CourseEvent) event).getCourseType();
        } else {
            courseType = EventType.OTHER;
        }

        if (courseType.equals(CM)) {
            anchorPane.setStyle("-fx-background-color: rgba(255,220,60,0.5);"); // Red for CM
        } else if (courseType.equals(TD)) {
            anchorPane.setStyle("-fx-background-color: rgba(0,255,255,0.5);"); // Green for TD
        } else if (courseType.equals(TP)) {
            anchorPane.setStyle("-fx-background-color: rgba(32,32,255,0.5);"); // Blue for TP
        } else if (courseType.equals(EVALUATION)) {
            anchorPane.setStyle("-fx-background-color: rgba(255,0,0,0.5);"); // Yellow for EVALUATION
        } else if (courseType.equals(OTHER)){
            anchorPane.setStyle("-fx-background-color: rgba(133,133,133,0.5);"); // White for unknown type
        }
    }
}
