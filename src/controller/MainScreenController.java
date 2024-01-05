package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.UserDataManager;

/**
 * Controller class for the Main Screen of the photo application.
 * Manages user interactions with the album list, including creation, deletion,
 * renaming, and opening of albums.
 *
 * @author Shehab Ahamed
 */
public class MainScreenController {
    @FXML
    public ListView<Album> albumListView;
    @FXML
    public TextField albumName;
    private UserDataManager userDataManager = UserDataManager.getInstance();
    public User currentUser;

    /**
     * Sets the current user after login and updates the album list view.
     *
     * @param loggedInUser The user who has logged in.
     */
    public void afterLogin(User loggedInUser) {
        this.currentUser = loggedInUser;
        updateAlbumListView();
    }

    /**
     * Initializes the controller and sets up the ListView for displaying albums.
     */
    public void initialize() {
        albumListView.setCellFactory(param -> new ListCell<Album>() {
            @Override
            protected void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);
                if (empty || album == null || album.getName() == null) {
                    setText(null);
                } else {
                    // Displaying the album name, number of photos, and date range
                    String text = album.getName() + " - Photos: " + album.getPhotos().size();
                    LocalDateTime earliestDate = album.getEarliestDate();
                    LocalDateTime latestDate = album.getLatestDate();

                    if (earliestDate != null && latestDate != null) {
                        // Format dates as needed
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        text += " (Dates: " + earliestDate.format(formatter) + " to " + latestDate.format(formatter)
                                + ")";
                    }
                    setText(text);
                }
            }
        });
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateAlbumListView();
    }

    /**
     * Updates the album list view with the current user's albums.
     */
    private void updateAlbumListView() {
        if (currentUser != null && currentUser.getAlbums() != null) {
            albumListView.setItems(FXCollections.observableList(currentUser.getAlbums()));
        }
    }

    public void setAlbums(List<Album> albums) {
        albumListView.setItems(FXCollections.observableArrayList(albums));
        // This will update the ListView to display the albums passed to the method
    }

    // Implement methods to create, delete, rename, and open albums

    /**
     * Handles the creation of a new album.
     */
    public void handleCreateAlbum() {
        // Example: Create a new album with a default name or prompt the user for a name
        String newAlbumName = albumName.getText(); // This could be replaced with user input
        Album newAlbum = new Album(newAlbumName);
        currentUser.addAlbum(newAlbum);
        updateAlbumListView();
        albumName.clear();
    }

    /**
     * Handles the renaming of an existing album.
     */
    public void handleRenameAlbum() {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            selectedAlbum.setName(albumName.getText());
            updateAlbumListView();
            albumName.clear();
        } else {
            showAlert("Selection Error", "No album selected to rename.");
            // Optionally, show an alert to the user
        }
    }

    /**
     * Handles the deletion of an existing album.
     */
    public void handleDeleteAlbum() {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            currentUser.removeAlbum(selectedAlbum);
            updateAlbumListView();
        } else {
            showAlert("Selection Error", "No album selected to delete.");
            // Optionally, show an alert to the user
        }
    }

    /**
     * Handles opening an album to view its photos.
     */
    public void handleOpenAlbum() {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoScreen.fxml"));
                Parent root = loader.load();

                PhotoScreenController photoScreenController = loader.getController();
                photoScreenController.setAlbum(selectedAlbum);
                photoScreenController.currentUser = currentUser;

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert("Loading Error", "Unable to open the selected album.");
                e.printStackTrace();
                // Handle exception
            }
        } else {
            // Handle case when no album is selected
            showAlert("Selection Error", "No album selected to open.");

        }
    }

    /**
     * Handles the search functionality.
     */
    @FXML
    protected void handleSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchScreen.fxml"));
            Parent root = loader.load();

            SearchScreenController searchScreenController = loader.getController();
            searchScreenController.setUser(currentUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Loading Error", "Unable to load the search screen.");
            e.printStackTrace();
        }
        updateAlbumListView();
    }

    /**
     * Handles the logout process.
     */
    @FXML
    protected void handleLogout() {
        // Save current user's data
        userDataManager.getInstance().saveUsers();
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) albumListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            showAlert("Logout ", "Sucessful.");

        } catch (IOException e) {
            showAlert("Logout Error", "An error occurred while logging out.");
            e.printStackTrace();
            // Handle exception
        }
    }

    /**
     * Displays an alert dialog with a given title and content.
     *
     * @param title   The title of the alert.
     * @param content The content of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
