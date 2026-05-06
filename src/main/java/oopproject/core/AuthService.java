package oopproject.core;

import oopproject.users.User;

public class AuthService {
    private final University university;

    public AuthService(University university) {
        this.university = university;
    }

    public User login(String username, String password) {
        return university.getUsers().stream()
                .filter(user -> user.getUsername().equals(username) && user.checkPassword(password))
                .findFirst()
                .orElse(null);
    }

    public void logout(User user) {
    }
}
