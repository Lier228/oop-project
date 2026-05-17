package oopproject.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Mark;
import oopproject.exceptions.AlreadyRegisteredException;
import oopproject.exceptions.CreditLimitExceededException;
import oopproject.research.Researcher;
import oopproject.research.ResearchProject;
import oopproject.research.ResearchService;
import oopproject.users.Admin;
import oopproject.users.Manager;
import oopproject.users.Student;
import oopproject.users.Teacher;
import oopproject.users.User;

public class University implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final University INSTANCE = new University();
    Admin admin = Admin.getInstance();

    private String name = "Research-Oriented University";
    public final List<User> users = new ArrayList<>(); //
    private final List<Course> courses = new ArrayList<>();
    private final List<Researcher> researchers = new ArrayList<>();
    private final List<ResearchProject> projects = new ArrayList<>();
    private final List<Log> logs = new ArrayList<>();

    private University() {
    }

    public static University getInstance() {
        return INSTANCE;
    }

//    public boolean addUser(User user) {
//        if (user == null || findUserById(user.getId()).isPresent() || findUserByUsername(user.getUsername()).isPresent()) {
//            return false;
//        }
//        users.add(user);
//        if (user instanceof Researcher researcher && researcher.isResearcher()) {
//            addResearcher(researcher);
//        }
//        addLog(user, "USER_REGISTERED");
//        return true;
//    }

//    public boolean addCourse(Course course) {
//        if (course == null || findCourseByCode(course.getCode()).isPresent()) {
//            return false;
//        }
//        courses.add(course);
//        addLog(null, "COURSE_ADDED " + course.getCode());
//        return true;
//    }

    public boolean openCourseForRegistration(String code) {
        Optional<Course> course = findCourseByCode(code);
        if (course.isEmpty()) {
            return false;
        }
        course.get().setOpen(true);
        addLog(null, "COURSE_OPENED " + course.get().getCode());
        return true;
    }
//
    public boolean closeCourseForRegistration(String code) {
        Optional<Course> course = findCourseByCode(code);
        if (course.isEmpty()) {
            return false;
        }
        course.get().setOpen(false);
        addLog(null, "COURSE_CLOSED " + course.get().getCode());
        return true;
    }
//
    public boolean assignTeacherToCourse(int teacherId, String courseCode) {
        Optional<User> user = findUserById(teacherId);
        Optional<Course> course = findCourseByCode(courseCode);
        if (user.isEmpty() || course.isEmpty() || !(user.get() instanceof Teacher teacher)) {
            return false;
        }
        teacher.addCourse(course.get());
        addLog(teacher, "TEACHER_ASSIGNED " + course.get().getCode());
        return true;
    }
//
    public boolean registerStudentToCourse(int studentId, String courseCode)
            throws CreditLimitExceededException, AlreadyRegisteredException {
        Optional<User> user = findUserById(studentId);
        Optional<Course> course = findCourseByCode(courseCode);
        if (user.isEmpty() || course.isEmpty() || !(user.get() instanceof Student student)) {
            return false;
        }
        student.registerCourse(course.get());
        addLog(student, "COURSE_REGISTERED " + course.get().getCode());
        return true;
    }
//
    public boolean putMark(int teacherId, int studentId, String courseCode, Mark mark) {
        Optional<User> teacherUser = findUserById(teacherId);
        Optional<User> studentUser = findUserById(studentId);
        Optional<Course> course = findCourseByCode(courseCode);
        if (teacherUser.isEmpty()
                || studentUser.isEmpty()
                || course.isEmpty()
                || !(teacherUser.get() instanceof Teacher teacher)
                || !(studentUser.get() instanceof Student student)
                || mark == null) {
            return false;
        }
        if (!course.get().getInstructors().contains(teacher)) {
            return false;
        }
        Enrollment enrollment = course.get().findEnrollment(student);
        if (enrollment == null) {
            return false;
        }
        teacher.putMark(enrollment, mark);
        addLog(teacher, "MARK_PUT " + course.get().getCode() + " studentId=" + student.getId());
        return true;
    }

