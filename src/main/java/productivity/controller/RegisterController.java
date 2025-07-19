package main.java.productivity.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.productivity.model.UserSession;
import main.java.productivity.util.SceneUtil;
import main.java.productivity.store.UserStore;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleRegister() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("All fields are required.");
        } else if (UserStore.userExists(user)) {
            messageLabel.setText("Username already taken.");
        } else {
            UserStore.addUser(user, pass);
            // Set credentials in session after registration
            UserSession.getInstance().setCredentials(user, pass);
            messageLabel.setText("Registered successfully! Redirecting...");
            SceneUtil.switchTo("dashboard.fxml", (Stage) usernameField.getScene().getWindow());
        }
    }

    @FXML
    private void handleBack() {
        SceneUtil.switchTo("login.fxml", (Stage) usernameField.getScene().getWindow());
    }
}