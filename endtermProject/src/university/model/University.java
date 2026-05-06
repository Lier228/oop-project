package endtermProject.src.university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class University implements Serializable {
    private static final long serialVersionUID = 1L;

    private static University instance = new University();

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<News> newsFeed = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();

    private University() {
    }

    public static University getInstance() {
        return instance;
    }

    public static void setInstance(University university) {
        instance = university == null ? new University() : university;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public List<News> getNewsFeed() {
        return Collections.unmodifiableList(newsFeed);
    }

    public List<ResearchProject> getResearchProjects() {
        return Collections.unmodifiableList(researchProjects);
    }

    public void addUser(User user) {
        if (user != null && !users.contains(user)) {
            users.add(user);
        }
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                users.set(i, user);
                return;
            }
        }
    }

    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
        }
    }

    public void addNews(News news) {
        if (news != null) {
            newsFeed.add(news);
            newsFeed.sort(Comparator.naturalOrder());
        }
    }

    public void addResearchProject(ResearchProject project) {
        if (project != null && !researchProjects.contains(project)) {
            researchProjects.add(project);
        }
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }
        return students;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Teacher) {
                teachers.add((Teacher) user);
            }
        }
        return teachers;
    }

    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Employee) {
                employees.add((Employee) user);
            }
        }
        return employees;
    }

    public List<Researcher> getResearchers() {
        List<Researcher> researchers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Researcher researcher) {
                if (researcher.isResearcher()) {
                    researchers.add(researcher);
                }
            }
        }
        return researchers;
    }

    public void printAllResearchPapers(Comparator<ResearchPaper> comparator) {
        Set<ResearchPaper> papers = new LinkedHashSet<>();
        for (Researcher researcher : getResearchers()) {
            papers.addAll(researcher.getResearchPapers());
        }
        papers.stream()
                .sorted(comparator)
                .forEach(System.out::println);
    }

    public Researcher getTopCitedResearcher() {
        return getResearchers().stream()
                .max(Comparator.comparingInt(Researcher::getTotalCitations))
                .orElse(null);
    }

    public Researcher getTopCitedResearcher(int year) {
        return getResearchers().stream()
                .max(Comparator.comparingInt(researcher -> citationsInYear(researcher, year)))
                .orElse(null);
    }

    public Optional<User> findUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public Optional<Course> findCourseByCode(String code) {
        return courses.stream()
                .filter(course -> course.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    private int citationsInYear(Researcher researcher, int year) {
        return researcher.getResearchPapers().stream()
                .filter(paper -> paper.getPublicationDate().getYear() == year)
                .mapToInt(ResearchPaper::getCitations)
                .sum();
    }
}
