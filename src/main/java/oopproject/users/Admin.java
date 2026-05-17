package oopproject.users;

import java.util.Scanner;
import oopproject.enums.UserType;

public class Admin extends User {
    private final transient Scanner scanner = new Scanner(System.in);

    public Admin() {
        this.role = UserType.ADMIN;
    }

    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email, UserType.ADMIN);
    }

    public void addUser() {
    }

    public void removeUser() {
    }

    public void updateLogs() {
    }

    public void blockUser(User user) {
        if (user != null) {
            user.block();
        }
    }

    public void unblockUser(User user) {
        if (user != null) {
            user.unblock();
        }
    }

    public void assignRole(User user, UserType role) {
        if (user != null && role != null) {
            user.role = role;
        }
    }
}
