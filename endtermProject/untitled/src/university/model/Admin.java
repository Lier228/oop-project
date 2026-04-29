package university.model;

import university.service.LogManager;

import java.util.List;

public class Admin extends Employee {
    private static final long serialVersionUID = 1L;

    public Admin(
            String id,
            String username,
            String password,
            String name,
            String surname,
            String email,
            double salary,
            String department
    ) {
        super(id, username, password, name, surname, email, salary, department);
    }

    public void addUser(User user) {
        University.getInstance().addUser(user);
        LogManager.getInstance().logAction(this, "added user " + user.getFullName());
    }

    public void removeUser(User user) {
        University.getInstance().removeUser(user);
        LogManager.getInstance().logAction(this, "removed user " + user.getFullName());
    }

    public void updateUser(User user) {
        University.getInstance().updateUser(user);
        LogManager.getInstance().logAction(this, "updated user " + user.getFullName());
    }

    public List<String> seeLogFiles() {
        return LogManager.getInstance().viewLogs();
    }
}
