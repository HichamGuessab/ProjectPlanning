package controller;

import entity.Config;
import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import model.ViewAndController;
import model.ViewModes;
import net.fortuna.ical4j.model.*;
import service.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    @FXML
    private AnchorPane calendarAnchorPane;
    @FXML
    private ChoiceBox<String> viewModeChoiceBox;

    private Calendar calendar = null;
    private List<Event> events;
    private ViewModes viewMode = ViewModes.WEEKLY;
    private int timePeriod = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Config config = ConfigRetriever.retrieve();
        if(config == null) {
            return;
        }
        try {
            this.calendar = CalendarRetriever.retrieve(new URL(config.getCalendarUrl()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        viewModeChoiceBox.getItems().addAll("Jour", "Semaine", "Mois");
        viewModeChoiceBox.setOnAction(event -> handleViewModeChange());
        selectWeeklyView();
    }

    @FXML
    private void onPreviousTimePeriodButtonClick() {
        timePeriod--;
        updateCalendarView();
    }

    @FXML
    private void onNextTimePeriodButtonClick() {
        timePeriod++;
        updateCalendarView();
    }

    private void handleViewModeChange() {
        String selectedViewMode = viewModeChoiceBox.getValue();
        if(selectedViewMode == null) {
            return;
        }
        switch (selectedViewMode) {
            case "Jour":
                selectDailyView();
                break;
            case "Semaine":
                selectWeeklyView();
                break;
            case "Mois":
                selectMonthlyView();
                break;
        }
    }

    private void selectWeeklyView() {
        viewModeChoiceBox.setValue("Semaine");
        timePeriod = java.util.Calendar.getInstance().get(java.util.Calendar.WEEK_OF_YEAR);
        viewMode = ViewModes.WEEKLY;

        updateCalendarView();
    }

    private void selectDailyView() {
        viewModeChoiceBox.setValue("Jour");
        viewMode = ViewModes.DAILY;
        timePeriod = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR);
        updateCalendarView();
    }

    private void selectMonthlyView() {
        viewModeChoiceBox.setValue("Mois");
        timePeriod = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)+1;
        viewMode = ViewModes.MONTHLY;
        updateCalendarView();
    }

    private void updateEvents() {
        switch (this.viewMode) {
            case DAILY:
                events = CalendarFilterer.getEventsForDayOfYear(this.calendar, timePeriod);
                break;
            case WEEKLY:
                events = CalendarFilterer.getEventsForWeekOfYear(this.calendar, timePeriod);
                break;
            case MONTHLY:
                events = CalendarFilterer.getEventsForMonthOfYear(this.calendar, timePeriod);
                break;
        }
    }

    private String getCalendarComponentName() {
        switch (this.viewMode) {
            case DAILY:
                return "dailyCalendarComponent";
            case WEEKLY:
                return "weeklyCalendarComponent";
            case MONTHLY:
                return "monthlyCalendarComponent";
            default:
                return null;
        }
    }

    private void removeCurrentCalendarView() {
        calendarAnchorPane.getChildren().clear();
    }

    private void updateCalendarView() {
        updateEvents();
        removeCurrentCalendarView();
        ViewAndController viewAndController = null;
        try {
            viewAndController = ViewLoader.getViewAndController(getCalendarComponentName());
        } catch (IOException e) {
            System.err.println("Failed to load calendar component : "+e.getMessage());
        }
        if(viewAndController == null) {
            return;
        }
        CalendarController calendarController = (CalendarController) viewAndController.controller;
        if(calendarController == null) {
            System.err.println("Failed to load controller");
            return;
        }
        calendarController.setEvents(this.events);
        calendarController.setTimePeriod(timePeriod);
        calendarAnchorPane.getChildren().add(viewAndController.node);
        try {
            calendarController.displayEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
