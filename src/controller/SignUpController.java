package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.UserDataManager;

/**
 * Controller for managing the sign-up process in the photo album application.
 * This class handles user registration, including input validation and
 * interaction with the UserDataManager for user persistence.
 * 
 * @author Shehab Ahamed
 */

public class SignUpController {

    @FXML
    private TextField usernameField;

    private UserDataManager userDataManager = UserDataManager.getInstance();

    /**
     * Handles the registration process when the register button is clicked.
     * Validates the username and communicates with the UserDataManager to
     * register the new user.
     */
    @FXML
    protected void handleRegister() {
        String username = usernameField.getText().trim();
        if (!username.isEmpty()) {
            boolean success = userDataManager.addUser(username);
            if (success) {
                showAlert("Registration Successful", "User " + username + " has been registered successfully.");

                // Close the sign-up screen
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.close();
            } else {
                // Show an error message (user already exists or other error)
                // User already exists
                showAlert("Registration Failed", "Username already taken. Please try a different one.");
            }
        } else {
            // Show an error message (username is empty)
            showAlert("Registration Error", "Username field cannot be empty. Please enter a username.");

        }
    }

    /**
     * Displays an alert dialog with the specified title and content.
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
}
