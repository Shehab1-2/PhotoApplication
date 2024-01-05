package controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.UserDataManager;
import model.Photo;

/**
 * Controller class for the Copy/Move Photo screen of the application.
 * It provides functionality to copy or move a photo from one album to another.
 *
 * @author Andre
 */
public class CopyMoveScreenController {
    @FXML
    public ListView<Album> albumListView;
    @FXML
    public TextField albumName;

    public User currentUser;

    public Photo photoToCM;

    public Album currentAlbum;

    /**
     * Initializes the controller. This method sets up the albums list view.
     */
    public void initialize() {
        albumListView.setCellFactory(param -> new ListCell<Album>() {
            @Override
            protected void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);
                if (empty || album == null || album.getName() == null) {
                    setText(null);
                } else {
                    setText(album.getName());
                }
            }
        });
    }

    /**
     * Sets the list of albums to display in the ListView.
     *
     * @param albums The list of albums to display.
     */
    public void setAlbums(List<Album> albums) {
        albumListView.setItems(FXCollections.observableArrayList(albums));
        // This will update the ListView to display the albums passed to the method
    }

    /**
     * Sets the current user.
     *
     * @param user The current user.
     */
    protected void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * Sets the photo to be copied or moved.
     *
     * @param photo The photo to copy or move.
     */
    protected void setPhoto(Photo photo) {
        this.photoToCM = photo;
    }

    /**
     * Sets the current album.
     *
     * @param album The current album.
     */
    protected void setAlbum(Album album) {
        this.currentAlbum = album;
    }

    /**
     * Displays an alert dialog with the given title and content.
     *
     * @param title   The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handles the action to copy a photo to a selected album.
     */
    @FXML
    public void handleCopyPhoto() {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            selectedAlbum.addPhoto(photoToCM);
            Stage stage = (Stage) albumListView.getScene().getWindow();
            stage.close();
        } else {
            showAlert("Copy Error", "Please select an album to copy the photo to.");
        }
    }

    /**
     * Handles the action to move a photo to a selected album.
     */
    @FXML
    public void handleMovePhoto() {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            selectedAlbum.addPhoto(photoToCM);
            currentAlbum.removePhoto(photoToCM);
            Stage stage = (Stage) albumListView.getScene().getWindow();
            stage.close();
        } else {
            showAlert("Move Error", "Please select an album to move the photo to");
        }

    }
}
