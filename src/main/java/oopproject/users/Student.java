package oopproject.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Marks;
import oopproject.academic.Schedule;
import oopproject.academic.StudyMaterial;
import oopproject.academic.Transcript;
import oopproject.core.University;
import oopproject.enums.UserType;
import oopproject.exceptions.AlreadyRegisteredException;
import oopproject.exceptions.CreditLimitExceededException;
import oopproject.exceptions.LowHIndexException;
import oopproject.exceptions.NonResearcherException;
import oopproject.research.ResearchPaper;
import oopproject.research.ResearchProject;
import oopproject.research.Researcher;
import oopproject.research.ResearcherProfile;

public class Student extends User implements Researcher {
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
    private ResearcherProfile researcherProfile;

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

    public List<Marks> viewMarks() {
        return enrollments.stream()
                .map(Enrollment::getMarks)
                .filter(marks -> marks != null)
                .toList();
    }

    public List<Course> getRegisteredCourses() {
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .toList();
    }

    public List<Course> getOpenCoursesForRegistration() {
        return University.getInstance().getCourses().stream()
                .filter(Course::isOpen)
                .toList();
    }

    public boolean registerForCourse(String courseCode)
            throws CreditLimitExceededException, AlreadyRegisteredException {
        return University.getInstance().registerStudentToCourse(getId(), courseCode);
    }

    public List<Teacher> getCourseTeachers(Course course) {
        if (course == null || !isRegisteredFor(course)) {
            return List.of();
        }
        return course.getInstructors();
    }

    public Course findRegisteredCourseByCode(String courseCode) {
        if (courseCode == null) {
            return null;
        }
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .filter(course -> courseCode.equalsIgnoreCase(course.getCode()))
                .findFirst()
                .orElse(null);
    }

    public List<String> getMarksSummaryLines() {
        return enrollments.stream()
                .map(enrollment -> enrollment.getCourse().getCode() + " - "
                        + enrollment.getCourse().getName() + ": "
                        + (enrollment.getMarks() == null ? "not graded" : enrollment.getMarks().toString()))
                .collect(Collectors.toList());
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

    public boolean rateTeacherForCourse(Course course, Teacher teacher, int rating) {
        if (course == null || teacher == null || !isRegisteredFor(course) || !course.getInstructors().contains(teacher)) {
            return false;
        }
        rateTeacher(teacher, rating);
        return true;
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

    public boolean becomeResearcher(String school) {
        if (!isActive()) {
            return false;
        }
        String normalizedSchool = normalizeResearchSchool(school);
        if (researcherProfile == null) {
            researcherProfile = new ResearcherProfile(this, normalizedSchool);
        } else {
            researcherProfile.setSchool(normalizedSchool);
        }
        trackResearcherIfRegistered();
        return true;
    }

    public boolean joinResearchProject(ResearchProject project) throws NonResearcherException {
        if (project == null) {
            return false;
        }
        boolean joined = project.addParticipant(this);
        if (joined) {
            trackResearcherIfRegistered();
        }
        return joined;
    }

    public boolean leaveResearchProject(ResearchProject project) {
        return project != null && project.deleteParticipant(this);
    }

    @Override
    public boolean addResearchPaper(ResearchPaper paper) {
        if (paper == null || !isActive()) {
            return false;
        }
        boolean added = ensureResearcherProfile().addResearchPaper(paper);
        if (added) {
            trackResearcherIfRegistered();
        }
        return added;
    }

    @Override
    public boolean removeResearchPaper(ResearchPaper paper) {
        return researcherProfile != null && researcherProfile.removeResearchPaper(paper);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return researcherProfile == null ? List.of() : researcherProfile.getResearchPapers();
    }

    @Override
    public Set<ResearchProject> getResearchProjects() {
        return researcherProfile == null ? Set.of() : researcherProfile.getResearchProjects();
    }

    @Override
    public void attachResearchProject(ResearchProject project) {
        if (project != null) {
            ensureResearcherProfile().attachResearchProject(project);
        }
    }

    @Override
    public void detachResearchProject(ResearchProject project) {
        if (researcherProfile != null) {
            researcherProfile.detachResearchProject(project);
        }
    }

    @Override
    public String getResearcherName() {
        return getUsername();
    }

    @Override
    public boolean isResearcher() {
        return researcherProfile != null && isActive();
    }

    @Override
    public String getResearchSchool() {
        return researcherProfile == null ? "UNSPECIFIED" : researcherProfile.getResearchSchool();
    }

    private ResearcherProfile ensureResearcherProfile() {
        if (researcherProfile == null) {
            researcherProfile = new ResearcherProfile(this, normalizeResearchSchool(null));
        }
        return researcherProfile;
    }

    private String normalizeResearchSchool(String school) {
        return school == null || school.isBlank() ? "UNSPECIFIED" : school;
    }

    private void trackResearcherIfRegistered() {
        University university = University.getInstance();
        if (university.findUserById(getId()).isPresent()) {
            university.addResearcher(this);
        }
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
