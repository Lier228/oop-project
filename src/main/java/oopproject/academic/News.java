package oopproject.academic;

import java.time.LocalDate;

public class News {
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
