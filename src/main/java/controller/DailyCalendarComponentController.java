package controller;

import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import model.ViewAndController;
import service.ViewLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DailyCalendarComponentController implements CalendarController {
    @FXML
    private GridPane calendarGridPane;

    private List<Event> events;

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void displayEvents() throws IOException {
        for (Event event : events) {
            int startHour = event.getStart().getHours();
            // Only display events within calendar range
            if(startHour < 8 || startHour > 20) {
                continue;
            }

            ViewAndController viewAndController = ViewLoader.getViewAndController("eventComponent");
            EventComponentController eventComponentController = (EventComponentController) viewAndController.controller;
            eventComponentController.setType(event.getCategory());
            eventComponentController.setSubject(event.getSummary());
            eventComponentController.setRoom(event.getLocation());

            int yStartCoordinates = (startHour - 8)*2+(event.getStart().getMinutes()/30);
            int yEndCoordinates = (event.getEnd().getHours() - 8)*2+(event.getEnd().getMinutes()/30);
            calendarGridPane.add(viewAndController.node, 0, yStartCoordinates, 1, yEndCoordinates-yStartCoordinates);
        }
    }
}
