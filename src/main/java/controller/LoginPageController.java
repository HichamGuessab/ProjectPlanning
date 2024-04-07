package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Themes;
import service.ThemeManager;
import service.UserManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable, ThemeApplyer {

    private final MainController mainController = MainController.getInstance();

    @FXML
    private TextField identifierTextField;
    @FXML
    private VBox vbox;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private AnchorPane anchorPane;

    private ThemeManager themeManager = ThemeManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // When enter key pressed, onLoginButtonClick is called
        identifierTextField.setOnAction(event -> onLoginButtonClick());
        passwordTextField.setOnAction(event -> onLoginButtonClick());
    }

    @FXML
    public void onLoginButtonClick() {
        this.removeLabelsInVbox();
        UserManager userManager = UserManager.getInstance();
        if(!userManager.authenticate(identifierTextField.getText(), passwordTextField.getText())) {
            Label label = new Label();
            label.setText("Identifiant ou mot de passe incorrect");
            label.setTextFill(Color.rgb(255, 0, 0));
            label.setVisible(true);
            List<Node> vboxChildren = vbox.getChildren();
            int identifierTextFieldIndex = vboxChildren.indexOf(identifierTextField);
            vboxChildren.add(identifierTextFieldIndex, label);
            return;
        }

        this.mainController.changeView("homePage");
    }

    private void removeLabelsInVbox() {
        List<Node> vboxChildren = vbox.getChildren();
        vboxChildren.removeIf(vboxChild -> vboxChild instanceof Label);
    }

    @Override
    public void applyTheme(Color[] colors, Themes theme) {
        anchorPane.getStylesheets().clear();
        if(theme == Themes.LIGHT) {
            anchorPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } else {
            anchorPane.getStylesheets().add(getClass().getResource("/styleDark.css").toExternalForm());
        }
    }
}
