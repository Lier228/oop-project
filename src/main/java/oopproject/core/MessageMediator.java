package oopproject.core;

import oopproject.users.User;

public interface MessageMediator {
    boolean sendMessage(User sender, User recipient, String content);
}
