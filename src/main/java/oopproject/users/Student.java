package oopproject.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Mark;
import oopproject.academic.Schedule;
import oopproject.academic.StudyMaterial;
import oopproject.academic.Transcript;
import oopproject.enums.UserType;
import oopproject.exceptions.AlreadyRegisteredException;
import oopproject.exceptions.CreditLimitExceededException;
import oopproject.exceptions.LowHIndexException;
import oopproject.research.Researcher;

public class Student extends User {
    private static final long serialVersionUID = 1L;

    public static final int MAX_CREDITS = 21;

    private double gpa;
    private int year;
    private int credits;
    private int failedCourses;
    private final UserType userType = UserType.STUDENT;
    private Schedule schedule = new Schedule(this);
    private final List<Enrollment> enrollments = new ArrayList<>();
    private final Map<Integer, Integer> teacherRatings = new HashMap<>();
    private Transcript transcript = new Transcript(this);
    private Researcher researchSupervisor;

    public Student() {
        this.role = UserType.STUDENT;
    }

    public Student(int id, String username, String password, String email,
                   double gpa, int year, int credits, int failedCourses) {
        super(id, username, password, email, UserType.STUDENT);
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

    public Transcript getTranscript() {
        if (transcript == null) {
            transcript = new Transcript(this);
        }
        return transcript;
    }

    public List<Mark> viewMarks() {
        return enrollments.stream()
                .map(Enrollment::getMark)
                .filter(mark -> mark != null)
                .toList();
    }

    public List<Course> getRegisteredCourses() {
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .toList();
    }

    public boolean isRegisteredFor(Course course) {
        if (course == null) {
            return false;
        }
        return enrollments.stream()
                .anyMatch(enrollment -> enrollment.getCourse().equals(course));
    }

    public double recalculateGpa() {
        gpa = getTranscript().calculateGpa();
        return gpa;
    }

    public int getCompletedCredits() {
        return getTranscript().getCompletedCredits();
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
            materials.addAll(enrollment.getCourse().getMaterials());
        }
        return materials;
    }

    public void solveTask(StudyMaterial task, String solutionText) {
        task.submitSolution(this, solutionText);
    }

    public void rateTeacher(Teacher teacher, int rating) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null.");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Teacher rating must be from 1 to 5.");
        }
        teacherRatings.put(teacher.getId(), rating);
    }

    public Map<Integer, Integer> getTeacherRatings() {
        return Collections.unmodifiableMap(teacherRatings);
    }

    public void incrementFailedCourses() {
        failedCourses++;
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

    @Override
    public String toString() {
        return getId() + ": " + getUsername()
                + " year=" + year
                + ", gpa=" + String.format(Locale.US, "%.2f", gpa)
                + ", registeredCredits=" + credits
                + ", completedCredits=" + getCompletedCredits()
                + ", role=" + getRole()
                + ", active=" + isActive();
    }
}
