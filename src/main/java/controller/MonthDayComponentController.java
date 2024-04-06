package controller;

import entity.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.ViewAndController;
import service.ViewLoader;

public class MonthDayComponentController {
    @FXML
    private Label dayLabel;
    @FXML
    private VBox eventVBox;

    private int dayOfMonth = 0;

    public void setDay(int day) {
        this.dayOfMonth = day;
        dayLabel.setText(String.valueOf(day));
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void addEvent(Event event) {
        ViewAndController viewAndController = null;
        try {
            viewAndController = ViewLoader.getViewAndController("monthEventComponent");
        } catch (Exception e) {
            e.printStackTrace();
        }

        MonthEventComponentController controller = (MonthEventComponentController) viewAndController.controller;

        controller.setEventName(event.getNameBySummary());
        eventVBox.getChildren().add(viewAndController.node);
    }
}
