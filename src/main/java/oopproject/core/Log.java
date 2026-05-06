package oopproject.core;

import java.time.LocalDateTime;
import oopproject.users.User;

public class Log {
    private User user;
    private String action;
    private LocalDateTime date;

    public Log(User user, String action) {
        this.user = user;
        this.action = action;
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return date + " " + user + " - " + action;
    }
}
