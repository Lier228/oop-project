package academic;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class Lesson implements Serializable {

    /**
     * Default constructor
     */
    public Lesson() {
    }

    /**
     * 
     */
    public void time;

    /**
     * 
     */
    public String room;

    /**
     * 
     */
    public void type;


    /**
     * @param time 
     * @param room 
     * @param type
     */
    public Lesson(void time, String room, void type) {
        // TODO implement here
    }

    /**
     * @return
     */
    public String getDisplayText() {
        // TODO implement here
        return "";
    }

}