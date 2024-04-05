package controller;

import entity.CustomEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.EventType;
import model.ViewAndController;
import service.UserManager;
import service.ViewLoader;
import service.eventComponentStylizer.EventComponentStylizer;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
    private Pane customEventPreviewPane;
    @FXML
    private VBox formVBox;
    @FXML
    private DatePicker customEventDayDatePicker;

    private ViewAndController previewViewAndController;

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

    }

    @FXML
    private void onFieldChange() {
        updatePreview();
    }

    private void updatePreview() {
        if(previewViewAndController == null) {
            return;
        }

        EventComponentController eventComponentController = (EventComponentController) previewViewAndController.controller;

        CustomEvent customEvent = buildCustomEvent();
        EventComponentStylizer eventComponentStylizer = new EventComponentStylizer();
        eventComponentStylizer.applyStyleToEventComponentController(customEvent, eventComponentController);
        eventComponentController.updatePopupContent();

        customEventPreviewPane.getChildren().clear();
        customEventPreviewPane.getChildren().add(previewViewAndController.node);
    }

    private CustomEvent buildCustomEvent() {
        return new CustomEvent(
                "HYPERPLANING",
                new Date(),
                new Date(),
                createUniqueIdentifier(),
                new Date(),
                new Date(),
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
