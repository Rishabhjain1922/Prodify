package main.java.productivity.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.productivity.model.Task;
import main.java.productivity.model.TaskPriority;
import main.java.productivity.model.UserSession;
import main.java.productivity.util.SceneUtil;
import javafx.geometry.Pos;
import javax.swing.text.Position;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    @FXML private VBox taskContainer;
    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private Label welcomeLabel;
    @FXML private Label taskCountLabel;
    @FXML private ComboBox<TaskPriority> priorityCombo;

    private final ObjectMapper mapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private String currentUser;
    private final String dataDir = "data";

    @FXML
    private void initialize() {
        // Get username from session
        currentUser = UserSession.getInstance().getUsername();
        if (currentUser == null) {
            redirectToLogin();
            return;
        }

        welcomeLabel.setText("Welcome, " + currentUser + "!");
        new File(dataDir).mkdirs();

        // Initialize priority combo box
        priorityCombo.setItems(FXCollections.observableArrayList(TaskPriority.values()));
        priorityCombo.getSelectionModel().select(TaskPriority.MEDIUM);

        loadTasks();
        updateTaskCount();
    }

    private void redirectToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) taskContainer.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            updateTaskCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTask() {
        String title = titleField.getText().trim();
        String desc = descField.getText().trim();
        TaskPriority priority = priorityCombo.getValue();

        if (!title.isEmpty() && !desc.isEmpty()) {
            Task task = new Task(title, desc, priority);
            tasks.add(task);
            saveTasks();
            addTaskToUI(task);
            titleField.clear();
            descField.clear();
            titleField.requestFocus();
        } else {
            showAlert("Validation Error", "Both title and description are required.");
        }
    }

    private void addTaskToUI(Task task) {
        VBox card = new VBox();
        card.getStyleClass().add("task-card");
        card.getStyleClass().add("priority-" + task.getPriority().toString().toLowerCase());

        HBox header = new HBox();
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(task.getTitle());
        title.getStyleClass().add("task-title");

        Label priority = new Label(task.getPriority().toString());
        priority.getStyleClass().add("priority-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label date = new Label(task.getFormattedDate());
        date.getStyleClass().add("date-label");

        header.getChildren().addAll(title, spacer, priority, date);

        Label desc = new Label(task.getDescription());
        desc.getStyleClass().add("task-desc");
        desc.setWrapText(true);

        Button delete = new Button("Delete");
        delete.getStyleClass().add("delete-button");

        delete.setOnAction(e -> {
            tasks.remove(task);
            saveTasks();
            taskContainer.getChildren().remove(card);
        });

        card.getChildren().addAll(header, desc, delete);
        card.setOpacity(0);
        taskContainer.getChildren().add(card);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), card);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private void updateTaskCount() {
        taskCountLabel.setText(tasks.size() + (tasks.size() == 1 ? " task" : " tasks"));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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