package university.model;

import university.exceptions.CreditLimitExceededException;
import university.exceptions.LowHIndexException;
import university.exceptions.TooManyFailsException;
import university.service.LogManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Student extends User implements Researcher {
    private static final long serialVersionUID = 1L;
    private static final int MAX_CREDITS = 21;

    private final int year;
    private double gpa;
    private final String major;
    private int credits;
    private final Transcript transcript = new Transcript();
    private final List<Registration> registrations = new ArrayList<>();
    private final ResearchProfile researchProfile = new ResearchProfile();
    private Researcher researchSupervisor;

    public Student(
            String id,
            String username,
            String password,
            String name,
            String surname,
            String email,
            int year,
            String major
    ) {
        super(id, username, password, name, surname, email);
        this.year = year;
        this.major = major;
    }

    public int getYear() {
        return year;
    }

    public double getGpa() {
        return gpa;
    }

    public String getMajor() {
        return major;
    }

    public int getCredits() {
        return credits;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public List<Registration> getRegistrations() {
        return Collections.unmodifiableList(registrations);
    }

    public Researcher getResearchSupervisor() {
        return researchSupervisor;
    }

    public Registration registerForCourse(Course course)
            throws CreditLimitExceededException, TooManyFailsException {
        if (credits + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitExceededException("Credit limit exceeded for student " + getFullName());
        }
        if (transcript.getFailedCoursesCount() >= 3) {
            throw new TooManyFailsException("Student " + getFullName() + " has too many failed courses.");
        }
        Registration registration = new Registration(this, course);
        registrations.add(registration);
        LogManager.getInstance().logAction(this, "requested registration for " + course.getCode());
        return registration;
    }

    public Map<Course, Mark> viewMarks() {
        return transcript.getCourseMarks();
    }

    public String viewTranscript() {
        refreshGpa();
        return transcript.printTranscript();
    }

    public void rateTeacher(Teacher teacher, int rate) {
        if (rate < 1 || rate > 5) {
            throw new IllegalArgumentException("Teacher rate must be in range 1..5.");
        }
        teacher.addRating(this, rate);
        LogManager.getInstance().logAction(this, "rated teacher " + teacher.getFullName() + " with " + rate);
    }

    public void assignResearchSupervisor(Researcher supervisor) throws LowHIndexException {
        if (year != 4) {
            throw new LowHIndexException("Only 4th year students can have a research supervisor.");
        }
        if (supervisor == null || !supervisor.isResearcher() || supervisor.getHIndex() < 3) {
            throw new LowHIndexException("Supervisor must be an active researcher with h-index >= 3.");
        }
        this.researchSupervisor = supervisor;
        LogManager.getInstance().logAction(this, "assigned research supervisor " + supervisor.getResearcherName());
    }

    public void refreshGpa() {
        gpa = transcript.calculateGPA();
    }

    void onRegistrationApproved(Course course) {
        transcript.registerCourse(course);
        credits += course.getCredits();
        LogManager.getInstance().logAction(this, "registration approved for " + course.getCode());
    }

    @Override
    public ResearchProfile getResearchProfile() {
        return researchProfile;
    }

    @Override
    public String getResearcherName() {
        return getFullName();
    }
}
