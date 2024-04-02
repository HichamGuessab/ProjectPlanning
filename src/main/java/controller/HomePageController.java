package controller;

import entity.Event;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.CalendarType;
import model.CalendarUrl;
import model.ViewAndController;
import model.ViewModes;
import net.fortuna.ical4j.model.*;
import node.AutoCompleteTextField;
import service.*;
import service.retriever.calendar.CalendarRetriever;
import service.retriever.location.LocationRetriever;
import service.retriever.location.LocationRetrieverJSON;
import service.retriever.promotion.PromotionRetriever;
import service.retriever.promotion.PromotionRetrieverJSON;
import service.retriever.user.UserRetriever;
import service.retriever.user.UserRetrieverJSON;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    @FXML
    private AnchorPane calendarAnchorPane;
    @FXML
    private ChoiceBox<String> viewModeChoiceBox;
    @FXML
    private HBox topHBox;
    @FXML
    private TextField searchTextField;

    private Calendar calendar = null;
    private List<Event> events;
    private ViewModes viewMode = ViewModes.WEEKLY;
    private int timePeriod = 0;
    private final UserManager userManager = UserManager.getInstance();
    private CalendarsManager calendarsManager;
    private CalendarType calendarType = CalendarType.USER;
    private MainController mainController = MainController.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User currentUser = userManager.getCurrentUser();
        if(currentUser == null) {
            return;
        }
        try {
            this.calendar = CalendarRetriever.retrieve(new URL(currentUser.getCalendarUrl()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        LocationRetriever locationRetriever = new LocationRetrieverJSON();
        UserRetriever userRetriever = new UserRetrieverJSON();
        PromotionRetriever promotionRetriever = new PromotionRetrieverJSON();
        calendarsManager = new CalendarsManager(userRetriever, locationRetriever, promotionRetriever);

        viewModeChoiceBox.getItems().addAll("Jour", "Semaine", "Mois");
        viewModeChoiceBox.setOnAction(event -> handleViewModeChange());
        selectWeeklyView();
        initSearchTextField();
    }

    private void initSearchTextField() {
        AutoCompleteTextField autoCompleteTextField = new AutoCompleteTextField();

        List<String> suggestions = new ArrayList<>(calendarsManager.getAllCalendarNames());

        autoCompleteTextField.setSuggestions(suggestions);

        autoCompleteTextField.setLayoutX(searchTextField.getLayoutX());
        autoCompleteTextField.setLayoutY(searchTextField.getLayoutY());
        autoCompleteTextField.setPrefWidth(searchTextField.getPrefWidth());
        autoCompleteTextField.setPrefHeight(searchTextField.getPrefHeight());
        autoCompleteTextField.setPromptText(searchTextField.getPromptText());
        autoCompleteTextField.setMaxWidth(searchTextField.getMaxWidth());
        autoCompleteTextField.setMaxHeight(searchTextField.getMaxHeight());
        autoCompleteTextField.setMinWidth(searchTextField.getMinWidth());
        autoCompleteTextField.setMinHeight(searchTextField.getMinHeight());
        autoCompleteTextField.setId(searchTextField.getId());
        autoCompleteTextField.getStyleClass().addAll(searchTextField.getStyleClass());
        autoCompleteTextField.setAlignment(searchTextField.getAlignment());
        autoCompleteTextField.setFocusTraversable(false);

        topHBox.getChildren().remove(searchTextField);
        topHBox.getChildren().add(0, autoCompleteTextField);

        searchTextField = autoCompleteTextField;
        autoCompleteTextField.setOnAction(event -> onSearchFieldSubmit());
    }

    @FXML
    private void onAddCalendarButtonClick() {
        mainController.changeView("addCalendarPage");
    }

    private void onSearchFieldSubmit() {
        String selectedCalendarName = searchTextField.getText();
        if(selectedCalendarName == null || selectedCalendarName.isEmpty()) {
            return;
        }

        CalendarUrl calendarUrl = calendarsManager.getCalendarUrlAndTypeFromName(selectedCalendarName);
        if(calendarUrl == null) {
            return;
        }
        calendarType = calendarUrl.type;
        try {
            this.calendar = CalendarRetriever.retrieve(new URL(calendarUrl.url));
            switch (viewMode) {
                case DAILY -> selectDailyView();
                case WEEKLY -> selectWeeklyView();
                case MONTHLY -> selectMonthlyView();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void onPreviousTimePeriodButtonClick() {
        timePeriod--;
        updateCalendarView();
    }

    @FXML
    private void onNextTimePeriodButtonClick() {
        timePeriod++;
        updateCalendarView();
    }

    private void handleViewModeChange() {
        String selectedViewMode = viewModeChoiceBox.getValue();
        if(selectedViewMode == null) {
            return;
        }
        switch (selectedViewMode) {
            case "Jour":
                selectDailyView();
                break;
            case "Semaine":
                selectWeeklyView();
                break;
            case "Mois":
                selectMonthlyView();
                break;
        }
    }

    private void selectWeeklyView() {
        viewModeChoiceBox.setValue("Semaine");
        timePeriod = java.util.Calendar.getInstance().get(java.util.Calendar.WEEK_OF_YEAR);
        viewMode = ViewModes.WEEKLY;

        updateCalendarView();
    }

    private void selectDailyView() {
        viewModeChoiceBox.setValue("Jour");
        viewMode = ViewModes.DAILY;
        timePeriod = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR);
        updateCalendarView();
    }

    private void selectMonthlyView() {
        viewModeChoiceBox.setValue("Mois");
        timePeriod = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)+1;
        viewMode = ViewModes.MONTHLY;
        updateCalendarView();
    }

    private void updateEvents() {
        switch (this.viewMode) {
            case DAILY:
                events = CalendarFilterer.getEventsForDayOfYear(this.calendar, timePeriod);
                break;
            case WEEKLY:
                events = CalendarFilterer.getEventsForWeekOfYear(this.calendar, timePeriod);
                break;
            case MONTHLY:
                events = CalendarFilterer.getEventsForMonthOfYear(this.calendar, timePeriod);
                break;
        }
    }

    private String getCalendarComponentName() {
        switch (this.viewMode) {
            case DAILY:
                return "dailyCalendarComponent";
            case WEEKLY:
                return "weeklyCalendarComponent";
            case MONTHLY:
                return "monthlyCalendarComponent";
            default:
                return null;
        }
    }

    private void removeCurrentCalendarView() {
        calendarAnchorPane.getChildren().clear();
    }

    private void updateCalendarView() {
        updateEvents();
        removeCurrentCalendarView();
        ViewAndController viewAndController = null;
        try {
            viewAndController = ViewLoader.getViewAndController(getCalendarComponentName());
        } catch (IOException e) {
            System.err.println("Failed to load calendar component : "+e.getMessage());
        }
        if(viewAndController == null) {
            return;
        }
        CalendarController calendarController = (CalendarController) viewAndController.controller;
        if(calendarController == null) {
            System.err.println("Failed to load controller");
            return;
        }
        calendarController.setEvents(this.events);
        calendarController.setTimePeriod(timePeriod);
        calendarAnchorPane.getChildren().add(viewAndController.node);
        try {
            calendarController.displayEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
