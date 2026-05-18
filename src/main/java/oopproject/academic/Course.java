package oopproject.academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import oopproject.users.Student;
import oopproject.users.Teacher;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String code;
    private int credits;
    private boolean open;
    private String targetMajor;
    private Integer targetYear;
    private final List<Teacher> instructors = new ArrayList<>();
    private final List<Lesson> lessons = new ArrayList<>();
    private final List<StudyMaterial> materials = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    public Course(String name, String code, int credits) {
        this(name, code, credits, null, null);
    }

    public Course(String name, String code, int credits, String targetMajor, Integer targetYear) {
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.targetMajor = targetMajor;
        this.targetYear = targetYear;
    }

    public void addStudent(Student student) {
        if (student != null && findEnrollment(student) == null) {
            enrollments.add(new Enrollment(student, this));
        }
    }

    public void addInstructor(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public void addLesson(Lesson lesson) {
        if (lesson != null) {
            lessons.add(lesson);
        }
    }

    public void addMaterial(StudyMaterial material) {
        if (material != null) {
            materials.add(material);
        }
    }

    public Enrollment findEnrollment(Student student) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getStudent().equals(student))
                .findFirst()
                .orElse(null);
    }

    public boolean isStudentEnrolled(Student student) {
        return findEnrollment(student) != null;
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    public List<StudyMaterial> getMaterials() {
        return Collections.unmodifiableList(materials);
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

    public String getTargetMajor() {
        return targetMajor;
    }

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setTargetMajor(String targetMajor) {
        this.targetMajor = targetMajor;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }

    public List<Teacher> getInstructors() {
        return Collections.unmodifiableList(instructors);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Course course)) {
            return false;
        }
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        String audience = targetMajor == null && targetYear == null
                ? ""
                : ", target=" + (targetMajor == null ? "any major" : targetMajor)
                + (targetYear == null ? "" : " year " + targetYear);
        return code + " - " + name + " (" + credits + " credits, open=" + open + audience + ")";
    }
}
