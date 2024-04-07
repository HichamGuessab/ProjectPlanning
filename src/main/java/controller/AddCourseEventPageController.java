package controller;

import entity.CourseEvent;
import entity.CustomEvent;
import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.CalendarUrl;
import model.EventType;
import model.ViewAndController;
import net.fortuna.ical4j.model.Calendar;
import service.*;
import service.persister.courseEvent.CourseEventPersister;
import service.persister.courseEvent.CourseEventPersisterJSON;
import service.retriever.calendar.CalendarRetriever;
import service.retriever.location.LocationRetrieverJSON;
import service.retriever.promotion.PromotionRetrieverJSON;
import service.retriever.user.UserRetrieverJSON;
import service.verification.TimeFieldsVerificator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddCourseEventPageController implements Initializable {
    @FXML
    private TextField courseEventNameTextField;
    @FXML
    private ChoiceBox<String> courseEventTypeChoiceBox;
    @FXML
    private ChoiceBox<String> courseEventPromotionChoiceBox;
    @FXML
    private Button addCourseEventPromotionButton;
    @FXML
    private VBox customEventPromotionsVBox;
    @FXML
    private DatePicker courseEventDayDatePicker;
    @FXML
    private TextField courseEventStartTimeTextField;
    @FXML
    private TextField courseEventEndTimeTextField;
    @FXML
    private Button addCourseEventButton;
    @FXML
    private VBox formVBox;
    @FXML
    private GridPane gridPane;
    private String errorMessage = "";
    private Label errorMessageLabel = null;
    private CourseEvent courseEvent;
    private MainController mainController = MainController.getInstance();
    private String location = "";
    private UserManager userManager = UserManager.getInstance();
    private List<String> promotions = new ArrayList<>();
    private Calendar calendar = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CalendarsManager calendarsManager = new CalendarsManager(
                new UserRetrieverJSON(),
                new LocationRetrieverJSON(),
                new PromotionRetrieverJSON()
        );

        this.location = mainController.getLocationReservation();
        CalendarUrl calendarUrl = calendarsManager.getCalendarUrlAndTypeFromName(this.location);
        try {
            calendar = CalendarRetriever.retrieve(new URL(calendarUrl.url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        List<CourseEvent> allCourseEvents = CalendarFilterer.getAllCourseEvents(calendar);

        courseEventNameTextField.textProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        courseEventTypeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        courseEventDayDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        courseEventStartTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        courseEventEndTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        addCourseEventPromotionButton.setOnAction(event -> onAddPromotionButtonCLick());

        List<String> eventTypes = new ArrayList<>();
        for(EventType eventType : EventType.values()) {
            eventTypes.add(eventType.toString());
        }
        courseEventTypeChoiceBox.getItems().addAll(eventTypes);
        courseEventPromotionChoiceBox.getItems().addAll(FiltersManager.getPromotions(allCourseEvents));

        addCourseEventButton.setOnAction(event -> onAddCourseEventButtonCLick());
    }

    private void onAddCourseEventButtonCLick() {
        addCourseEventButton.setDisable(true);
        if(!verifyFields()) {
            showErrorMessage();
            return;
        }
        formVBox.getChildren().remove(errorMessageLabel);

        CourseEventPersister courseEventPersister = new CourseEventPersisterJSON();
        courseEvent = buildCourseEvent();
        if(courseEventPersister.persist(courseEvent)) {
            System.out.println("Course event persisted");
        } else {
            System.err.println("Failed to persist course event");
        }
        closeWindow();
        addCourseEventButton.setDisable(false);
    }

    private void onAddPromotionButtonCLick() {
        String promotion = courseEventPromotionChoiceBox.getValue();
        if(promotion != null && !promotions.contains(promotion)) {
            promotions.add(promotion);

            HBox line = new HBox();
            line.setAlignment(javafx.geometry.Pos.CENTER);
            line.setSpacing(5);

            Label label = new Label(promotion);
            Button deleteButton = new Button("-");
            deleteButton.setOnAction(event -> {
                promotions.remove(promotion);
                customEventPromotionsVBox.getChildren().remove(line);
                onFieldChange();
            });

            line.getChildren().add(label);
            line.getChildren().add(deleteButton);

            customEventPromotionsVBox.getChildren().add(line);
            onFieldChange();
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private void onFieldChange() {
        if(!verifyFields()) {
            showErrorMessage();
            return;
        }
        formVBox.getChildren().remove(errorMessageLabel);
        updatePreview();
    }

    private void updatePreview() {
        ViewAndController viewAndController = null;
        try {
            viewAndController = ViewLoader.getViewAndController("dailyCalendarComponent");
        } catch (IOException e) {
            System.err.println("Failed to load preview calendar component : "+e.getMessage());
        }

        if(viewAndController == null || calendar == null) {
            return;
        }

        CalendarController calendarController = (CalendarController) viewAndController.controller;

        if(calendarController == null) {
            System.err.println("Failed to load controller");
            return;
        }

        int timePeriod = courseEventDayDatePicker.getValue().getDayOfYear();
        List<Event> events = new ArrayList<>(CalendarFilterer.getEventsForDayOfYear(calendar, timePeriod));
        events.add(buildCourseEvent());

        calendarController.setEvents(events);
        calendarController.setTimePeriod(timePeriod);
        gridPane.add(viewAndController.node, 1, 0);
        try {
            calendarController.displayEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CourseEvent buildCourseEvent() {
        Date startDate = new Date(courseEventDayDatePicker.getValue().getYear() - 1900, courseEventDayDatePicker.getValue().getMonthValue() - 1, courseEventDayDatePicker.getValue().getDayOfMonth(), Integer.parseInt(courseEventStartTimeTextField.getText().split(":")[0]), Integer.parseInt(courseEventStartTimeTextField.getText().split(":")[1]));
        Date endDate = new Date(courseEventDayDatePicker.getValue().getYear() - 1900, courseEventDayDatePicker.getValue().getMonthValue() - 1, courseEventDayDatePicker.getValue().getDayOfMonth(), Integer.parseInt(courseEventEndTimeTextField.getText().split(":")[0]), Integer.parseInt(courseEventEndTimeTextField.getText().split(":")[1]));

        String promotionsStr = String.join(",", promotions);
        String courseName = courseEventNameTextField.getText();
        String teacherName = userManager.getCurrentUser().getName();
        String courseType = courseEventTypeChoiceBox.getValue();

        String summary = courseName+" - "+ teacherName +" - \n" +
                " "+promotionsStr+" - "+courseType;

        String description = "Matière : "+courseName+"\\nEnseignant : " + teacherName + "\n" +
                courseType+" : "+promotionsStr+" " +
                "\nSalle : "+location+"\nType : "+courseType+"\n" +
                "X-ALT-DESC;FMTTYPE=text/html:Matière : "+promotionsStr+"<br/>" +
                "Enseignant : "+teacherName+"<br/>"+courseType+" : "+promotionsStr+"<br/>Salle : "+location+"<br/>Type : "+courseType+"<br/>";
        
        CourseEvent courseEvent = new CourseEvent(
                "HYPERPLANING",
                new Date(),
                new Date(),
                UniqueIdentifierCreator.create(),
                startDate,
                endDate,
                summary,
                location,
                description
        );
        return courseEvent;
    }

    private boolean verifyFields() {
        if(courseEventNameTextField.getText().isEmpty()) {
            errorMessage = "Le nom du cours ne peut pas être vide";
            return false;
        }
        if(courseEventTypeChoiceBox.getValue() == null) {
            errorMessage = "Le type decours ne peut pas être vide";
            return false;
        }
        if(promotions.isEmpty()) {
            errorMessage = "Le cours doit avoir au moins une promotion";
            return false;
        }
        if(courseEventDayDatePicker.getValue() == null) {
            errorMessage = "La date du cours ne peut pas être vide";
            return false;
        }
        errorMessage = TimeFieldsVerificator.getErrorMessage(courseEventStartTimeTextField.getText(), courseEventEndTimeTextField.getText());
        if(!errorMessage.isEmpty()) {
            return false;
        }
        return true;
    }

    private void closeWindow() {
        Stage stage = (Stage) courseEventNameTextField.getScene().getWindow();
        mainController.enableMainWindow();
        stage.close();
    }

    private void showErrorMessage() {
        formVBox.getChildren().remove(errorMessageLabel);
        Label label = new Label();
        label.setWrapText(true);
        label.setText(errorMessage);
        label.setTextFill(Color.rgb(255, 0, 0));
        label.setVisible(true);
        List<Node> vboxChildren = formVBox.getChildren();
        int courseEventNameTextFieldIndex = vboxChildren.indexOf(courseEventNameTextField);
        vboxChildren.add(courseEventNameTextFieldIndex, label);
        errorMessageLabel = label;
        addCourseEventButton.setDisable(false);
    }
}