//    public boolean addResearcher(Researcher researcher) {
//        if (researcher == null || !researcher.isResearcher() || researchers.contains(researcher)) {
//            return false;
//        }
//        researchers.add(researcher);
//        addLog(null, "RESEARCHER_REGISTERED " + researcher.getResearcherName());
//        return true;
//    }

    public boolean addProject(ResearchProject project) {
        if (project == null || findProjectByTopic(project.getTopic()).isPresent()) {
            return false;
        }
        projects.add(project);
        addLog(null, "RESEARCH_PROJECT_ADDED " + project.getTopic());
        return true;
    }

//    public boolean removeUserById(int id) {
//        Optional<User> user = findUserById(id);
//        if (user.isEmpty()) {
//            return false;
//        }
//        users.remove(user.get());
//        if (user.get() instanceof Researcher researcher) {
//            researchers.remove(researcher);
//        }
//        addLog(user.get(), "USER_REMOVED");
//        return true;
//    }

//    public boolean removeCourseByCode(String code) {
//        Optional<Course> course = findCourseByCode(code);
//        if (course.isEmpty()) {
//            return false;
//        }
//        courses.remove(course.get());
//        addLog(null, "COURSE_REMOVED " + course.get().getCode());
//        return true;
//    }

    public Optional<User> findUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public Optional<User> findUserByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return users.stream()
                .filter(user -> username.equalsIgnoreCase(user.getUsername()))
                .findFirst();
    }

    public Optional<Course> findCourseByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        return courses.stream()
                .filter(course -> code.equalsIgnoreCase(course.getCode()))
                .findFirst();
    }

    public Optional<ResearchProject> findProjectByTopic(String topic) {
        if (topic == null) {
            return Optional.empty();
        }
        return projects.stream()
                .filter(project -> topic.equalsIgnoreCase(project.getTopic()))
                .findFirst();
    }

    public Optional<Researcher> findTopCitedResearcher() {
        return ResearchService.findTopCitedResearcher(researchers);
    }

    public Optional<Researcher> findTopCitedResearcherOfSchool(String school) {
        return ResearchService.findTopCitedResearcherOfSchool(researchers, school);
    }

    public Optional<Researcher> findTopCitedResearcherOfYear(int year) {
        return ResearchService.findTopCitedResearcherOfYear(researchers, year);
    }

    public void addLog(User user, String action) {
        logs.add(new Log(user, action));
    }

    public void clear() {
        users.clear();
        courses.clear();
        researchers.clear();
        projects.clear();
        logs.clear();
    }

    void copyFrom(University saved) {
        if (saved == null) {
            return;
        }
        clear();
        name = saved.name;
        users.addAll(saved.users);
        courses.addAll(saved.courses);
        researchers.addAll(saved.researchers);
        projects.addAll(saved.projects);
        logs.addAll(saved.logs);
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public List<Course> getCoursesByStudent(Student student) {
        if (student == null) {
            return Collections.emptyList();
        }
        return courses.stream()
                .filter(course -> course.isStudentEnrolled(student))
                .toList();
    }

    public List<Course> getCoursesByTeacher(Teacher teacher) {
        if (teacher == null) {
            return Collections.emptyList();
        }
        return courses.stream()
                .filter(course -> course.getInstructors().contains(teacher))
                .toList();
    }

    public List<Researcher> getResearchers() {
        return Collections.unmodifiableList(researchers);
    }

    public List<ResearchProject> getProjects() {
        return Collections.unmodifiableList(projects);
    }

    public List<Log> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    public List<Log> getLogsByUser(User user) {
        if (user == null) {
            return Collections.emptyList();
        }
        return logs.stream()
                .filter(log -> user.equals(log.getUser()))
                .toList();
    }

    public List<Student> getStudents() {
        return users.stream()
                .filter(Student.class::isInstance)
                .map(Student.class::cast)
                .toList();
    }

    public List<Teacher> getTeachers() {
        return users.stream()
                .filter(Teacher.class::isInstance)
                .map(Teacher.class::cast)
                .toList();
    }

    public List<Manager> getManagers() {
        return users.stream()
                .filter(Manager.class::isInstance)
                .map(Manager.class::cast)
                .toList();
    }

    public List<Admin> getAdmins() {
        return users.stream()
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .toList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }
}
