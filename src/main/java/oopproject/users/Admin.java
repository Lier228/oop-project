package oopproject.users;

import java.util.Optional;
import oopproject.academic.Course;
import oopproject.core.University;
import oopproject.enums.UserType;
import oopproject.research.Researcher;

public class Admin extends User {
    private final transient University university = University.getInstance();

    public Admin() {
        this.role = UserType.ADMIN;
    }

    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email, UserType.ADMIN);
    }

    public boolean addUser(User user) {
        return university.addUser(user);
    }

    public Optional<User> findUserById(int id) {
        return university.findUserById(id);
    }

    public boolean removeUserById(int id) {
        return university.removeUserById(id);
    }

    public boolean addCourse(Course course) {
        return university.addCourse(course);
    }

    public Optional<Course> findCourseByCode(String code) {
        return university.findCourseByCode(code);
    }

    public boolean removeCourseByCode(String code) {
        return university.removeCourseByCode(code);
    }

    public boolean addResearcher(Researcher researcher) {
        return university.addResearcher(researcher);
    }

    public void updateLogs() {
        university.addLog(this, "LOGS_UPDATED");
    }

    public void blockUser(User user) {
        if (user != null) {
            user.block();
            university.addLog(this, "USER_BLOCKED " + user.getUsername());
        }
    }

    public void unblockUser(User user) {
        if (user != null) {
            user.unblock();
            university.addLog(this, "USER_UNBLOCKED " + user.getUsername());
        }
    }

    public void assignRole(User user, UserType role) {
        if (user != null && role != null) {
            user.role = role;
            university.addLog(this, "ROLE_ASSIGNED " + user.getUsername() + " " + role);
        }
    }
}
