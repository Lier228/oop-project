package oopproject.core;

import java.io.Serializable;
import java.time.LocalDateTime;
import oopproject.users.User;

public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;
    private String action;
    private LocalDateTime date;

    public Log(User user, String action) {
        this.user = user;
        this.action = action;
        this.date = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        String actor = user == null ? "SYSTEM" : user.toString();
        return date + " " + actor + " - " + action;
    }
}
