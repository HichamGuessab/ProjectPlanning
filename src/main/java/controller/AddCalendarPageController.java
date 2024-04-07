package controller;

import entity.Location;
import entity.Promotion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.CalendarType;
import service.persister.location.LocationPersister;
import service.persister.location.LocationPersisterJSON;
import service.persister.promotion.PromotionPersister;
import service.persister.promotion.PromotionPersisterJSON;
import service.retriever.calendar.CalendarRetriever;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddCalendarPageController implements Initializable {
    @FXML
    private TextField calendarUrlTextField;
    @FXML
    private TextField calendarNameTextField;
    @FXML
    private ChoiceBox<String> calendarTypeChoiceBox;
    @FXML
    private VBox vbox;
    @FXML
    private Button addCalendarButton;
    @FXML
    private Button backButton;

    private Label errorMessageLabel = null;
    private String errorMessage = "";
    private MainController mainController = MainController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendarTypeChoiceBox.getItems().addAll("Salle", "Formation");
        calendarTypeChoiceBox.setValue("Salle");
        backButton.setOnAction(actionEvent -> mainController.changeView("homePage"));
    }

    @FXML
    private void onAddCalendarButtonClicked() {
        addCalendarButton.setDisable(true);
        String calendarUrl = calendarUrlTextField.getText();
        String calendarName = calendarNameTextField.getText();
        CalendarType calendarType = switch (calendarTypeChoiceBox.getValue()) {
            case "Salle" -> CalendarType.LOCATION;
            case "Formation" -> CalendarType.PROMOTION;
            default -> null;
        };
        if(!verifyFields(calendarUrl, calendarName, calendarType)) {
            showErrorMessage();
            return;
        }

        boolean persistenceOk = false;
        switch (Objects.requireNonNull(calendarType)) {
            case LOCATION -> {
                LocationPersister locationPersister = new LocationPersisterJSON();
                persistenceOk = locationPersister.persist(new Location(calendarName, calendarUrl));
            }
            case PROMOTION -> {
                PromotionPersister promotionPersister = new PromotionPersisterJSON();
                persistenceOk = promotionPersister.persist(new Promotion(calendarName, calendarUrl));
            }
        }

        if(!persistenceOk) {
            errorMessage = "Erreur lors de la sauvegarde du calendrier";
            showErrorMessage();
            return;
        }

        mainController.changeView("homePage");
    }

    private boolean verifyFields(String calendarUrl, String calendarName, CalendarType calendarType) {
        boolean calendarRetrievable = false;
        try {
            calendarRetrievable = CalendarRetriever.retrieve(new URL(calendarUrl)) != null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        boolean fieldsOk = true;

        if(!calendarRetrievable) {
            errorMessage = "Impossible de récupérer le calendrier à l'URL spécifiée";
            fieldsOk = false;
        } else if (calendarName == null || calendarName.isEmpty()) {
            errorMessage = "Le nom du calendrier ne peut pas être vide";
            fieldsOk = false;
        } else if (calendarType == null) {
            errorMessage = "Le type de calendrier n'est pas valide";
            fieldsOk = false;

        }

        return fieldsOk;
    }

    private void showErrorMessage() {
        vbox.getChildren().remove(errorMessageLabel);
        Label label = new Label();
        label.setText(errorMessage);
        label.setTextFill(Color.rgb(255, 0, 0));
        label.setVisible(true);
        List<Node> vboxChildren = vbox.getChildren();
        int calendarNameTextFieldIndex = vboxChildren.indexOf(calendarNameTextField);
        vboxChildren.add(calendarNameTextFieldIndex, label);
        errorMessageLabel = label;
        addCalendarButton.setDisable(false);
    }
}
