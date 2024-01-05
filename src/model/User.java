package model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Represents a user in a photo management application.
 * Each user has a username and a collection of albums.
 *
 * @author Shehab Ahamed
 */
public class User implements Serializable {
    public String username;
    public List<Album> albums;

    /**
     * Constructs a new User with the specified username.
     *
     * @param username The username of the user.
     */
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
    }

    // Methods to add, remove, and get albums

    /**
     * Adds an album to the user's collection.
     *
     * @param album The album to be added.
     */
    public void addAlbum(Album album) {
        albums.add(album);
    }

    /**
     * Removes an album from the user's collection.
     *
     * @param album The album to be removed.
     */
    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    /**
     * Retrieves the list of albums belonging to the user.
     *
     * @return A list of the user's albums.
     */
    public List<Album> getAlbums() {
        return albums;
    }

    // Getters and Setters

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The new username for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
