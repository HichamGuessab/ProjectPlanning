package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import service.DayEventComponentBuilder;

import java.io.IOException;
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

        DayEventComponentBuilder dayEventComponentBuilder = new DayEventComponentBuilder();
        dayEventComponentBuilder.buildDay(calendarGridPane, 1, 1, 26, events);

        for (int hour = 8; hour <= 19; hour++) {
            Pane hourSeparator = new Pane();
            hourSeparator.getStyleClass().add("hour-separator");
            int rowIndex = (hour - 8) * 2 + 2;
            calendarGridPane.add(hourSeparator, 1, rowIndex, GridPane.REMAINING, 1);
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
