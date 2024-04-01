package controller;

import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;

import java.io.IOException;

public class MainController {
    private static MainController INSTANCE;
    private Stage root;
    private AnchorPane rootAnchorPane;
    private MainLayoutController mainLayoutController;

    private static HostServices hostServices;

    private MainController() {

    }

    public static MainController getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new MainController();
        }
        return INSTANCE;
    }

    public void start(Stage stage) throws IOException {
        root = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/mainLayout.fxml"));
        this.rootAnchorPane = loader.load();
        mainLayoutController = loader.getController();
        Scene scene = new Scene(rootAnchorPane);
        root.setTitle("EDT Turboflex");
        root.setScene(scene);
        root.setMaximized(false); // TODO : set to true
        root.show();
    }

    public void changeView(String scene) {
        this.mainLayoutController.changeView(scene);
    }

    public void setHostServices(HostServices hostServices) {
        MainController.hostServices = hostServices;
    }

    public static HostServices getHostServices() {
        return hostServices;
    }
}
