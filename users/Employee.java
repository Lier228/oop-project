package users;

import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Employee extends User {

    /**
     * Default constructor
     */
    public Employee() {
    }

    /**
     * 
     */
    public double salary;

    /**
     * 
     */
    public void hireDate;

    /**
     * 
     */
    protected static Set<void> requests;

    /**
     * 
     */
    public Employee() {
        // TODO implement here
    }

    /**
     * @param id 
     * @param username 
     * @param password 
     * @param email 
     * @param salary 
     * @param hireDate
     */
    public Employee(int id, String username, String password, String email, double salary, void hireDate) {
        // TODO implement here
    }

    /**
     * @return
     */
    public static Set<void> getRequestInstance() {
        // TODO implement here
        return null;
    }

    /**
     * @param loadedRequests 
     * @return
     */
    public static void setRequestInstance(Set<void> loadedRequests) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void addRequest() {
        // TODO implement here
        return null;
    }

    /**
     * @param request 
     * @return
     */
    public void addRequest(void request) {
        // TODO implement here
        return null;
    }

}