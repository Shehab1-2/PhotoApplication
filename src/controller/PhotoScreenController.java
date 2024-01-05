package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.Photo;
import model.UserDataManager;
import model.Album;
import model.User;
import model.Tag;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

/**
 * Controller for managing the photo screen of the photo album application.
 * This controller is responsible for displaying photo details, adding tags,
 * handling photo navigation, and other related functionalities.
 *
 * @author Shehab Ahamed
 */
public class PhotoScreenController {
    @FXML
    private ListView<Photo> photoListView;
    @FXML
    private ImageView selectedPhotoView;
    @FXML
    private Label photoCaption;
    @FXML
    private Label photoDate;
    @FXML
    private ListView<String> photoTags;
    @FXML
    private TextField tagNameField;
    @FXML
    private TextField tagValueField;
    @FXML
    private TextField captionField;

    private Album currentAlbum;

    protected User currentUser;

    private int currentPhotoIndex = 0;

    private UserDataManager saver = UserDataManager.getInstance();

    /**
     * Initializes the controller and sets up listeners for UI components.
     */
    public void initialize() {
        photoListView.setCellFactory(param -> new ListCell<Photo>() {
            @Override
            protected void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);
                if (empty || photo == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Display thumbnail or photo information
                    setText(photo.getCaption()); // For instance
                    // You can also set a graphic, such as an ImageView with the photo's thumbnail
                }
            }
        });

        photoListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updatePhotoDetails(newSelection);
            }
        });
    }

    /**
     * Sets the current album and updates the photo list view.
     *
     * @param album The album to display photos from.
     */
    public void setAlbum(Album album) {
        this.currentAlbum = album;

        if (album != null && !album.getPhotos().isEmpty()) {
            currentPhotoIndex = 0;
            updatePhotoDetails(album.getPhotos().get(currentPhotoIndex));
        }

        ObservableList<Photo> observablePhotoList = FXCollections.observableArrayList(album.getPhotos());
        photoListView.setItems(observablePhotoList);
    }

    /**
     * Updates the photo details section with information from the selected photo.
     *
     * @param photo The photo to display details of.
     */
    public void updatePhotoDetails(Photo photo) {
        try {
            if (photo.getFilePath() != null) {
                selectedPhotoView.setImage(new Image(photo.getFilePath().toUri().toString()));
            } else {
                selectedPhotoView.setImage(null);
            }
        } catch (Exception e) {
            showAlert("Error Loading Photo", "Could not load the selected photo.");
            selectedPhotoView.setImage(null);
        }

        photoCaption.setText(photo.getCaption() != null ? photo.getCaption() : "No Caption");
        photoDate.setText(photo.getDateTaken() != null
                ? photo.getDateTaken().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "Unknown Date");

        photoTags.setItems(FXCollections.observableArrayList(
                photo.getTags().stream()
                        .map(tag -> tag.getName() + ": " + tag.getValue())
                        .collect(Collectors.toList())));
    }

    /**
     * Shows an error dialog when a tag already exists for the selected photo.
     */
    public void showTagExistsError() {
        // Show an alert dialog or some form of notification
        // to inform the user that the tag already exists

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Tag Error");
        alert.setHeaderText(null);
        alert.setContentText("This tag already exists for the selected photo.");
        alert.showAndWait();
    }

    /**
     * Handles adding a tag to a photo.
     */
    @FXML
    public void handleAddTag() {
        String tagName = tagNameField.getText().trim();
        String tagValue = tagValueField.getText().trim();

        if (!tagName.isEmpty() && !tagValue.isEmpty()) {
            Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
            if (selectedPhoto != null) {
                boolean tagAdded = selectedPhoto.addTag(tagName, tagValue);
                if (tagAdded) {
                    updatePhotoDetails(selectedPhoto); // Refresh the details
                    for (int i = 0; i < currentUser.albums.size(); i++) {

                     for (int j = 0; j < currentUser.albums.get(i).getPhotos().size(); j++) {
                        if(currentUser.albums.get(i).getPhotos().get(j).getFilePath().equals(selectedPhoto.getFilePath())){
                            currentUser.albums.get(i).getPhotos().get(j).addTag(tagName, tagValue);
                            }
                        }
                    }
                    showAlert("Tag Added", "New tag added successfully.");
                } else {
                    // Tag already exists, show an error message
                    showTagExistsError();
                }
            }
            tagNameField.clear();
            tagValueField.clear();
        } else {
            showAlert("Empty Fields", "Tag name and value cannot be empty.");
        }
    }


    /*
     * Deletes tags from photos
     */
    @FXML
    private void handleDeleteTag(){
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();

        String selectedTagString = photoTags.getSelectionModel().getSelectedItem();
        String[] tagParams = selectedTagString.split(": ");

        Tag tag = new Tag(tagParams[0], tagParams[1]);

        selectedPhoto.removeTag(tag);

        updatePhotoDetails(selectedPhoto);
        updatePhotoListView();
    }

    /**
     * Updates the photo list view to reflect any changes in the photo list.
     */
    private void updatePhotoListView() {
        photoListView.setItems(FXCollections.observableList(currentAlbum.getPhotos()));
    }

    /**
     * Shows the next photo in the album.
     *
     * @param event The action event triggering this method.
     */
    public void showNextPhoto(ActionEvent event) {
        if (currentAlbum != null && !currentAlbum.getPhotos().isEmpty()) {
            currentPhotoIndex = (currentPhotoIndex + 1) % currentAlbum.getPhotos().size();
            updatePhotoDetails(currentAlbum.getPhotos().get(currentPhotoIndex));
        }
    }

    /**
     * Shows the previous photo in the album.
     *
     * @param event The action event triggering this method.
     */
    public void showPreviousPhoto(ActionEvent event) {
        if (currentAlbum != null && !currentAlbum.getPhotos().isEmpty()) {
            currentPhotoIndex = (currentPhotoIndex - 1 + currentAlbum.getPhotos().size())
                    % currentAlbum.getPhotos().size();
            updatePhotoDetails(currentAlbum.getPhotos().get(currentPhotoIndex));
        }
    }

    /**
     * Displays an alert dialog with a given title and content.
     *
     * @param title   The title of the alert.
     * @param content The content of the alert.
     */
    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Implement methods for add/remove/caption/tag/copy functionalities here

    /**
     * Handles the addition of a new photo to the current album.
     */
    @FXML
    public void handleAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add a Photo!");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                 Photo chosenPhoto = new Photo(selectedFile.getAbsolutePath());
                //first, check search for photo in user's already added photos;
                for (int i = 0; i < currentUser.albums.size(); i++) {

                    for (int j = 0; j < currentUser.albums.get(i).getPhotos().size(); j++) {
                        if(currentUser.albums.get(i).getPhotos().get(j).getFilePath().equals(chosenPhoto.getFilePath())){
                            chosenPhoto = currentUser.albums.get(i).getPhotos().get(j);
                        }
                    }
                }
               
                currentAlbum.addPhoto(chosenPhoto);
                updatePhotoListView();
                saver.saveUsers();
                showAlert("Photo Added", "Photo added successfully to the album.");
            } catch (Exception e) {
                showAlert("Error Adding Photo", "Could not add the selected photo.");

            }
        }

    }

    /**
     * Handles updating the caption of the selected photo.
     */
    @FXML
    public void handleCaption() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        selectedPhoto.setCaption(captionField.getText());
        updatePhotoDetails(selectedPhoto);

        for (int i = 0; i < currentUser.albums.size(); i++) {

                    for (int j = 0; j < currentUser.albums.get(i).getPhotos().size(); j++) {
                        if(currentUser.albums.get(i).getPhotos().get(j).getFilePath().equals(selectedPhoto.getFilePath())){
                            currentUser.albums.get(i).getPhotos().get(j).setCaption(captionField.getText());
                        }
                    }
        }
        
        updatePhotoListView();
        captionField.clear();
    }

    /**
     * Handles the deletion of the selected photo from the current album.
     */
    @FXML
    public void handleDeletePhoto() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            currentAlbum.removePhoto(selectedPhoto);
            updatePhotoListView();
            // Check if there are still photos in the album after deletion
            if (!currentAlbum.getPhotos().isEmpty()) {
                // Select the next photo or the last one if it was the last in the list
                int newIndex = currentPhotoIndex < currentAlbum.getPhotos().size() ? currentPhotoIndex
                        : currentAlbum.getPhotos().size() - 1;
                updatePhotoDetails(currentAlbum.getPhotos().get(newIndex));
                photoListView.getSelectionModel().select(newIndex);
            } else {
                // No photos left, clear details
                updatePhotoDetails(null);
                currentPhotoIndex = -1; // reset index
            }
        }
        saver.saveUsers();
    }

    /**
     * Handles the copy or move functionality for the selected photo to another
     * album.
     */
    @FXML
    public void handleCopyMove() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CopyMoveScreen.fxml"));
                Parent root = loader.load();

                CopyMoveScreenController copyMoveScreenController = loader.getController();
                copyMoveScreenController.setPhoto(photoListView.getSelectionModel().getSelectedItem());
                copyMoveScreenController.setUser(currentUser);
                copyMoveScreenController.setAlbum(currentAlbum);
                copyMoveScreenController.setAlbums(currentUser.getAlbums());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // throw error
        }
        updatePhotoListView();
    }

}
