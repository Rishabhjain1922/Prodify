package main.java.productivity.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.productivity.model.UserSession;
import main.java.productivity.util.SceneUtil;
import static main.java.productivity.store.UserStore.validate;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (validate(user, pass)) {
            // Set both username and password in UserSession
            UserSession.getInstance().setCredentials(user, pass);
            SceneUtil.switchTo("dashboard.fxml", (Stage) usernameField.getScene().getWindow());
        } else {
            errorLabel.setText("Invalid credentials!");
        }
    }

    @FXML
    private void handleRegister() {
        SceneUtil.switchTo("register.fxml", (Stage) usernameField.getScene().getWindow());
    }
}