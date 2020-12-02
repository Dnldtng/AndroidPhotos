package com.example.androidphotos80.model;

import java.util.Date;
import java.util.ArrayList;

public class Photo {
    /**
     * Tag list for photo
     */
    private ArrayList<Tag> tags = new ArrayList<Tag>();

    /**
     * Caption string
     */
    private String caption = "";

    /**
     * Date object for photo date
     */
    private Date photoDate;

    /**
     * Path string of photo
     */
    private String path;

    /**
     * Photo constructor
     * @param p Path string of photo
     */
    public Photo(String p) {
        this.path = p;
    }

    /**
     * Returns path string of photo
     * @return Path string
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Adds tag to tag list
     * @param t Tag object
     */
    public void addTag(Tag t) {
        tags.add(t);
    }

    /**
     * Returns the tag list
     * @return ArrayList of tags
     */
    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    /**
     * Returns a string of all tags
     * @return Tag string
     */
    public String getStringTags() {
        StringBuilder sb = new StringBuilder();
        for(Tag t : tags) {
            sb.append(t.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns the photos date
     * @return Date object
     */
    public Date getPhotoDate() {
        return photoDate;
    }


    /**
     * Removes a tag from the tag list
     * @param t Tag to be removed
     */
    public void removeTag(Tag t) {
        tags.remove(t);
    }

    /**
     * Checks if a photo has duplicate tags of passed tag
     * @param temp Tag to check if duplicates exist of
     * @return true if found, false if not
     */
    public boolean duplicatesExist(Tag temp) {
        for(Tag t :tags) {
            if(t.equals(temp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the value of the tag in the tag list at the index passed
     * @param index Index of tag in tag list to be edited.
     * @param name New name for tag
     * @param value New value for tag
     */
    public void editTag(int index, int name, String value) {
        tags.get(index).setName(name);
        tags.get(index).setValue(value);
    }

    /**
     * Returns the photos caption
     * @return Caption string
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the photos caption
     * @param caption Caption string
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }
}
