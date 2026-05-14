package oopproject.academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oopproject.users.Student;
import oopproject.users.Teacher;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String code;
    private int credits;
    private boolean open;
    private final List<Student> students = new ArrayList<>();
    private final List<Teacher> instructors = new ArrayList<>();
    private final List<Lesson> lessons = new ArrayList<>();
    private final List<StudyMaterial> materials = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    public Course(String name, String code, int credits) {
        this.name = name;
        this.code = code;
        this.credits = credits;
    }

    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
            enrollments.add(new Enrollment(student, this));
        }
    }

    public void addInstructor(Teacher teacher) {
        if (!instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void addMaterial(StudyMaterial material) {
        materials.add(material);
    }

    public Enrollment findEnrollment(Student student) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getStudent().equals(student))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getCredits() {
        return credits;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public List<Teacher> getInstructors() {
        return Collections.unmodifiableList(instructors);
    }
}
