package endtermProject.src.university.service;

import endtermProject.src.university.model.Course;
import endtermProject.src.university.model.Mark;
import endtermProject.src.university.model.Student;

import java.util.Collection;

public class ReportGenerator {
    private static final ReportGenerator INSTANCE = new ReportGenerator();

    private ReportGenerator() {
    }

    public static ReportGenerator getInstance() {
        return INSTANCE;
    }

    public String generateStudentPerformanceReport(Collection<Student> students) {
        StringBuilder builder = new StringBuilder("Student Performance Report\n");
        for (Student student : students) {
            student.refreshGpa();
            builder.append("- ")
                    .append(student.getFullName())
                    .append(" | major=")
                    .append(student.getMajor())
                    .append(" | GPA=")
                    .append(String.format("%.2f", student.getGpa()))
                    .append(" | credits=")
                    .append(student.getCredits())
                    .append('\n');
        }
        return builder.toString();
    }

    public String generateMarkStatistics(Course course) {
        double average = course.getAverageMark();
        long passed = course.getMarks().values().stream().filter(Mark::isPassed).count();
        int total = course.getMarks().size();

        return "Mark Statistics for " + course.getCode() + System.lineSeparator() +
                "Average total: " + String.format("%.2f", average) + System.lineSeparator() +
                "Passed: " + passed + "/" + total;
    }
}
