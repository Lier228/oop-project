package university.model;

import university.enums.LessonType;

import java.io.Serializable;
import java.time.LocalDate;

public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String lessonId;
    private final String topic;
    private final LessonType lessonType;
    private final LocalDate date;

    public Lesson(String lessonId, String topic, LessonType lessonType, LocalDate date) {
        this.lessonId = lessonId;
        this.topic = topic;
        this.lessonType = lessonType;
        this.date = date;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getTopic() {
        return topic;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public LocalDate getDate() {
        return date;
    }
}
