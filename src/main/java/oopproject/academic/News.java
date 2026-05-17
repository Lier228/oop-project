package oopproject.academic;

import java.io.Serializable;
import java.time.LocalDate;

public class News implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private LocalDate date;
    private String author;

    public News(String title, String content, LocalDate date) {
        this(title, content, date, "System");
    }

    public News(String title, String content, LocalDate date, String author) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
    }

    @Override
    public String toString() {
        return date + " - " + title + " by " + author + ": " + content;
    }
}
