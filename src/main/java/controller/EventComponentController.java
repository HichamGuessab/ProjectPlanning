package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class EventComponentController implements Initializable {
    @FXML
    private Label type;

    @FXML
    private Label subject;

    @FXML
    private Label room;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setType(String type) {
        this.type.setText(type);
    }

    public void setSubject(String subject) {
        this.subject.setText(subject);
    }

    public void setRoom(String room) {
        this.room.setText(room);
    }

    public String getType() {
        return this.type.getText();
    }

    public String getSubject() {
        return this.subject.getText();
    }

    public String getRoom() {
        return this.room.getText();
    }

//    public void setBackGroundColors(CourseEvent event) {
//        Enum<CourseType> courseType = event.getCourseType();
//
//        if (courseType.equals(CM)) {
//            rootPane.setStyle("-fx-background-color: #ff0000;"); // Red for CM
//        } else if (courseType.equals(TD)) {
//            rootPane.setStyle("-fx-background-color: #00ff00;"); // Green for TD
//        } else if (courseType.equals(TP)) {
//            rootPane.setStyle("-fx-background-color: #0000ff;"); // Blue for TP
//        } else if (courseType.equals(EVALUATION)) {
//            rootPane.setStyle("-fx-background-color: #ffff00;"); // Yellow for EVALUATION
//        } else {
//            rootPane.setStyle("-fx-background-color: #ffffff;"); // White for unknown type
//        }
//    }
}
