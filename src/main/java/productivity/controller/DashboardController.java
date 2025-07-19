package main.java.productivity.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.productivity.model.Task;
import main.java.productivity.model.UserSession;
import main.java.productivity.util.SceneUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    @FXML private VBox taskContainer;
    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private Label welcomeLabel;

    private final ObjectMapper mapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private String currentUser;
    private final String dataDir = "data";

    @FXML
    private void initialize() {
        // Get username from session (should be set by login/register)
        currentUser = UserSession.getInstance().getUsername();
        if (currentUser == null) {
            // Handle case where user accesses dashboard directly without login
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

                // Close current window if possible
                Stage currentStage = (Stage) taskContainer.getScene().getWindow();
                currentStage.close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        welcomeLabel.setText("Welcome, " + currentUser + "!");
        new File(dataDir).mkdirs();
        loadTasks();
    }

    private File getUserFile() {
        return new File(dataDir + "/" + currentUser + "_tasks.json");
    }

    private void loadTasks() {
        tasks.clear();
        taskContainer.getChildren().clear();
        File file = getUserFile();
        if (file.exists()) {
            try {
                tasks = mapper.readValue(file, new TypeReference<List<Task>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Task task : tasks) addTaskToUI(task);
    }

    private void saveTasks() {
        try {
            mapper.writeValue(getUserFile(), tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTask() {
        String title = titleField.getText();
        String desc = descField.getText();
        if (!title.isEmpty() && !desc.isEmpty()) {
            Task task = new Task(title, desc);
            tasks.add(task);
            saveTasks();
            addTaskToUI(task);
            titleField.clear();
            descField.clear();
        }
    }

    private void addTaskToUI(Task task) {
        VBox card = new VBox();
        card.getStyleClass().add("task-card");

        Label title = new Label(task.getTitle());
        title.getStyleClass().add("task-title");

        Label desc = new Label(task.getDescription());
        desc.getStyleClass().add("task-desc");

        Button delete = new Button("Delete");
        delete.getStyleClass().add("delete-button");

        delete.setOnAction(e -> {
            tasks.remove(task);
            saveTasks();
            taskContainer.getChildren().remove(card);
        });

        card.getChildren().addAll(title, desc, delete);
        card.setOpacity(0);
        taskContainer.getChildren().add(card);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), card);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UserSession.getInstance().clear();
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneUtil.switchTo("login.fxml", stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
