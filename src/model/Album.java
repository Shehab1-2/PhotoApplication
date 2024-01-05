package model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents an album in a photo application.
 * An album is a collection of photos, each represented by a Photo
 * object.
 * This class provides methods to manage photos within the album, including
 * adding
 * and removing photos, as well as getting information about the album such
 * as
 * its name, the number of photos, and the range of dates on which photos were
 * taken.
 *
 * @author Shehab Ahamed
 */

public class Album implements Serializable {
    private String name;
    private List<Photo> photos;

    /**
     * Constructs a new Album with the specified name.
     * If the provided name is null or empty, a default name "Unnamed Album" is
     * used.
     *
     * @param name The name of the album.
     */
    public Album(String name) {
        this.name = name != null && !name.trim().isEmpty() ? name : "Unnamed Album";
        this.photos = new ArrayList<>();
    }

    // Methods to add, remove, and get photos

    /**
     * Adds a photo to the album.
     *
     * @param photo The Photo to be added.
     */
    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    /**
     * Removes a photo from the album.
     *
     * @param photo The Photo to be removed.
     */

    public void removePhoto(Photo photo) {
        photos.remove(photo);
    }

    /**
     * Returns a list of all photos in the album.
     *
     * @return A list of Photo objects.
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    // Getters and Setters

    /**
     * Returns the name of the album.
     *
     * @return The name of the album.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the album.
     *
     * @param name The new name of the album.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the earliest date on which a photo was taken in the album.
     *
     * @return The earliest date or null if no photos are in the album.
     */
    public LocalDateTime getEarliestDate() {
        return photos.stream()
                .map(Photo::getDateTaken)
                .min(LocalDateTime::compareTo)
                .orElse(null); // Return null if there are no photos
    }

    /**
     * Retrieves the latest date on which a photo was taken in the album.
     *
     * @return The latest date or null if no photos are in the album.
     */
    public LocalDateTime getLatestDate() {
        return photos.stream()
                .map(Photo::getDateTaken)
                .max(LocalDateTime::compareTo)
                .orElse(null); // Return null if there are no photos
    }

    /**
     * Returns the number of photos in the album.
     *
     * @return The number of photos.
     */
    public int getNumberOfPhotos() {
        return photos.size();
    }

    @Override
    public String toString() {
        LocalDateTime earliest = getEarliestDate();
        LocalDateTime latest = getLatestDate();
        return String.format("%s - %d photos (Dates: %s to %s)",
                name, getNumberOfPhotos(),
                earliest != null ? earliest.toString() : "N/A",
                latest != null ? latest.toString() : "N/A");
    }

}
