package oopproject.users;

import oopproject.academic.Course;
import oopproject.core.Log;
import oopproject.core.University;
import oopproject.enums.UserType;
import oopproject.exceptions.AlreadyRegisteredException;
import oopproject.exceptions.CreditLimitExceededException;
import oopproject.research.Researcher;

import java.util.Optional;
public class Admin extends User {

    University uni = University.getInstance();
    private static final Admin INSTANCE = new Admin();

    public Admin() {
    }
    public static Admin getInstance() {
        return INSTANCE;
    }

    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email);
    }

    public boolean addUser(User user) {
        if (user == null || uni.findUserById(user.getId()).isPresent()) {
            return false;
        }
        uni.getUsers().add(user);
        if (user instanceof Researcher researcher && researcher.isResearcher()) {
            addResearcher(researcher);
        }
        uni.addLog(user, "USER_REGISTERED");
        return true;
    }

//    public Optional<User> findUserById(int id) {
//        return uni.getUsers().stream()
//                .filter(user -> user.getId() == id)
//                .findFirst();
//    }

    public boolean removeUserById(int id) {
        Optional<User> user = uni.findUserById(id);
        if (user.isEmpty()) {
            return false;
        }
        uni.getUsers().remove(user.get());
        if (user.get() instanceof Researcher researcher) {
            uni.getResearchers().remove(researcher);
        }
        uni.addLog(user.get(), "USER_REMOVED");
        return true;
    }

    public boolean addCourse(Course course) {
        if (course == null || uni.findCourseByCode(course.getCode()).isPresent()) {
            return false;
        }
        uni.getCourses().add(course);
        uni.addLog(null, "COURSE_ADDED " + course.getCode());
        return true;
    }

//    public Optional<Course> findCourseByCode(String code) {
//        if (code == null) {
//            return Optional.empty();
//        }
//        return uni.getCourses().stream()
//                .filter(course -> code.equalsIgnoreCase(course.getCode()))
//                .findFirst();
//    }

    public boolean removeCourseByCode(String code) {
        Optional<Course> course = uni.findCourseByCode(code);
        if (course.isEmpty()) {
            return false;
        }
        uni.getCourses().remove(course.get());
        uni.addLog(null, "COURSE_REMOVED " + course.get().getCode());
        return true;
    }

    public boolean addResearcher(Researcher researcher) {
        if (researcher == null || !researcher.isResearcher() || uni.getResearchers().contains(researcher)) {
            return false;
        }
        uni.getResearchers().add(researcher);
        uni.addLog(null, "RESEARCHER_REGISTERED " + researcher.getResearcherName());
        return true;
    }

    // Доделать то что ниже:

    public void updateLogs() {
        System.out.println("Logs updated");
    }

    public void blockUser(User user) {
        System.out.println(user.username + " blocked");
    }

    public void assignRole(User user, UserType role) {
        System.out.println("Role assigned to " + user.username);
    }
}

//
