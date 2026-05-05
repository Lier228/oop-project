package academic;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * private void seedUsers() {
 * 		Admin a = new Admin(); a.id = 1; a.username = "admin"; a.password = "123";
 * 		Student s = new Student(2,"alice","111","a@mail.com",3.5,2,0,0);
 * 		Teacher t = new Teacher(); t.id = 3; t.username = "bob"; t.password = "222";
 * 		Manager m = new Manager(); m.id = 4; m.username = "manager"; m.password = "333";
 * 
 * 		uni.users.add(a);
 * 		uni.users.add(s);
 * 		uni.users.add(t);
 * 		uni.users.add(m);
 * 	}
 */
public class Course implements Serializable {

    /**
     * Default constructor
     */
    public Course() {
    }

    /**
     * 
     */
    public String name;

    /**
     * 
     */
    public String code;

    /**
     * 
     */
    public int credits;

    /**
     * 
     */
    public boolean isOpen;

    /**
     * 
     */
    public Set<void> students;

    /**
     * 
     */
    public Set<void> instructors;

    /**
     * 
     */
    public Set<Lesson> lessons;

    /**
     * 
     */
    public Set<StudyMaterial> materials;

    /**
     * 
     */
    public Set<Enrollment> enrollments;



    /**
     * @param name 
     * @param code 
     * @param credits
     */
    public Course(String name, String code, int credits) {
        // TODO implement here
    }

    /**
     * @param s 
     * @return
     */
    public void addStudent(void s) {
        // TODO implement here
        return null;
    }

    /**
     * @param t 
     * @return
     */
    public void addInstructor(void t) {
        // TODO implement here
        return null;
    }

    /**
     * @param lesson 
     * @return
     */
    public void addLesson(Lesson lesson) {
        // TODO implement here
        return null;
    }

    /**
     * @param material 
     * @return
     */
    public void addMaterial(StudyMaterial material) {
        // TODO implement here
        return null;
    }

    /**
     * @param student 
     * @return
     */
    public Enrollment findEnrollment(void student) {
        // TODO implement here
        return null;
    }

}