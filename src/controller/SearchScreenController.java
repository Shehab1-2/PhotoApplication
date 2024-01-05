package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.UserDataManager;
import model.Photo;
import model.Tag;

/**
 * Controller for managing the search screen of the photo album application.
 * This controller handles searching photos by date range or tags and displays
 * the search results.
 * 
 * @author Andre
 */

public class SearchScreenController {
    User user;
    @FXML
    ListView<Photo> searchedPhotos;
    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;
    @FXML
    TextField tag1;
    @FXML
    TextField tag2;
    @FXML
    ChoiceBox<String> andOr;
    @FXML
    TextField albumName;

    List<Photo> foundPhotos = new ArrayList<Photo>();

    /**
     * Initializes the controller, setting up the necessary UI components.
     */
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList("AND", "OR");
        andOr.setItems(items);

        andOr.setValue("AND");

        searchedPhotos.setCellFactory(param -> new ListCell<Photo>() {
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

    }

    /**
     * Sets the user for whom the search will be performed.
     *
     * @param user The user to set.
     */
    protected void setUser(User user) {
        this.user = user;
    }

    /**
     * Handles the search functionality based on a date range.
     */
    @FXML
    protected void handleSearchByDateRange() {
        foundPhotos.clear();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        for (int i = 0; i < user.albums.size(); i++) {

            for (int j = 0; j < user.albums.get(i).getPhotos().size(); j++) {
                Photo checkedPhoto = user.albums.get(i).getPhotos().get(j);
                if ((checkedPhoto.getDateTaken().toLocalDate().isAfter(start))
                        && (checkedPhoto.getDateTaken().toLocalDate().isBefore(end)) && !foundPhotos.contains(checkedPhoto)) {
                    foundPhotos.add(checkedPhoto);
                }
            }
        }
        updateSearchListView();
    }

    /**
     * Handles the search functionality based on tags.
     */
    @FXML
    protected void handleSearchByTag() {
        foundPhotos.clear();

        String junction = andOr.getValue();

        if(!tag1.getText().equals("")){
        String[] tag1params = tag1.getText().split("=", 3);
        Tag tag1Search = new Tag(tag1params[0], tag1params[1]);

            if(!tag2.getText().equals("")){
                String[] tag2params = tag2.getText().split("=", 3);

        
                Tag tag2Search = new Tag(tag2params[0], tag2params[1]);

                for (int i = 0; i < user.albums.size(); i++) {

                    for (int j = 0; j < user.albums.get(i).getPhotos().size(); j++) {
                        Photo checkedPhoto = user.albums.get(i).getPhotos().get(j);
                        if(junction.equals("AND")){
                            if(checkedPhoto.getTags().contains(tag1Search) && checkedPhoto.getTags().contains(tag2Search) && !foundPhotos.contains(checkedPhoto)){
                            foundPhotos.add(checkedPhoto);
                            }

                        } else if (junction.equals("OR")){
                            if(checkedPhoto.getTags().contains(tag1Search) || checkedPhoto.getTags().contains(tag2Search) && !foundPhotos.contains(checkedPhoto)){
                            foundPhotos.add(checkedPhoto);
                            }  
                        }
                    }   
                }
            } else if (tag2.getText().equals("")){
                for (int i = 0; i < user.albums.size(); i++) {

                    for (int j = 0; j < user.albums.get(i).getPhotos().size(); j++) {
                        Photo checkedPhoto = user.albums.get(i).getPhotos().get(j);
                        if(checkedPhoto.getTags().contains(tag1Search) && !foundPhotos.contains(checkedPhoto)) foundPhotos.add(checkedPhoto);
                    }
                }
            }

        }

        
        updateSearchListView();
    }

    /**
     * Updates the search list view with found photos.
     */
    private void updateSearchListView() {
        searchedPhotos.setItems(FXCollections.observableList(foundPhotos));
    }

    /*
     * Creates an album using photos found through Search
     */
    @FXML
    private void handleCreateAlbum(){
        Album fromFound = new Album(albumName.getText());

        for(Photo picture : foundPhotos){
            fromFound.addPhoto(picture);
        }
        user.addAlbum(fromFound);
    }
}
