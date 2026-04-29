package university.model;

import java.io.Serializable;
import java.time.LocalDate;

public class News implements Comparable<News>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String content;
    private final LocalDate date;

    public News(String title, String content, LocalDate date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public int compareTo(News other) {
        return other.date.compareTo(date);
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
