package controller;

import entity.CourseEvent;
import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.ViewAndController;
import service.ViewLoader;
import service.eventComponentStylizer.EventComponentStylizer;

import java.io.IOException;
import java.net.URL;
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
        for (Event event : events) {
            int dayOfWeek = event.getStart().getDay();
            if(dayOfWeek == 0) {
                dayOfWeek = 7;
            }
            dayOfWeek -= 1;

            int startHour = event.getStart().getHours();
            int endHour = event.getEnd().getHours();

            // Only display events within calendar range
            if(dayOfWeek > 5) {
                continue;
            }

            if(startHour < 8) {
                startHour = 8;
            }
            if(endHour > 20 || endHour < 8) {
                endHour = 19;
            }

            ViewAndController viewAndController = ViewLoader.getViewAndController("eventComponent");
            EventComponentController eventComponentController = (EventComponentController) viewAndController.controller;
            EventComponentStylizer eventComponentStylizer = new EventComponentStylizer();
            eventComponentStylizer.applyStyleToEventComponentController(event, eventComponentController);

            int yStartCoordinates = ((startHour - 8) * 2) + calculateAdjustment(event.getStart().getMinutes());
            int yEndCoordinates = ((endHour - 8) * 2) + calculateAdjustment(event.getEnd().getMinutes());
            calendarGridPane.add(viewAndController.node, dayOfWeek + 1, yStartCoordinates + 1, 1, yEndCoordinates - yStartCoordinates);

            for (int hour = startHour; hour <= endHour; hour++) {
                int rowIndex = (hour - 8) * 2;
                if (rowIndex > 0 && rowIndex < calendarGridPane.getRowConstraints().size()) {
                    Pane line = new Pane();
                    line.getStyleClass().add("hour-separator");
                    calendarGridPane.add(line, 1, rowIndex, GridPane.REMAINING, 1); // Span across all columns
                }
            }
        }
    }

    private int calculateAdjustment(int minutes) {
        int adjustment = 0;
        if(minutes > 15 && minutes < 45) {
            adjustment = 1;
        }
        if(minutes >= 45) {
            adjustment = 2;
        }
        return adjustment;
    }
}
