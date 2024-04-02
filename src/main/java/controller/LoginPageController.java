package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import service.UserManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {

    private final MainController mainController = MainController.getInstance();

    @FXML
    private TextField identifierTextField;
    @FXML
    private VBox vbox;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
}
