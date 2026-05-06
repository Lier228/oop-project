package oopproject.users;

import oopproject.enums.UserType;

public final class UserFactory {
    private UserFactory() {
    }

    public static User createUser(UserType type, int id, String username, String password, String email) {
        return switch (type) {
            case ADMIN -> new Admin(id, username, password, email);
            case STUDENT -> new Student(id, username, password, email, 0.0, 1, 0, 0);
            case TEACHER -> new Teacher(id, username, password, email, 0.0, null, null);
            case MANAGER -> new Manager(id, username, password, email, 0.0, null, null);
        };
    }
}
