package controller;

import entity.CourseEvent;
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
            if(endHour > 18 || endHour < 8) {
                endHour = 19;
            }

            ViewAndController viewAndController = ViewLoader.getViewAndController("eventComponent");
            EventComponentController eventComponentController = (EventComponentController) viewAndController.controller;
            eventComponentController.setEvent(event);
            if(event.getClass() == CourseEvent.class) {
                eventComponentController.setType(((CourseEvent) event).getCourseType().toString());
                eventComponentController.setName(((CourseEvent) event).getName());
                eventComponentController.setRoom(event.getLocation());
            } else {
                eventComponentController.setType(event.getNameBySummary());
                eventComponentController.setName(event.getSummary());
                eventComponentController.setRoom(event.getLocation());
            }
            eventComponentController.setBackGroundColors(event);

            int yStartCoordinates = (startHour - 8)*2+(event.getStart().getMinutes()/30);
            int yEndCoordinates = (endHour - 8)*2+(event.getEnd().getMinutes()/30);
            calendarGridPane.add(viewAndController.node, dayOfWeek+1, yStartCoordinates+1, 1, yEndCoordinates-yStartCoordinates);
        }
    }
}
