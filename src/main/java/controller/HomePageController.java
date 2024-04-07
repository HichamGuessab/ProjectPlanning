package controller;

import entity.CourseEvent;
import entity.Event;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.CalendarType;
import model.CalendarUrl;
import model.ViewAndController;
import model.ViewModes;
import model.*;
import javafx.scene.control.Button;
import net.fortuna.ical4j.model.Calendar;
import node.AutoCompleteTextField;
import service.*;
import service.retriever.calendar.CalendarRetriever;
import service.retriever.courseEvent.CourseEventRetriever;
import service.retriever.courseEvent.CourseEventRetrieverJSON;
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
    public HBox searchHBox;
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
    @FXML
    private Button dependentActionButton;

    private Calendar calendar = null;
    private List<Event> events;
    private ViewModes viewMode = ViewModes.WEEKLY;
    private int timePeriod = 0;
    private final UserManager userManager = UserManager.getInstance();
    private CalendarsManager calendarsManager;
    private CalendarType calendarType = CalendarType.USER;
    private String calendarName;
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
        updateDependentActionButtonText();
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

        searchHBox.getChildren().remove(searchTextField);
        searchHBox.getChildren().add(1, autoCompleteTextField);

        searchTextField = autoCompleteTextField;
        autoCompleteTextField.setOnAction(event -> onSearchFieldSubmit());
    }

    @FXML
    private void onDependentActionButtonClick() throws IOException {
        switch (calendarType) {
            case USER -> mainController.openModalWindow("Ajout d'évènement", "addCustomEventPage");
            case LOCATION -> {
                mainController.setLocationReservation(calendarName);
                mainController.openModalWindow("Réserver une salle", "addCourseEventPage");
            }
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
        calendarName = selectedCalendarName;
        calendarType = calendarUrl.type;
        updateDependentActionButtonText();
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

    private void updateDependentActionButtonText() {
        dependentActionButton.setVisible(true);
        switch (calendarType) {
            case USER -> {
                dependentActionButton.setText("Ajouter un évènement");
            }
            case LOCATION -> {
                if(userManager.getCurrentUser().getType() == UserType.TEACHER) {
                    dependentActionButton.setText("Réserver une salle");
                } else {
                    dependentActionButton.setVisible(false);
                }
            }
        }
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
        timePeriod = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
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
        CourseEventRetriever courseEventRetriever = new CourseEventRetrieverJSON();

        List<Event> customEvents = new ArrayList<>();
        customEvents.addAll(customEventRetriever.retrieveFromUserIdentifier(userManager.getCurrentUser().getIdentifier()));
        newEvents.addAll(CalendarFilterer.filterEventsByPeriod(this.viewMode, timePeriod, customEvents));

        List<Event> courseEvents = new ArrayList<>();
        switch (calendarType) {
            case USER -> {
                courseEvents.addAll(courseEventRetriever.retrieveFromTeacherName(userManager.getCurrentUser().getName()));
            }
            case LOCATION -> {
                courseEvents.addAll(courseEventRetriever.retrieveFromLocationName(calendarName));
            }
        }
        newEvents.addAll(CalendarFilterer.filterEventsByPeriod(this.viewMode, timePeriod, courseEvents));

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
