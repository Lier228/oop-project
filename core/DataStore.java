package core;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * 
 */
public final class DataStore {

    /**
     * Default constructor
     */
    public DataStore() {
    }

    /**
     * 
     */
    private static final String STATE_FILE = "./data/university.ser";

    /**
     * 
     */
    private DataStore() {
        // TODO implement here
    }

    /**
     * @return
     */
    public static void saveState() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public static boolean loadState() {
        // TODO implement here
        return false;
    }

    /**
     * 
     */
    private class Snapshot implements Serializable {

        /**
         * Default constructor
         */
        private Snapshot() {
        }

        /**
         * 
         */
        private final void university;

        /**
         * 
         */
        private final Set<void> requests;

        /**
         * @param university 
         * @param requests
         */
        private Snapshot(void university, Set<void> requests) {
            // TODO implement here
        }

    }

}