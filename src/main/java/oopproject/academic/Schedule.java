package oopproject.academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oopproject.users.Student;

public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private final List<ScheduleEntry> scheduleEntries = new ArrayList<>();

    public Schedule() {
    }

    public Schedule(Student student) {
        this.student = student;
    }

    public void addScheduleEntry(ScheduleEntry entry) {
        scheduleEntries.add(entry);
    }

    public void removeScheduleEntry(ScheduleEntry entry) {
        scheduleEntries.remove(entry);
    }

    public List<ScheduleEntry> getScheduleEntries() {
        return Collections.unmodifiableList(scheduleEntries);
    }

    public Student getStudent() {
        return student;
    }
}
