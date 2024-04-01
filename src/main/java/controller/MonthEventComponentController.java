package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MonthEventComponentController {
    @FXML
    private Label eventNameLabel;

    public void setEventName(String eventName) {
        eventNameLabel.setText(eventName);
    }
}
