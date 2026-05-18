package oopproject.users;

import java.util.List;
import oopproject.academic.Course;
import oopproject.core.Log;
import oopproject.core.University;
import oopproject.enums.UserType;
import oopproject.research.Researcher;

public class Admin extends User {

    University uni = University.getInstance();
    private static final Admin INSTANCE = new Admin();

    public Admin() {
        this.role = UserType.ADMIN;
    }

    public static Admin getInstance() {
        return INSTANCE;
    }

    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email, UserType.ADMIN);
    }

    public boolean addUser(User user) {
        return uni.addUser(user);
    }

    public boolean removeUserById(int id) {
        return uni.removeUserById(id);
    }

    public boolean addCourse(Course course) {
        return uni.addCourse(course);
    }

    public boolean removeCourseByCode(String code) {
        return uni.removeCourseByCode(code);
    }

    public boolean addResearcher(Researcher researcher) {
        if (researcher == null || !researcher.isResearcher() || uni.getResearchers().contains(researcher)) {
            return false;
        }
        uni.getResearchers().add(researcher);
        uni.addLog(null, "RESEARCHER_REGISTERED " + researcher.getResearcherName());
        return true;
    }

    public void updateLogs() {
        System.out.println("Logs updated");
        uni.addLog(this, "LOGS_UPDATED");
    }

    public List<User> showUsers() {
        return uni.getUsers();
    }

    public List<Log> showLogs() {
        return uni.getLogs();
    }

    public boolean setUserActive(User user, boolean active) {
        if (user == null) {
            return false;
        }
        if (active) {
            user.unblock();
            uni.addLog(this, "USER_UNBLOCKED " + user.username);
        } else {
            user.block();
            uni.addLog(this, "USER_BLOCKED " + user.username);
        }
        return true;
    }

    public void blockUser(User user) {
        if (user != null) {
            user.block();
            uni.addLog(this, "USER_BLOCKED " + user.username);
            System.out.println(user.username + " blocked");
        }
    }

    public void unblockUser(User user) {
        if (user != null) {
            user.unblock();
            uni.addLog(this, "USER_UNBLOCKED " + user.username);
            System.out.println(user.username + " unblocked");
        }
    }

    public void assignRole(User user, UserType role) {
        if (user != null && role != null) {
            user.role = role;
            uni.addLog(this, "ROLE_ASSIGNED " + user.username + " " + role);
            System.out.println("Role assigned to " + user.username);
        }
    }

    public boolean updateUserEmail(User user, String email) {
        if (user == null) {
            return false;
        }
        try {
            user.updateEmail(email);
            uni.addLog(this, "USER_EMAIL_UPDATED userId=" + user.getId());
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
