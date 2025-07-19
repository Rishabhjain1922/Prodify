package main.java.productivity.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtil {
    public static void switchTo(String fxmlFile, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource("/" + fxmlFile));
            Scene scene = new Scene(loader.load());

            // Always apply the stylesheet
            String cssPath = SceneUtil.class.getResource("/style.css").toExternalForm();
            scene.getStylesheets().clear(); // Clear any existing stylesheets
            scene.getStylesheets().add(cssPath);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}