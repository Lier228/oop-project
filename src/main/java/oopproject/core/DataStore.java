package oopproject.core;

public final class DataStore {
    private static final String STATE_FILE = "./data/university.ser";

    private DataStore() {
    }

    public static void saveState() {
        // Serialization is outside the model-only scope of part B.
    }

    public static boolean loadState() {
        return false;
    }

    public static String getStateFile() {
        return STATE_FILE;
    }
}
