package university.model;

import university.service.LogManager;

import java.io.Serializable;
import java.util.Objects;

public abstract class User implements Comparable<User>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private boolean loggedIn;

    protected User(String id, String username, String password, String name, String surname, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return name + " " + surname;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void login() {
        loggedIn = true;
        LogManager.getInstance().logAction(this, "logged in");
    }

    public void logout() {
        loggedIn = false;
        LogManager.getInstance().logAction(this, "logged out");
    }

    @Override
    public int compareTo(User other) {
        int bySurname = surname.compareToIgnoreCase(other.surname);
        if (bySurname != 0) {
            return bySurname;
        }
        int byName = name.compareToIgnoreCase(other.name);
        if (byName != 0) {
            return byName;
        }
        return id.compareToIgnoreCase(other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
