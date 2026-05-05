package academic;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class StudyMaterial implements Serializable {

    /**
     * Default constructor
     */
    public StudyMaterial() {
    }

    /**
     * 
     */
    public String title;

    /**
     * 
     */
    public String description;

    /**
     * 
     */
    public String fileName;

    /**
     * 
     */
    public void deadline;

    /**
     * 
     */
    public boolean isTask;

    /**
     * 
     */
    public void uploadedBy;

    /**
     * 
     */
    public void submissions;


    /**
     * 
     */
    public Course course;

    /**
     * @param title 
     * @param description 
     * @param fileName 
     * @param deadline 
     * @param isTask 
     * @param uploadedBy 
     * @param course
     */
    public StudyMaterial(String title, String description, String fileName, void deadline, boolean isTask, void uploadedBy, Course course) {
        // TODO implement here
    }

    /**
     * @param student 
     * @param solutionText 
     * @return
     */
    public void submitSolution(void student, String solutionText) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public String getSubmission(int studentId) {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public String toString() {
        // TODO implement here
        return "";
    }

}