package model;

import java.io.Serializable;

/**
 * Represents a tag for a photo in a photo management application.
 * A tag consists of a name and a value, allowing for flexible categorization
 * and organization of photos.
 *
 * @author Andre
 */

public class Tag implements Serializable {
    private String name;
    private String value;

    /**
     * Constructs a new Tag with the specified name and value.
     *
     * @param name  The name of the tag.
     * @param value The value of the tag.
     */
    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    // Getters and Setters

    /**
     * Gets the name of the tag.
     *
     * @return The name of the tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag.
     *
     * @param name The new name for the tag.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the tag.
     *
     * @return The value of the tag.
     */

    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the tag.
     *
     * @param value The new value for the tag.
     */

    public void setValue(String value) {
        this.value = value;
    }

    /*
     *  Equals method for the purposes of searching.
     */
    public boolean equals(Tag tag){
        if(this.name == tag.name && this.value == tag.value) return true;
        else return false;
    }
}
