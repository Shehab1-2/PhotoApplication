package model;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * Represents a photo in the application
 * This class contains details about the photo such as its file path,
 * the date it was taken, a caption, and any tags associated with it.
 * It also provides functionality to manage these properties.
 *
 * @author Shehab Ahamed
 */

public class Photo implements Serializable {
    private String filePathString; // Store as String
    private LocalDateTime dateTaken;
    private String caption;
    private List<Tag> tags;

    /**
     * Creates a new Photo with the specified file path.
     * The date taken is set to the current time, and the default caption is
     * assigned.
     * Initializes an empty list of tags.
     *
     * @param path The file path of the photo.
     */

    public Photo(String path) {
        this.filePathString = path;
        this.dateTaken = LocalDateTime.now(); // Or fetch from file metadata
        this.caption = "Default Image Caption";
        this.tags = new ArrayList<>(); // Initialize the list
    }

    /**
     * Adds a tag to the photo.
     * If a tag with the same key and value already exists, it is not added again.
     *
     * @param key   The key of the tag.
     * @param value The value of the tag.
     * @return true if the tag was added, false if it already exists.
     */
    public boolean addTag(String key, String value) {
        // Check if a tag with the same key and value already exists
        boolean tagExists = tags.stream().anyMatch(tag -> tag.getName().equals(key) && tag.getValue().equals(value));
        if (!tagExists) {
            tags.add(new Tag(key, value));
            return true; // Tag was added
        }
        return false; // Tag already exists
    }

    /**
     * Removes a tag from the photo by its key.
     *
     * @param key The key of the tag to be removed.
     */
    public void removeTag(Tag tag) {
        for(int i = 0; i < this.tags.size(); i++){
        Tag currentTag = tags.get(i);
        if(currentTag.getName().equals(tag.getName()) && currentTag.getValue().equals(tag.getValue()))
            tags.remove(currentTag); // Remove whole tag 
        }
        
    }

    // Getters and setters

    /**
     * Gets the file path of the photo as a Path object.
     *
     * @return The file path.
     */
    public Path getFilePath() {
        return Paths.get(filePathString);
    }

    /**
     * Sets the file path string of the photo.
     *
     * @param filePathString The new file path string.
     */
    public void setFilePathString(String filePathString) {
        this.filePathString = filePathString;
    }

    /**
     * Gets the date when the photo was taken.
     *
     * @return The date taken.
     */

    public LocalDateTime getDateTaken() {
        return dateTaken;
    }

    /**
     * Sets the date when the photo was taken.
     *
     * @param dateTaken The new date taken.
     */
    public void setDateTaken(LocalDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    /**
     * Gets the caption of the photo.
     *
     * @return The caption.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the caption of the photo.
     *
     * @param caption The new caption.
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Gets the list of tags associated with the photo.
     *
     * @return The list of tags.
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Returns an Image object for displaying in the GUI.
     * If the file path is invalid or the image cannot be loaded, returns null.
     *
     * @return The Image object or null.
     */
    public Image getImage() {
        if (filePathString != null && !filePathString.isEmpty()) {
            try {
                return new Image(Paths.get(filePathString).toUri().toString(), true);
            } catch (IllegalArgumentException | NullPointerException e) {
                // Handle the case where the image cannot be loaded
                System.err.println("Error loading image: " + e.getMessage());
            }
        }
        return null; // or a default image
    }

}
