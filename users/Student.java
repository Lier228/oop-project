package users;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class Student extends User {

    /**
     * Default constructor
     */
    public Student() {
    }

    /**
     * 
     */
    public static final int MAX_CREDITS = 21;

    /**
     * 
     */
    public double gpa;

    /**
     * 
     */
    public int year;

    /**
     * 
     */
    public int credits;

    /**
     * 
     */
    public int failedCourses;

    /**
     * 
     */
    public static final void userType;

    /**
     * 
     */
    public void schedule;

    /**
     * 
     */
    public Set<void> enrollments;

    /**
     * 
     */
    public void researchSupervisor;

    /**
     * 
     */
    public Student() {
        // TODO implement here
    }

    /**
     * @param id 
     * @param username 
     * @param password 
     * @param email 
     * @param gpa 
     * @param year 
     * @param credits 
     * @param failedCourses
     */
    public Student(int id, String username, String password, String email, double gpa, int year, int credits, int failedCourses) {
        // TODO implement here
    }

    /**
     * @param course 
     * @return
     */
    public void registerCourse(void course) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void viewTranscript() {
        // TODO implement here
        return null;
    }

    /**
     * @param supervisor 
     * @return
     */
    public void assignResearchSupervisor(void supervisor) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void viewSchedule() {
        // TODO implement here
        return null;
    }

    /**
     * public void viewSchedule() {
     * 		University uni = University.getInstance();
     * 		System.out.println("Schedule for " + username + ":");
     * 		for(Course course : uni.courses) {
     * 			if(course.students.contains(this)) {
     * 				System.out.println("- " + course.name + " (" + course.code + ")");
     * 				if(course.lessons.isEmpty()) {
     * 					System.out.println("  No lessons yet");
     * 					continue;
     * 				}
     * 				for(Lesson lesson : course.lessons) {
     * 					System.out.println("  " + lesson.getDisplayText());
     * 				}
     * 			}
     * 		}
     * 	}
     * @return
     */
    public void viewLearningFiles() {
        // TODO implement here
        return null;
    }

    /**
     * @param task 
     * @param solutionText 
     * @return
     */
    public void solveTask(void task, String solutionText) {
        // TODO implement here
        return null;
    }

}