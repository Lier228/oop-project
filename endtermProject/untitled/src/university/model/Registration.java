package university.model;

import university.enums.RegistrationStatus;

import java.io.Serializable;
import java.time.LocalDate;

public class Registration implements Serializable {
    private static final long serialVersionUID = 1L;

    private RegistrationStatus status;
    private final LocalDate date;
    private final Student student;
    private final Course course;

    public Registration(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.status = RegistrationStatus.PENDING;
        this.date = LocalDate.now();
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public void approve() {
        status = RegistrationStatus.APPROVED;
    }

    public void reject() {
        status = RegistrationStatus.REJECTED;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "status=" + status +
                ", date=" + date +
                ", student=" + student.getFullName() +
                ", course=" + course.getCode() +
                '}';
    }
}
