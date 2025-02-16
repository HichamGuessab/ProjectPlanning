package controller;

import entity.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.ViewAndController;
import service.ViewLoader;
import service.monthEventComponentStylizer.MonthEventComponentStylizer;

public class MonthDayComponentController {
    @FXML
    private Label dayLabel;
    @FXML
    private VBox eventVBox;
    @FXML
    private ScrollPane eventScrollPane;

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

        MonthEventComponentController monthEventComponentController = (MonthEventComponentController) viewAndController.controller;
        MonthEventComponentStylizer monthEventComponentStylizer = new MonthEventComponentStylizer();
        monthEventComponentStylizer.applyStyleToEventComponentController(event, monthEventComponentController);
        monthEventComponentController.setName(event.getNameBySummary());
        eventVBox.getChildren().add(viewAndController.node);
        adjustScrollBarVisibility();
    }

    private void adjustScrollBarVisibility() {
        boolean needsScroll = (eventVBox.getChildren().size() > 2);
        eventScrollPane.setVbarPolicy(needsScroll ? ScrollPane.ScrollBarPolicy.AS_NEEDED : ScrollPane.ScrollBarPolicy.NEVER);
    }
}
