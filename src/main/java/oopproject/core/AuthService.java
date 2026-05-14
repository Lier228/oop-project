package oopproject.core;

import java.util.Optional;
import oopproject.users.User;

public class AuthService {
    private final University university;
    private User currentUser;

    public AuthService(University university) {
        this.university = university;
    }

    public User login(String username, String password) {
        if (username == null || password == null) {
            university.addLog(null, "FAILED_LOGIN");
            return null;
        }

        Optional<User> authenticatedUser = university.getUsers().stream()
                .filter(user -> username.equalsIgnoreCase(user.getUsername()) && user.checkPassword(password))
                .findFirst();

        currentUser = authenticatedUser.orElse(null);
        if (currentUser != null) {
            university.addLog(currentUser, "LOGIN");
        } else {
            university.addLog(null, "FAILED_LOGIN " + username);
        }
        return currentUser;
    }

    public void logout(User user) {
        if (user != null) {
            university.addLog(user, "LOGOUT");
        }
        if (currentUser == user) {
            currentUser = null;
        }
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
