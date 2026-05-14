package oopproject.users;

import java.util.Scanner;
import oopproject.enums.UserType;

public class Admin extends User {
    private final transient Scanner scanner = new Scanner(System.in);

    public Admin() {
    }

    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email);
    }

    public void addUser() {
    }

    public void removeUser() {
    }

    public void updateLogs() {
    }

    public void blockUser(User user) {
    }

    public void assignRole(User user, UserType role) {
    }
}
