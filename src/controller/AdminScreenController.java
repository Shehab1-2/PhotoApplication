package controller;

import javafx.scene.control.TableView;
import model.User;
import model.UserDataManager;

import java.io.IOException;

import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.UserDataManager;

/**
 * The AdminScreenController class handles the admin functionalities within the
 * application.
 * This includes managing user accounts and updating user lists. It provides a
 * UI for the admin
 * to create and delete users and to log out of the admin panel.
 *
 * @author Andre
 */
public class AdminScreenController {
    @FXML
    private ListView<User> usersListView;
    @FXML
    private TextField name;

    private UserDataManager userDataManager = UserDataManager.getInstance();

    /**
     * Initializes the controller. This method sets up the users list view with the
     * current user data and configures the cell factory.
     */
    public void initialize() {
        // Initialize the users table with user data
        usersListView.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null || user.getUsername() == null) {
                    setText(null);
                } else {
                    setText(user.getUsername());
                }
            }
        });
        updateUserList();
    }

    /**
     * Updates the list of users displayed on the admin screen.
     */
    private void updateUserList() {
        usersListView.setItems(FXCollections.observableList(userDataManager.getAllUsers()));
    }

    /**
     * Displays an alert dialog with the given title and content.
     *
     * @param title   The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handles the creation of a new user. It reads the username from the text
     * field,
     * creates a new user, and updates the user list.
     */
    @FXML
    private void handleCreateUser() {
        String newUserName = name.getText();
        if (!newUserName.trim().isEmpty()) {
            boolean successful = userDataManager.addUser(newUserName);
            if (successful) {
                showAlert("User Created", "New user '" + newUserName + "' created successfully.");
            } else {
                showAlert("User Already Exists", "A user with the name '" + newUserName + "' already exists.");
            }
            updateUserList();
            userDataManager.saveUsers();
            name.clear();
        } else {
            showAlert("Invalid Input", "Please enter a valid username.");
        }
    }

    /**
     * Handles the deletion of a selected user. It prompts for confirmation before
     * deleting the user and updates the user list.
     */
    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the user: " + selectedUser.getUsername() + "?",
                    ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    userDataManager.removeUser(selectedUser.getUsername());
                    updateUserList();
                    userDataManager.saveUsers();
                    showAlert("User Deleted", "User '" + selectedUser.getUsername() + "' was successfully deleted.");
                }
            });
        } else {
            showAlert("No User Selected", "Please select a user to delete.");
        }
    }

    /**
     * Handles the logout action. It saves the current user data and navigates
     * back to the login screen.
     */
    @FXML
    protected void handleLogout() {
        try {
            // Save current user's data
            userDataManager.saveUsers();

            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usersListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Logout Error", "An error occurred while logging out. Please try again.");
            e.printStackTrace();

        }
    }
}
