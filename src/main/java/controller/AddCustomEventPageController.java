package controller;

import entity.CustomEvent;
import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.CalendarUrl;
import model.ViewAndController;
import net.fortuna.ical4j.model.Calendar;
import service.*;
import service.eventComponentStylizer.EventComponentStylizer;
import service.persister.customEvent.CustomEventPersister;
import service.persister.customEvent.CustomEventPersisterJSON;
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

public class AddCustomEventPageController implements Initializable {
    @FXML
    private TextField customEventNameTextField;
    @FXML
    private TextField customEventDescriptionTextField;
    @FXML
    private TextField customEventLocationTextField;
    @FXML
    private ColorPicker customEventColorPicker;
    @FXML
    private Button addCustomEventButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private VBox formVBox;
    @FXML
    private DatePicker customEventDayDatePicker;
    @FXML
    private TextField customEventStartTimeTextField;
    @FXML
    private TextField customEventEndTimeTextField;
    private String errorMessage = "";
    private Label errorMessageLabel = null;
    private CustomEvent customEvent;
    private MainController mainController = MainController.getInstance();
    private Calendar calendar = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CalendarsManager calendarsManager = new CalendarsManager(
                new UserRetrieverJSON(),
                new LocationRetrieverJSON(),
                new PromotionRetrieverJSON()
        );
        try {
            calendar = CalendarRetriever.retrieve(new URL(UserManager.getInstance().getCurrentUser().getCalendarUrl()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Link onFieldChange function to name field change event
        customEventNameTextField.textProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        customEventDescriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        customEventLocationTextField.textProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        customEventColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        customEventDayDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        customEventStartTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
        customEventEndTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> onFieldChange());
    }

    @FXML
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

        int timePeriod = customEventDayDatePicker.getValue().getDayOfYear();
        List<Event> events = new ArrayList<>(CalendarFilterer.getEventsForDayOfYear(calendar, timePeriod));
        events.add(buildCustomEvent());

        calendarController.setEvents(events);
        calendarController.setTimePeriod(timePeriod);
        gridPane.add(viewAndController.node, 1, 0);
        try {
            calendarController.displayEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddCustomEventButtonClick() {
        addCustomEventButton.setDisable(true);
        if(!verifyFields()) {
            showErrorMessage();
            return;
        }
        formVBox.getChildren().remove(errorMessageLabel);
        CustomEventPersister customEventPersister = new CustomEventPersisterJSON();
        if(customEventPersister.persist(customEvent)) {
            System.out.println("Custom event persisted");
        } else {
            System.err.println("Error while persisting custom event");
        }
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) customEventNameTextField.getScene().getWindow();
        mainController.enableMainWindow();
        stage.close();
    }

    private boolean verifyFields() {
        if(customEventNameTextField.getText().isEmpty()) {
            errorMessage = "Le nom de l'événement ne peut pas être vide";
            return false;
        }
        if(customEventDescriptionTextField.getText().isEmpty()) {
            errorMessage = "La description de l'événement ne peut pas être vide";
            return false;
        }
        if(customEventLocationTextField.getText().isEmpty()) {
            errorMessage = "La localisation de l'événement ne peut pas être vide";
            return false;
        }
        if(customEventColorPicker.getValue() == null) {
            errorMessage = "La couleur de l'événement ne peut pas être vide";
            return false;
        }
        if(customEventDayDatePicker.getValue() == null) {
            errorMessage = "La date de l'événement ne peut pas être vide";
            return false;
        }
        errorMessage = TimeFieldsVerificator.getErrorMessage(customEventStartTimeTextField.getText(), customEventEndTimeTextField.getText());
        if(!errorMessage.isEmpty()) {
            return false;
        }
        return true;
    }

    private void showErrorMessage() {
        formVBox.getChildren().remove(errorMessageLabel);
        Label label = new Label();
        label.setWrapText(true);
        label.setText(errorMessage);
        label.setTextFill(Color.rgb(255, 0, 0));
        label.setVisible(true);
        List<Node> vboxChildren = formVBox.getChildren();
        int customEventNameTextFieldIndex = vboxChildren.indexOf(customEventNameTextField);
        vboxChildren.add(customEventNameTextFieldIndex, label);
        errorMessageLabel = label;
        addCustomEventButton.setDisable(false);
    }

    private CustomEvent buildCustomEvent() {
        Date startDate = new Date(customEventDayDatePicker.getValue().getYear() - 1900, customEventDayDatePicker.getValue().getMonthValue() - 1, customEventDayDatePicker.getValue().getDayOfMonth(), Integer.parseInt(customEventStartTimeTextField.getText().split(":")[0]), Integer.parseInt(customEventStartTimeTextField.getText().split(":")[1]));
        Date endDate = new Date(customEventDayDatePicker.getValue().getYear() - 1900, customEventDayDatePicker.getValue().getMonthValue() - 1, customEventDayDatePicker.getValue().getDayOfMonth(), Integer.parseInt(customEventEndTimeTextField.getText().split(":")[0]), Integer.parseInt(customEventEndTimeTextField.getText().split(":")[1]));
        customEvent = new CustomEvent(
                "HYPERPLANING",
                new Date(),
                new Date(),
                UniqueIdentifierCreator.create(),
                startDate,
                endDate,
                customEventNameTextField.getText(),
                customEventLocationTextField.getText(),
                customEventDescriptionTextField.getText(),
                UserManager.getInstance().getCurrentUser().getIdentifier(),
                customEventColorPicker.getValue().toString()
        );
        return customEvent;
    }
}
