package oopproject.academic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import oopproject.users.Student;
import oopproject.users.Teacher;

public class StudyMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private String fileName;
    private LocalDate deadline;
    private boolean task;
    private Teacher uploadedBy;
    private Course course;
    private final Map<Integer, String> submissions = new HashMap<>();

    public StudyMaterial(String title, String description, String fileName, LocalDate deadline,
                         boolean task, Teacher uploadedBy, Course course) {
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.deadline = deadline;
        this.task = task;
        this.uploadedBy = uploadedBy;
        this.course = course;
    }

    public void submitSolution(Student student, String solutionText) {
        submissions.put(student.getId(), solutionText);
    }

    public String getSubmission(int studentId) {
        return submissions.get(studentId);
    }

    @Override
    public String toString() {
        return title + " (" + fileName + ")";
    }
}
