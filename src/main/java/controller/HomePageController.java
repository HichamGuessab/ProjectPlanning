package controller;

import entity.CourseEvent;
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
import net.fortuna.ical4j.model.Calendar;
import node.AutoCompleteTextField;
import service.*;
import service.retriever.calendar.CalendarRetriever;
import service.retriever.customEvent.CustomEventRetriever;
import service.retriever.customEvent.CustomEventRetrieverJSON;
import service.retriever.location.LocationRetriever;
import service.retriever.location.LocationRetrieverJSON;
import service.retriever.promotion.PromotionRetriever;
import service.retriever.promotion.PromotionRetrieverJSON;
import service.retriever.user.UserRetriever;
import service.retriever.user.UserRetrieverJSON;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomePageController implements Initializable {
    @FXML
    private AnchorPane calendarAnchorPane;
    @FXML
    private ChoiceBox<String> viewModeChoiceBox;
    @FXML
    private HBox topHBox;
    @FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> nameFilterChoiceBox;
    @FXML
    private ChoiceBox<String> locationFilterChoiceBox;
    @FXML
    private ChoiceBox<String> promotionFilterChoiceBox;
    @FXML
    private ChoiceBox<String> courseTypeFilterChoiceBox;

    private Calendar calendar = null;
    private List<Event> events;
    private ViewModes viewMode = ViewModes.WEEKLY;
    private int timePeriod = 0;
    private final UserManager userManager = UserManager.getInstance();
    private CalendarsManager calendarsManager;
    private CalendarType calendarType = CalendarType.USER;
    private MainController mainController = MainController.getInstance();
    private final String allFilterTag = "Tous";

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
        viewModeChoiceBox.setOnAction(event -> updateView());

        List<CourseEvent> allEvents = CalendarFilterer.getAllCourseEvents(calendar);
        nameFilterChoiceBox.getItems().addAll(FiltersManager.getCourseNames(allEvents));
        nameFilterChoiceBox.getItems().add(0, allFilterTag);
        nameFilterChoiceBox.setValue(allFilterTag);
        nameFilterChoiceBox.setOnAction(event -> updateCalendarView());
        locationFilterChoiceBox.getItems().addAll(FiltersManager.getLocations(allEvents));
        locationFilterChoiceBox.getItems().add(0, allFilterTag);
        locationFilterChoiceBox.setValue(allFilterTag);
        locationFilterChoiceBox.setOnAction(event -> updateCalendarView());
        promotionFilterChoiceBox.getItems().addAll(FiltersManager.getPromotions(allEvents));
        promotionFilterChoiceBox.getItems().add(0, allFilterTag);
        promotionFilterChoiceBox.setValue(allFilterTag);
        promotionFilterChoiceBox.setOnAction(event -> updateCalendarView());
        courseTypeFilterChoiceBox.getItems().addAll(FiltersManager.getCourseTypes(allEvents));
        courseTypeFilterChoiceBox.getItems().add(0, allFilterTag);
        courseTypeFilterChoiceBox.setValue(allFilterTag);
        courseTypeFilterChoiceBox.setOnAction(event -> updateCalendarView());

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
    private void onDependentActionButtonClick() throws IOException {
        switch (calendarType) {
            case USER -> mainController.openModalWindow("Ajout d'évènement", "addCustomEventPage");
        }
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

    private void updateView() {
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
        List<Event> newEvents = new ArrayList<>();
        switch (this.viewMode) {
            case DAILY -> newEvents.addAll(CalendarFilterer.getEventsForDayOfYear(this.calendar, timePeriod));
            case WEEKLY -> newEvents.addAll(CalendarFilterer.getEventsForWeekOfYear(this.calendar, timePeriod));
            case MONTHLY -> newEvents.addAll(CalendarFilterer.getEventsForMonthOfYear(this.calendar, timePeriod));
        }
        CustomEventRetriever customEventRetriever = new CustomEventRetrieverJSON();
        newEvents.addAll(customEventRetriever.retrieveFromUserIdentifier(userManager.getCurrentUser().getIdentifier()));
        events = CalendarFilterer.filterEvents(getFilters(), newEvents);
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

    private Map<String, String> getFilters() {
        Map<String, String> filters = new HashMap<>();
        if(!nameFilterChoiceBox.getValue().equals(allFilterTag)) {
            filters.put("name", nameFilterChoiceBox.getValue());
        }
        if(!locationFilterChoiceBox.getValue().equals(allFilterTag)) {
            filters.put("location", locationFilterChoiceBox.getValue());
        }
        if(!promotionFilterChoiceBox.getValue().equals(allFilterTag)) {
            filters.put("promotions", promotionFilterChoiceBox.getValue());
        }
        if(!courseTypeFilterChoiceBox.getValue().equals(allFilterTag)) {
            filters.put("courseType", courseTypeFilterChoiceBox.getValue());
        }
        return filters;
    }
}
