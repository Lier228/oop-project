package oopproject.academic;

import java.io.Serializable;
import oopproject.users.Student;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private Course course;
    private Marks marks;

    public Enrollment() {
    }

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Marks getMarks() {
        return marks;
    }

    public void setMarks(Marks marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        String markText = marks == null ? "not graded" : marks.toString();
        return course.getCode() + " - " + course.getName() + ": " + markText;
    }
}
