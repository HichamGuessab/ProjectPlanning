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

        this.events = CalendarFilterer.getCurrentWeekEvents(this.calendar);

        this.viewMode = ViewModes.WEEKLY;
        updateCalendarView();
    }

    private void updateCalendarView() {
        String calendarComponentName = "";
        switch (this.viewMode) {
            case WEEKLY:
                calendarComponentName = "weeklyCalendarComponent";
                break;
            case DAILY:
                calendarComponentName = "dailyCalendarComponent";
                break;
        }
        ViewAndController viewAndController = null;
        try {
            viewAndController = ViewLoader.getViewAndController(calendarComponentName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(viewAndController == null) {
            System.err.println("Failed to load view and controller");
            return;
        }
        CalendarController calendarController = (CalendarController) viewAndController.controller;
        if(calendarController == null) {
            System.err.println("Failed to load controller");
            return;
        }
        calendarController.setEvents(this.events);
        calendarAnchorPane.getChildren().add(viewAndController.node);
        try {
            calendarController.displayEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
