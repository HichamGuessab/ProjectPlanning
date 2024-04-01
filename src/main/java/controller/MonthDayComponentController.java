package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MonthDayComponentController {
    @FXML
    private Label dayLabel;

    private int dayOfMonth = 0;

    public void setDay(int day) {
        this.dayOfMonth = day;
        dayLabel.setText(String.valueOf(day));
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }
}
