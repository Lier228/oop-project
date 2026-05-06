package endtermProject.src.university.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Course, Mark> courseMarks = new LinkedHashMap<>();

    public void registerCourse(Course course) {
        courseMarks.putIfAbsent(course, null);
    }

    public void addMark(Course course, Mark mark) {
        courseMarks.put(course, mark);
    }

    public Map<Course, Mark> getCourseMarks() {
        return Collections.unmodifiableMap(courseMarks);
    }

    public double calculateGPA() {
        double weightedScore = 0.0;
        int totalCredits = 0;

        for (Map.Entry<Course, Mark> entry : courseMarks.entrySet()) {
            Mark mark = entry.getValue();
            if (mark == null) {
                continue;
            }
            int credits = entry.getKey().getCredits();
            weightedScore += toGradePoint(mark.calculateTotal()) * credits;
            totalCredits += credits;
        }

        return totalCredits == 0 ? 0.0 : weightedScore / totalCredits;
    }

    public int getFailedCoursesCount() {
        int fails = 0;
        for (Mark mark : courseMarks.values()) {
            if (mark != null && !mark.isPassed()) {
                fails++;
            }
        }
        return fails;
    }

    public String printTranscript() {
        StringBuilder builder = new StringBuilder("Transcript:\n");
        for (Map.Entry<Course, Mark> entry : courseMarks.entrySet()) {
            builder.append("- ")
                    .append(entry.getKey().getCode())
                    .append(" / ")
                    .append(entry.getKey().getName())
                    .append(": ")
                    .append(entry.getValue() == null ? "in progress" : entry.getValue())
                    .append('\n');
        }
        builder.append("GPA: ").append(String.format("%.2f", calculateGPA()));
        return builder.toString();
    }

    private double toGradePoint(double totalScore) {
        if (totalScore >= 95) {
            return 4.0;
        }
        if (totalScore >= 90) {
            return 3.67;
        }
        if (totalScore >= 85) {
            return 3.33;
        }
        if (totalScore >= 80) {
            return 3.0;
        }
        if (totalScore >= 75) {
            return 2.67;
        }
        if (totalScore >= 70) {
            return 2.33;
        }
        if (totalScore >= 65) {
            return 2.0;
        }
        if (totalScore >= 60) {
            return 1.0;
        }
        return 0.0;
    }
}
