package controller;

import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import service.DayEventComponentBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WeeklyCalendarComponentController extends AbstractCalendarController implements Initializable {
    @FXML
    private GridPane weeklyCalendarGridPane;
    @FXML
    private Label mondayLabel;
    @FXML
    private Label tuesdayLabel;
    @FXML
    private Label wednesdayLabel;
    @FXML
    private Label thursdayLabel;
    @FXML
    private Label fridayLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void displayEvents() throws IOException {
        initDayLabels();
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

    private void initDayLabels() {
        int weekOfYear = timePeriod;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date monday = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date tuesday = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date wednesday = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date thursday = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date friday = calendar.getTime();

        mondayLabel.setText("Lundi " + monday.getDate() + "/" + ((monday.getMonth() + 1)<10 ? "0":"")  + (monday.getMonth() + 1));
        tuesdayLabel.setText("Mardi " + tuesday.getDate() + "/" + ((tuesday.getMonth() + 1)<10 ? "0":"") + (tuesday.getMonth() + 1));
        wednesdayLabel.setText("Mercredi " + wednesday.getDate() + "/" + ((wednesday.getMonth() + 1)<10 ? "0":"") + (wednesday.getMonth() + 1));
        thursdayLabel.setText("Jeudi " + thursday.getDate() + "/" + ((thursday.getMonth() + 1)<10 ? "0":"") + (thursday.getMonth() + 1));
        fridayLabel.setText("Vendredi " + friday.getDate() + "/" + ((friday.getMonth() + 1)<10 ? "0":"") + (friday.getMonth() + 1));
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
