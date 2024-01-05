package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
import model.UserDataManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import Main.Photos;

/**
 * Controller class for the Login screen of the application.
 * Manages the login functionality, including stock user initialization, admin
 * login, and user login.
 *
 * @author Shehab Ahamed & Andre
 */

public class LoginController {
    public static final String STOCK_PHOTOS_PATH = "data";
    public static User stockUser;
    @FXML
    private TextField usernameField;

    private UserDataManager userDataManager = UserDataManager.getInstance();

    /**
     * Handles the sign-up button action.
     * Loads the sign-up screen where new users can register.
     */
    @FXML
    protected void handleSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUpScreen.fxml"));
            Pane signUpPane = loader.load();
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");
            signUpStage.setScene(new Scene(signUpPane));
            signUpStage.show();
        } catch (IOException e) {
            showAlert("Loading Error", "Unable to load the sign-up screen.");

        }
    }

    /**
     * Loads the user view screen after successful login.
     *
     * @param username The username of the user.
     */
    private void loadUserView(String username) {
        // Logic to load the main screen with the specified user
    }

    /**
     * Handles the login button action.
     * Manages the login process for admin, stock, and regular users.
     */
    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();

        if (username.equals("admin")) {
            // Admin login
            showAlert("Admin Login", "Sucessful!");
            loadAdminView();
        } else if (username.equals("stock")) {
            // Stock user login
            stockUser = userDataManager.getUser(username);
            stockUser = initializeStockPhotos();
            showAlert("Stock Login", "Sucessful!");
            loadUserView(stockUser);

        } else {
            // Regular user login
            User user = userDataManager.getUser(username);
            if (user != null) {
                showAlert("Login", "Sucessful!");
                loadUserView(user);
            } else {
                showLoginError();
            }
        }
    }

    /**
     * Displays a login error alert.
     */
    private void showLoginError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid username or password.");
        alert.showAndWait();
    }

    /**
     * Loads the admin view screen.
     */
    public void loadAdminView() {
        try {
            // Load the admin view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminScreen.fxml"));
            Parent root = loader.load();

            // Set the new scene
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Loading Error", "Unable to load the admin view.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the main user view screen.
     *
     * @param user The user to load the view for.
     */
    private void loadUserView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
            Parent root = loader.load();

            MainScreenController mainScreenController = loader.getController();
            mainScreenController.afterLogin(user); // Pass the user data to the main screen

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Loading Error", "Unable to load the user view.");
            e.printStackTrace();
            // Handle IOException
        }
    }

    /**
     * Initializes stock photos for the stock user.
     * Creates a stock user with a predefined set of photos.
     *
     * @return The initialized stock user.
     */
    private User initializeStockPhotos() {
        User stockUser = new User("stock");
        Album stockAlbum = new Album("stock");

        File stockPhotoDir = new File(Paths.get(System.getProperty("user.dir"), STOCK_PHOTOS_PATH).toUri());
        if (stockPhotoDir.exists() && stockPhotoDir.isDirectory()) {
            for (File photoFile : stockPhotoDir.listFiles()) {
                if (isImageFile(photoFile)) {
                    stockAlbum.addPhoto(new Photo(photoFile.getAbsolutePath()));
                }
            }
        } else {
            showAlert("Stock Photos Error", "Stock photos directory not found.");
        }

        stockUser.addAlbum(stockAlbum);
        return stockUser;
    }

    /**
     * Checks if a file is an image based on its file extension.
     *
     * @param file The file to check.
     * @return True if the file is an image, false otherwise.
     */
    private boolean isImageFile(File file) {
        // Implement logic to check if the file is an image (e.g., based on file
        // extensions)
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
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
}
