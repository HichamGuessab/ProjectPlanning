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

public class DailyCalendarComponentController extends AbstractCalendarController {
    @FXML
    private GridPane calendarGridPane;

    public void displayEvents() throws IOException {
        for (Event event : events) {
            System.out.println("Event: " + event.getSummary());

            int startHour = event.getStart().getHours();
            int endHour = event.getEnd().getHours();

            System.out.println("Start hour: " + startHour);
            System.out.println("End hour: " + endHour);

            if(startHour < 8) {
                startHour = 8;
            }
            if(endHour > 18 || endHour < 8) {
                endHour = 19;
            }

            ViewAndController viewAndController = ViewLoader.getViewAndController("eventComponent");
            EventComponentController eventComponentController = (EventComponentController) viewAndController.controller;
            eventComponentController.setType(event.getCategory());
            eventComponentController.setSubject(event.getSummary());
            eventComponentController.setRoom(event.getLocation());

            int yStartCoordinates = (startHour - 8)*2+(event.getStart().getMinutes()/30);
            int yEndCoordinates = (endHour - 8)*2+(event.getEnd().getMinutes()/30);
            calendarGridPane.add(viewAndController.node, 1, yStartCoordinates+1, 1, yEndCoordinates-yStartCoordinates);
        }
    }
}
