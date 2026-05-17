package oopproject.users;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import oopproject.academic.News;
import oopproject.academic.Request;
import oopproject.core.Message;
import oopproject.core.MessageMediator;
import oopproject.core.University;
import oopproject.enums.UserType;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String username;
    protected String password;
    protected String email;
    protected UserType role;
    protected boolean active = true;
    protected final List<News> inboxNews = new ArrayList<>();
    protected Deque<Message> inboxMessages = new ArrayDeque<>();

    public User() {
    }

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(int id, String username, String password, String email, UserType role) {
        this(id, username, password, email);
        this.role = role;
    }

    public boolean checkPassword(String password) {
        return active && this.password != null && this.password.equals(password);
    }

    public void receiveNews(News news) {
        if (news != null) {
            inboxNews.add(news);
        }
    }

    public boolean sendMessage(User recipient, String content, MessageMediator mediator) {
        return mediator != null && mediator.sendMessage(this, recipient, content);
    }

    public void receiveMessage(Message message) {
        if (message != null) {
            directMessageInbox().offer(message);
        }
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!checkPassword(oldPassword) || newPassword == null || newPassword.isBlank()) {
            return false;
        }
        password = newPassword;
        return true;
    }

    public void updateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        this.email = email;
    }

    public void block() {
        active = false;
    }

    public void unblock() {
        active = true;
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

    public UserType getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isBlocked() {
        return !active;
    }

    public List<News> getInboxNews() {
        return Collections.unmodifiableList(inboxNews);
    }

    public List<Message> getInboxMessages() {
        return Collections.unmodifiableList(new ArrayList<>(directMessageInbox()));
    }

    public List<Request> viewCreatedRequests() {
        return University.getInstance().getRequestsBySender(this);
    }

    private Deque<Message> directMessageInbox() {
        if (inboxMessages == null) {
            inboxMessages = new ArrayDeque<>();
        }
        return inboxMessages;
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
        return id + ": " + username + " <" + email + ">"
                + " role=" + role
                + ", active=" + active;
    }
}
