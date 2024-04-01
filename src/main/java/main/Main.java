package main;

import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        MainController controller = MainController.getInstance();
        controller.start(stage);
        controller.setHostServices(getHostServices());
    }
}
