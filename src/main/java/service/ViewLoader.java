package service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import main.Main;
import model.ViewAndController;

import java.io.IOException;

public class ViewLoader {

    /**
     * Load the view and controller from the fxml file
     * @param fxmlFileName
     * @return ViewAndController
     * @throws IOException
     */
    public static ViewAndController getViewAndController(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/"+fxmlFileName+".fxml"));
        Node node = loader.load();
        Object controller = loader.getController();
        return new ViewAndController(node, controller);
    }
}
