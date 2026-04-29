package university.service;

import university.model.University;
import university.model.User;

public class AuthService {
    private static final AuthService INSTANCE = new AuthService();

    private AuthService() {
    }

    public static AuthService getInstance() {
        return INSTANCE;
    }

    public User authenticate(String username, String password) {
        User user = University.getInstance()
                .findUserByUsername(username)
                .orElse(null);

        if (user != null && user.getPassword().equals(password)) {
            user.login();
            return user;
        }

        return null;
    }
}
