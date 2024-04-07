package controller;

import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import service.DayEventComponentBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WeeklyCalendarComponentController extends AbstractCalendarController implements Initializable {
    public AnchorPane weeklyAnchorPane;
    @FXML
    private GridPane weeklyCalendarGridPane;

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
            dayEventComponentBuilder.buildDay(weeklyCalendarGridPane, i, 1, 26, currentDayEvents);
            for (int hour = 8; hour <= 19; hour++) {
                int rowIndex = (hour - 8) * 2 + 2;
                if (rowIndex > 0 && rowIndex < weeklyCalendarGridPane.getRowConstraints().size()) {
                    Pane line = new Pane();
                    line.getStyleClass().add("hour-separator");
                    weeklyCalendarGridPane.add(line, 1, rowIndex, GridPane.REMAINING, 1); // Span across all columns
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
