package users;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class User implements Serializable {

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * 
     */
    public int id;

    /**
     * 
     */
    public String username;

    /**
     * 
     */
    public String password;

    /**
     * 
     */
    public String email;

    /**
     * 
     */
    public Set<void> inboxNews;

    /**
     * 
     */
    public User() {
        // TODO implement here
    }

    /**
     * @param id 
     * @param username 
     * @param password 
     * @param email
     */
    public User(int id, String username, String password, String email) {
        // TODO implement here
    }

    /**
     * @param password 
     * @return
     */
    public boolean checkPassword(String password) {
        // TODO implement here
        return false;
    }

    /**
     * @return
     */
    public String toString() {
        // TODO implement here
        return "";
    }

    /**
     * @param news 
     * @return
     */
    public void receiveNews(void news) {
        // TODO implement here
        return null;
    }

}