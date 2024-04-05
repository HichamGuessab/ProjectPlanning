package controller;

import entity.CourseEvent;
import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.ViewAndController;
import service.ViewLoader;
import service.eventComponentStylizer.EventComponentStylizer;

import java.io.IOException;
import java.util.List;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DailyCalendarComponentController extends AbstractCalendarController {
    @FXML
    private GridPane calendarGridPane;
    @FXML
    private Label dayLabel;

    public void displayEvents() throws IOException {
        if(this.events == null) {
            return;
        }
        for (Event event : events) {
            int startHour = event.getStart().getHours();
            int endHour = event.getEnd().getHours();

            if(startHour < 8) {
                startHour = 8;
            }
            if(endHour > 18 || endHour < 8) {
                endHour = 19;
            }

            ViewAndController viewAndController = ViewLoader.getViewAndController("eventComponent");
            EventComponentController eventComponentController = (EventComponentController) viewAndController.controller;
            EventComponentStylizer eventComponentStylizer = new EventComponentStylizer();
            eventComponentStylizer.applyStyleToEventComponentController(event, eventComponentController);

            int yStartCoordinates = (startHour - 8)*2+(event.getStart().getMinutes()/30);
            int yEndCoordinates = (endHour - 8)*2+(event.getEnd().getMinutes()/30);
            calendarGridPane.add(viewAndController.node, 1, yStartCoordinates+1, 1, yEndCoordinates-yStartCoordinates);
        }
    }

    public void setTimePeriod(int dayOfYear) {
        Date date = dayOfYearToDate(dayOfYear);
        LocalDate localDate = date.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDate();
        String dateInFrench = localDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.FRENCH));
        this.dayLabel.setText(dateInFrench);
    }

    private Date dayOfYearToDate(int dayOfYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return calendar.getTime();
    }
}
