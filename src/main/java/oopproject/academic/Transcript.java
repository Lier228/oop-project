package oopproject.academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import oopproject.users.Student;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Student student;

    public Transcript(Student student) {
        this.student = student;
    }

    public List<Enrollment> getEnrollments() {
        if (student == null) {
            return Collections.emptyList();
        }
        return student.viewTranscript();
    }

    public List<Enrollment> getGradedEnrollments() {
        List<Enrollment> graded = new ArrayList<>();
        for (Enrollment enrollment : getEnrollments()) {
            if (enrollment.getMark() != null) {
                graded.add(enrollment);
            }
        }
        return Collections.unmodifiableList(graded);
    }

    public int getRegisteredCredits() {
        return getEnrollments().stream()
                .mapToInt(enrollment -> enrollment.getCourse().getCredits())
                .sum();
    }

    public int getCompletedCredits() {
        return getGradedEnrollments().stream()
                .filter(enrollment -> isPassed(enrollment.getMark()))
                .mapToInt(enrollment -> enrollment.getCourse().getCredits())
                .sum();
    }

    public double getAverageMark() {
        List<Enrollment> graded = getGradedEnrollments();
        if (graded.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Enrollment enrollment : graded) {
            total += enrollment.getMark().getTotal();
        }
        return total / graded.size();
    }

    public double calculateGpa() {
        List<Enrollment> graded = getGradedEnrollments();
        if (graded.isEmpty()) {
            return 0.0;
        }

        double weightedPoints = 0.0;
        int credits = 0;
        for (Enrollment enrollment : graded) {
            int courseCredits = enrollment.getCourse().getCredits();
            weightedPoints += getGradePoint(enrollment.getMark()) * courseCredits;
            credits += courseCredits;
        }
        return credits == 0 ? 0.0 : weightedPoints / credits;
    }

    public String getAcademicStanding() {
        double gpa = calculateGpa();
        if (gpa >= 3.5) {
            return "Excellent";
        }
        if (gpa >= 2.5) {
            return "Good";
        }
        if (gpa >= 2.0) {
            return "Satisfactory";
        }
        return "At risk";
    }

    public List<String> asLines() {
        List<String> lines = new ArrayList<>();
        lines.add("Transcript for " + student.getUsername());
        lines.add("Registered credits: " + getRegisteredCredits());
        lines.add("Completed credits: " + getCompletedCredits());
        lines.add(String.format(Locale.US, "GPA: %.2f", calculateGpa()));
        lines.add("Standing: " + getAcademicStanding());
        for (Enrollment enrollment : getEnrollments()) {
            lines.add(enrollment.toString());
        }
        return Collections.unmodifiableList(lines);
    }

    public boolean isPassed(Mark mark) {
        return mark != null && mark.getTotal() >= 50.0;
    }

    public double getGradePoint(Mark mark) {
        if (mark == null) {
            return 0.0;
        }
        double total = mark.getTotal();
        if (total >= 95) {
            return 4.0;
        }
        if (total >= 90) {
            return 3.67;
        }
        if (total >= 85) {
            return 3.33;
        }
        if (total >= 80) {
            return 3.0;
        }
        if (total >= 75) {
            return 2.67;
        }
        if (total >= 70) {
            return 2.33;
        }
        if (total >= 65) {
            return 2.0;
        }
        if (total >= 60) {
            return 1.67;
        }
        if (total >= 55) {
            return 1.33;
        }
        if (total >= 50) {
            return 1.0;
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), asLines());
    }
}
