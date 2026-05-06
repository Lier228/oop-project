package endtermProject.src.university.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String text;
    private final LocalDate date;
    private final String sender;

    public Message(String text, LocalDate date, String sender) {
        this.text = text;
        this.date = date;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + sender + ": " + text;
    }
}
