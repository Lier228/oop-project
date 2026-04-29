package university.model;

import university.enums.TeacherTitle;
import university.service.LogManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;

    private final TeacherTitle title;
    private final List<Course> courses = new ArrayList<>();
    private final Map<String, Integer> studentRatings = new LinkedHashMap<>();

    public Teacher(
            String id,
            String username,
            String password,
            String name,
            String surname,
            String email,
            double salary,
            String department,
            TeacherTitle title
    ) {
        super(id, username, password, name, surname, email, salary, department);
        this.title = title;
        if (title == TeacherTitle.PROFESSOR) {
            activateResearchProfile(3);
        }
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public List<Course> viewCourses() {
        return Collections.unmodifiableList(courses);
    }

    public void putMark(Student student, Course course, Mark mark) {
        if (!courses.contains(course)) {
            throw new IllegalArgumentException("Teacher is not assigned to this course.");
        }
        course.assignMark(student, mark);
        student.getTranscript().addMark(course, mark);
        student.refreshGpa();
        LogManager.getInstance().logAction(this, "put mark for " + student.getFullName() + " in " + course.getCode());
    }

    public void manageCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            course.addTeacher(this);
            LogManager.getInstance().logAction(this, "started managing course " + course.getCode());
        }
    }

    void addRating(Student student, int rate) {
        studentRatings.put(student.getId(), rate);
    }

    public double getAverageRating() {
        return studentRatings.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}
