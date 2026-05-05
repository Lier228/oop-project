package academic;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class News implements Serializable {

    /**
     * Default constructor
     */
    public News() {
    }

    /**
     * 
     */
    public String title;

    /**
     * 
     */
    public String content;

    /**
     * 
     */
    public void date;

    /**
     * 
     */
    public String author;

    /**
     * @param title 
     * @param content 
     * @param date
     */
    public News(String title, String content, void date) {
        // TODO implement here
    }

    /**
     * @param title 
     * @param content 
     * @param date 
     * @param author
     */
    public News(String title, String content, void date, String author) {
        // TODO implement here
    }

    /**
     * @return
     */
    public String toString() {
        // TODO implement here
        return "";
    }

}