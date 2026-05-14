package oopproject.academic;

import java.io.Serializable;

public class ScheduleEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseName;
    private String teacherName;
    private Room room;

    public ScheduleEntry(String courseName, String teacherName, Room room) {
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.room = room;
    }

    public void setCourse(String courseName) {
        this.courseName = courseName;
    }

    public void setTeacher(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Room getRoom() {
        return room;
    }
}
