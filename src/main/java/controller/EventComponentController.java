package controller;

import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

import static model.EventType.*;

import entity.CourseEvent;
import model.EventType;

public class EventComponentController implements Initializable {
    @FXML
    private Label type;

    @FXML
    private Label subject;

    @FXML
    private Label room;

    @FXML
    private AnchorPane anchorPane;

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

    public void setBackGroundColors(Event event) {
        Enum<EventType> courseType;
        if(event.getClass() == CourseEvent.class) {
            courseType = ((CourseEvent) event).getCourseType();
        } else {
            courseType = EventType.OTHER;
        }

        if (courseType.equals(CM)) {
            anchorPane.setStyle("-fx-background-color: rgba(255,0,0,0.5);"); // Red for CM
        } else if (courseType.equals(TD)) {
            anchorPane.setStyle("-fx-background-color: rgba(0,255,0,0.5);"); // Green for TD
        } else if (courseType.equals(TP)) {
            anchorPane.setStyle("-fx-background-color: rgba(0,0,255,0.5);"); // Blue for TP
        } else if (courseType.equals(EVALUATION)) {
            anchorPane.setStyle("-fx-background-color: rgba(255,255,0,0.5);"); // Yellow for EVALUATION
        } else if (courseType.equals(OTHER)){
            anchorPane.setStyle("-fx-background-color: rgba(133,133,133,0.5);"); // White for unknown type
        }
    }
}
