package oopproject.academic;

import oopproject.enums.LessonType;

public class Lesson {
    private TimeSlot time;
    private String room;
    private LessonType type;

    public Lesson(TimeSlot time, String room, LessonType type) {
        this.time = time;
        this.room = room;
        this.type = type;
    }

    public String getDisplayText() {
        return type + " at " + room + " (" + time.getDay() + " " + time.getStartTime() + "-" + time.getEndTime() + ")";
    }

    public TimeSlot getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public LessonType getType() {
        return type;
    }
}
