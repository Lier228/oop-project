package university.service;

import university.model.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final LogManager INSTANCE = new LogManager();
    private final List<String> logs = new ArrayList<>();

    private LogManager() {
    }

    public static LogManager getInstance() {
        return INSTANCE;
    }

    public void logAction(User user, String action) {
        String actor = user == null ? "System" : user.getUsername();
        logs.add(LocalDateTime.now() + " | " + actor + " | " + action);
    }

    public List<String> viewLogs() {
        return Collections.unmodifiableList(logs);
    }
}
