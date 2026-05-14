package oopproject.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import oopproject.academic.News;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String username;
    protected String password;
    protected String email;
    protected final List<News> inboxNews = new ArrayList<>();

    public User() {
    }

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public boolean checkPassword(String password) {
        return this.password != null && this.password.equals(password);
    }

    public void receiveNews(News news) {
        inboxNews.add(news);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<News> getInboxNews() {
        return Collections.unmodifiableList(inboxNews);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof User user)) {
            return false;
        }
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + ": " + username + " <" + email + ">";
    }
}
