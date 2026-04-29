package university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Course implements Comparable<Course>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String name;
    private final int credits;
    private final String major;
    private final int yearOfStudy;
    private final List<Teacher> instructors = new ArrayList<>();
    private final List<Student> enrolledStudents = new ArrayList<>();
    private final List<Lesson> lessons = new ArrayList<>();
    private final Map<Student, Mark> marks = new LinkedHashMap<>();

    public Course(String code, String name, int credits, String major, int yearOfStudy) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.major = major;
        this.yearOfStudy = yearOfStudy;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getMajor() {
        return major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public List<Teacher> getInstructors() {
        return Collections.unmodifiableList(instructors);
    }

    public List<Student> getEnrolledStudents() {
        return Collections.unmodifiableList(enrolledStudents);
    }

    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    public Map<Student, Mark> getMarks() {
        return Collections.unmodifiableMap(marks);
    }

    public void addTeacher(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public void addLesson(Lesson lesson) {
        if (lesson != null) {
            lessons.add(lesson);
        }
    }

    public void enrollStudent(Student student) {
        if (student != null && !enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            student.onRegistrationApproved(this);
        }
    }

    public void assignMark(Student student, Mark mark) {
        if (!enrolledStudents.contains(student)) {
            throw new IllegalArgumentException("Student is not enrolled in this course.");
        }
        marks.put(student, mark);
    }

    public double getAverageMark() {
        return marks.values().stream()
                .mapToDouble(Mark::calculateTotal)
                .average()
                .orElse(0.0);
    }

    @Override
    public int compareTo(Course other) {
        return code.compareToIgnoreCase(other.code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + credits + " credits)";
    }
}
