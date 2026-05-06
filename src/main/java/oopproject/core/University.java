package oopproject.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oopproject.academic.Course;
import oopproject.research.ResearchProject;
import oopproject.users.User;

public class University {
    private static final University INSTANCE = new University();

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<ResearchProject> projects = new ArrayList<>();

    private University() {
    }

    public static University getInstance() {
        return INSTANCE;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addProject(ResearchProject project) {
        projects.add(project);
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public List<ResearchProject> getProjects() {
        return Collections.unmodifiableList(projects);
    }
}
