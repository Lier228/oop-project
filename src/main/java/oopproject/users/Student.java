package oopproject.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Schedule;
import oopproject.academic.StudyMaterial;
import oopproject.enums.UserType;
import oopproject.exceptions.AlreadyRegisteredException;
import oopproject.exceptions.CreditLimitExceededException;
import oopproject.exceptions.LowHIndexException;
import oopproject.research.Researcher;

public class Student extends User {
    public static final int MAX_CREDITS = 21;

    private double gpa;
    private int year;
    private int credits;
    private int failedCourses;
    private final UserType userType = UserType.STUDENT;
    private Schedule schedule = new Schedule(this);
    private final List<Enrollment> enrollments = new ArrayList<>();
    private Researcher researchSupervisor;

    public Student() {
    }

    public Student(int id, String username, String password, String email,
                   double gpa, int year, int credits, int failedCourses) {
        super(id, username, password, email);
        this.gpa = gpa;
        this.year = year;
        this.credits = credits;
        this.failedCourses = failedCourses;
    }

    public void registerCourse(Course course) throws CreditLimitExceededException, AlreadyRegisteredException {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null.");
        }
        if (!course.isOpen()) {
            throw new IllegalStateException("Course is not open for registration.");
        }
        boolean alreadyRegistered = enrollments.stream()
                .anyMatch(enrollment -> enrollment.getCourse().equals(course));
        if (alreadyRegistered || course.isStudentEnrolled(this)) {
            throw new AlreadyRegisteredException("Student is already registered for " + course.getCode() + ".");
        }
        if (credits + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitExceededException("Student cannot register for more than " + MAX_CREDITS + " credits.");
        }
        course.addStudent(this);
        Enrollment enrollment = course.findEnrollment(this);
        if (enrollment != null) {
            enrollments.add(enrollment);
        }
        credits += course.getCredits();
    }

    public List<Enrollment> viewTranscript() {
        return Collections.unmodifiableList(enrollments);
    }

    public void assignResearchSupervisor(Researcher supervisor) throws LowHIndexException {
        if (year >= 4 && (supervisor == null || supervisor.calculateHIndex() < 3)) {
            throw new LowHIndexException("Fourth-year student supervisor must have h-index at least 3.");
        }
        this.researchSupervisor = supervisor;
    }

    public Schedule viewSchedule() {
        return schedule;
    }

    public List<StudyMaterial> viewLearningFiles() {
        List<StudyMaterial> materials = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            // The model keeps the relation through courses; full file listing belongs to the service layer.
        }
        return materials;
    }

    public void solveTask(StudyMaterial task, String solutionText) {
        task.submitSolution(this, solutionText);
    }

    public UserType getUserType() {
        return userType;
    }

    public int getYear() {
        return year;
    }

    public double getGpa() {
        return gpa;
    }

    public int getCredits() {
        return credits;
    }

    public int getFailedCourses() {
        return failedCourses;
    }

    public Researcher getResearchSupervisor() {
        return researchSupervisor;
    }
}
