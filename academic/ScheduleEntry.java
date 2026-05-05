package academic;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class ScheduleEntry implements Serializable {

    /**
     * Default constructor
     */
    public ScheduleEntry() {
    }

    /**
     * 
     */
    public String courseName;

    /**
     * 
     */
    public String teacherName;


    /**
     * 
     */
    public Room room;

    /**
     * @param courseName 
     * @param teacherName 
     * @param room
     */
    public ScheduleEntry(String courseName, String teacherName, Room room) {
        // TODO implement here
    }

    /**
     * @param teacherName 
     * @return
     */
    public void setTeacher(String teacherName) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseName 
     * @return
     */
    public void setCourse(String courseName) {
        // TODO implement here
        return null;
    }

    /**
     * @param room 
     * @return
     */
    public void setRoom(Room room) {
        // TODO implement here
        return null;
    }

}