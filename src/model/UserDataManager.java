package model;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages user data for a photo management application.
 * This includes loading, saving, and manipulating user data.
 * 
 * @author Shehab Ahamed
 */
public class UserDataManager {
    private static final String USER_DATA_FILE = "users.dat";
    private static UserDataManager instance;
    private List<User> users;

    /**
     * Constructs a UserDataManager and loads user data from a file.
     */
    public UserDataManager() {
        loadUsers();
    }

    /**
     * Gets the singleton instance of UserDataManager.
     *
     * @return The singleton instance of UserDataManager.
     */
    public static UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    /**
     * Loads users from a data file.
     */
    protected void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            users = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }

    }

    /**
     * Saves the current user data to a file.
     */
    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new user with the given username if it does not already exist.
     *
     * @param username The username of the new user.
     * @return True if the user was added, false otherwise.
     */
    public boolean addUser(String username) {
        if (getUser(username) == null) {
            users.add(new User(username));
            saveUsers();
            return true;
        }
        return false;
    }

    /**
     * Removes the user with the specified username.
     *
     * @param username The username of the user to be removed.
     * @return True if the user was removed, false otherwise.
     */
    public boolean removeUser(String username) {
        User user = getUser(username);
        if (user != null) {
            users.remove(user);
            saveUsers();
            return true;
        }
        return false;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The User object if found, null otherwise.
     */
    public User getUser(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return users;
    }
}
