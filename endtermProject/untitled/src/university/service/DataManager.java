package university.service;

import university.model.University;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {
    private static final DataManager INSTANCE = new DataManager();
    private static final String DEFAULT_FILE = "university-data.ser";

    private DataManager() {
    }

    public static DataManager getInstance() {
        return INSTANCE;
    }

    public void save() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(DEFAULT_FILE))) {
            outputStream.writeObject(University.getInstance());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save university data.", e);
        }
    }

    public University load() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DEFAULT_FILE))) {
            University university = (University) inputStream.readObject();
            University.setInstance(university);
            return university;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load university data.", e);
        }
    }
}
