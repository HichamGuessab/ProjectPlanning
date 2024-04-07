package controller;

import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.ViewAndController;
import service.ViewLoader;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MonthlyCalendarComponentController extends AbstractCalendarController implements Initializable {
    public AnchorPane monthlyAnchorPane;
    @FXML
    private Label currentMonthLabel;
    private GridPane monthlyCalendarGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private List<MonthDayComponentController> monthDayComponentControllers = new ArrayList<>();

    private void putDayComponentsInGridPane() {
        monthDayComponentControllers = new ArrayList<>();
        int numberOfDaysInMonth = java.time.YearMonth.of(java.time.Year.now().getValue(), timePeriod+1).lengthOfMonth();
        for (int i = 0; i < numberOfDaysInMonth; i++) {
            int dayOfMonth = i + 1;
            ViewAndController viewAndController = null;
            try {
                viewAndController = ViewLoader.getViewAndController("monthDayComponent");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(viewAndController == null) {
                return;
            }

            int[] coordinates = calculateGridCoordinatesForDayOfMonth(dayOfMonth);

            if(coordinates[0] > 4) {
                continue;
            }

            MonthDayComponentController controller = (MonthDayComponentController) viewAndController.controller;
            controller.setDay(dayOfMonth);
            monthDayComponentControllers.add(controller);
            monthlyCalendarGridPane.add(viewAndController.node, coordinates[0], coordinates[1]);
        }
    }

    private int[] calculateGridCoordinatesForDayOfMonth(int dayOfMonth) {
        int firstDayOfWeekOfMonth = java.time.LocalDate.of(java.time.Year.now().getValue(), timePeriod+1, 1).getDayOfWeek().getValue();

        firstDayOfWeekOfMonth -= 1;
        int day = dayOfMonth + firstDayOfWeekOfMonth - 1;
        int y = (day / 7)+1;
        int x = day % 7;
        int[] coordinates = new int[2]; // x, y
        coordinates[0] = x;
        coordinates[1] = y;
        return coordinates;
    }

    private MonthDayComponentController getDayComponentController(int dayOfMonth) {
        for (MonthDayComponentController controller : monthDayComponentControllers) {
            if (controller.getDayOfMonth() == dayOfMonth) {
                return controller;
            }
        }
        return null;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, timePeriod);
        // Display the current month in french language in currentMonthLabel
        currentMonthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.FRANCE).toUpperCase());

        putDayComponentsInGridPane();
    }

    public void displayEvents() throws IOException {
        if(this.events == null) {
            return;
        }
        for (Event event : events) {
            Date start = event.getStart();
            int dayOfMonth = start.getDate();
            MonthDayComponentController controller = getDayComponentController(dayOfMonth);
            if (controller != null) {
                controller.addEvent(event);
            }
        }
    }
}
