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

    private StudyMaterial(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.fileName = builder.fileName;
        this.deadline = builder.task ? builder.deadline : null;
        this.task = builder.task;
        this.uploadedBy = builder.uploadedBy;
        this.course = builder.course;
    }

    public static Builder builder() {
        return new Builder();
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

    public static final class Builder {
        private String title;
        private String description;
        private String fileName;
        private LocalDate deadline;
        private boolean task;
        private Teacher uploadedBy;
        private Course course;

        public Builder title(String title) {
            this.title = normalize(title);
            return this;
        }

        public Builder description(String description) {
            this.description = normalize(description);
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = normalize(fileName);
            return this;
        }

        public Builder deadline(LocalDate deadline) {
            this.deadline = deadline;
            return this;
        }

        public Builder task(boolean task) {
            this.task = task;
            return this;
        }

        public Builder uploadedBy(Teacher uploadedBy) {
            this.uploadedBy = uploadedBy;
            return this;
        }

        public Builder course(Course course) {
            this.course = course;
            return this;
        }

        public StudyMaterial build() {
            if (title == null || fileName == null || course == null) {
                throw new IllegalStateException("Study material requires title, file name, and course.");
            }
            if (task && deadline == null) {
                throw new IllegalStateException("Task study material requires a deadline.");
            }
            return new StudyMaterial(this);
        }

        private String normalize(String value) {
            if (value == null) {
                return null;
            }
            String trimmed = value.trim();
            return trimmed.isEmpty() ? null : trimmed;
        }
    }
}
