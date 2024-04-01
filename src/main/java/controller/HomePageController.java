package controller;

import entity.Config;
import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Calendar calendar = null;
    private List<Event> events;
    private ViewModes viewMode = ViewModes.WEEKLY;
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

        selectMonthlyView();
    }

    private void selectWeeklyView() {
        this.viewMode = ViewModes.WEEKLY;
        int currentWeekOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.WEEK_OF_YEAR);
        updateCalendarView("weeklyCalendarComponent", currentWeekOfYear);
    }

    private void selectDailyView() {
        this.viewMode = ViewModes.DAILY;
        int currentDayOfMonth = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);
        updateCalendarView("dailyCalendarComponent",currentDayOfMonth);
    }

    private void selectMonthlyView() {
        this.viewMode = ViewModes.MONTHLY;
        int currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        updateCalendarView("monthlyCalendarComponent", currentMonth+1);
    }

    private void updateCalendarView(String calendarComponentName, int timePeriod) {
        ViewAndController viewAndController = null;
        try {
            viewAndController = ViewLoader.getViewAndController(calendarComponentName);
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
