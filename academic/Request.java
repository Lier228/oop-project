package academic;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class Request implements Serializable {

    /**
     * Default constructor
     */
    public Request() {
    }

    /**
     * 
     */
    private static void requestIdCounter;

    /**
     * 
     */
    public int senderId;

    /**
     * 
     */
    public void type;

    /**
     * 
     */
    public void status;

    /**
     * 
     */
    public String description;

    /**
     * 
     */
    public long requestId;

    /**
     * 
     */
    public Request() {
        // TODO implement here
    }

    /**
     * @param id 
     * @param type 
     * @param status 
     * @param description
     */
    public Request(int id, void type, void status, String description) {
        // TODO implement here
    }

    /**
     * @return
     */
    public void approve() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void reject() {
        // TODO implement here
        return null;
    }

}