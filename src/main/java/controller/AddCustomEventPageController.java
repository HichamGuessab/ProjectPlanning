package controller;

import entity.CustomEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.EventType;
import model.ViewAndController;
import service.UserManager;
import service.ViewLoader;
import service.eventComponentStylizer.EventComponentStylizer;
import service.persister.customEvent.CustomEventPersister;
import service.persister.customEvent.CustomEventPersisterJSON;

import java.io.IOException;
import java.net.URL;
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

    private ViewAndController previewViewAndController;
    private String errorMessage = "";
    private Label errorMessageLabel = null;
    private CustomEvent customEvent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewAndController viewAndController = null;
        try {
            previewViewAndController = ViewLoader.getViewAndController("eventComponent");
        } catch (IOException e) {
            System.err.println("Error loading event preview.");
            e.printStackTrace();
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
        updatePreview();
    }

    private void updatePreview() {
        if(previewViewAndController == null) {
            return;
        }

        if(!verifyFields()) {
            showErrorMessage();
            return;
        }
        formVBox.getChildren().remove(errorMessageLabel);

        gridPane.getChildren().removeIf(node -> !(node instanceof VBox));

        EventComponentController eventComponentController = (EventComponentController) previewViewAndController.controller;

        customEvent = buildCustomEvent();
        EventComponentStylizer eventComponentStylizer = new EventComponentStylizer();
        eventComponentStylizer.applyStyleToEventComponentController(customEvent, eventComponentController);
        eventComponentController.updatePopupContent();

        gridPane.add(previewViewAndController.node, 1, 0);
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
        customEventPersister.persist(customEvent);
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
        if(customEventStartTimeTextField.getText().isEmpty()) {
            errorMessage = "L'heure de début de l'événement ne peut pas être vide";
            return false;
        }
        if(customEventEndTimeTextField.getText().isEmpty()) {
            errorMessage = "L'heure de fin de l'événement ne peut pas être vide";
            return false;
        }
        if(!isStringCorrectTimeFormat(customEventStartTimeTextField.getText())) {
            return false;
        }
        if(!isStringCorrectTimeFormat(customEventEndTimeTextField.getText())) {
            return false;
        }
        return true;
    }

    private boolean isStringCorrectTimeFormat(String time) {
        if(!time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            errorMessage = "Le temps doit être au format HH:MM";
            return false;
        }
        String[] timeParts = time.split(":");
        int minutes = Integer.parseInt(timeParts[1]);
        if(minutes % 30 != 0) {
            errorMessage = "Les minutes doivent être 00 ou 30";
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
        return new CustomEvent(
                "HYPERPLANING",
                new Date(),
                new Date(),
                createUniqueIdentifier(),
                startDate,
                endDate,
                customEventNameTextField.getText(),
                customEventLocationTextField.getText(),
                customEventDescriptionTextField.getText(),
                UserManager.getInstance().getCurrentUser().getIdentifier(),
                customEventColorPicker.getValue().toString()
        );
    }

    private String createUniqueIdentifier() {
        return UserManager.getInstance().getCurrentUser().getIdentifier() + System.currentTimeMillis();
    }
}
