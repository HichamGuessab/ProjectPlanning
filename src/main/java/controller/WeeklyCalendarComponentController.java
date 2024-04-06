package controller;

import entity.CourseEvent;
import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.ViewAndController;
import service.DayEventComponentBuilder;
import service.ViewLoader;
import service.eventComponentStylizer.EventComponentStylizer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WeeklyCalendarComponentController extends AbstractCalendarController implements Initializable {
    @FXML
    private GridPane calendarGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void displayEvents() throws IOException {
        if(this.events == null) {
            return;
        }

        DayEventComponentBuilder dayEventComponentBuilder = new DayEventComponentBuilder();

        for(int i=1; i<6; i++) {
            List<Event> currentDayEvents = getAllEventsForDayOfWeek(i);
            dayEventComponentBuilder.buildDay(calendarGridPane, i, 1, 24, currentDayEvents);
            for (int hour = 8; hour <= 19; hour++) {
                int rowIndex = (hour - 8) * 2;
                if (rowIndex > 0 && rowIndex < calendarGridPane.getRowConstraints().size()) {
                    Pane line = new Pane();
                    line.getStyleClass().add("hour-separator");
                    calendarGridPane.add(line, 1, rowIndex, GridPane.REMAINING, 1); // Span across all columns
                }
            }
        }
    }

    private List<Event> getAllEventsForDayOfWeek(int dayOfWeek) {
        List<Event> eventsForDayOfWeek = new ArrayList<>();
        for (Event event : events) {
            int eventDay = event.getStart().getDay();
            if(eventDay == 0) {
                eventDay = 7;
            }
            if(eventDay == dayOfWeek) {
                eventsForDayOfWeek.add(event);
            }
        }
        return eventsForDayOfWeek;
    }
}
