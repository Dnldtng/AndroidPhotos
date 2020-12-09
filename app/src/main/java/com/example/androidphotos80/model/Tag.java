package com.example.androidphotos80.model;

import java.io.Serializable;

public class Tag implements Serializable {
    private static final long serialVersionUID = 4420084517420071942L;
    static final int LOCATION = 1;
    static final int PERSON = 2;

    private String value;
    private int name;

    public Tag(int name, String value){
        this.name = name;
        this.value = value;
    }



    /**
     * Returns the name of the tag
     * @return Name string
     */
    public int getName() {
        return name;
    }

    /**
     * Sets the name of tag
     * @param name Name string
     */
    public void setName(int name) {
        this.name = name;
    }

    /**
     * Returns the value of tag
     * @return Value string
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of tag
     * @param value Value string
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * toString override for tag printing
     */
    public String toString() {
        return name + ":" + value;
    }


}
