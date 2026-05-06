package endtermProject.src.university.model;

import endtermProject.src.university.enums.ManagerType;
import endtermProject.src.university.service.LogManager;
import endtermProject.src.university.service.ReportGenerator;

public class Manager extends Employee {
    private static final long serialVersionUID = 1L;

    private final ManagerType managerType;

    public Manager(
            String id,
            String username,
            String password,
            String name,
            String surname,
            String email,
            double salary,
            String department,
            ManagerType managerType
    ) {
        super(id, username, password, name, surname, email, salary, department);
        this.managerType = managerType;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public void approveRegistration(Registration registration) {
        registration.approve();
        registration.getCourse().enrollStudent(registration.getStudent());
        LogManager.getInstance().logAction(this, "approved registration for " + registration.getStudent().getFullName());
    }

    public void rejectRegistration(Registration registration) {
        registration.reject();
        LogManager.getInstance().logAction(this, "rejected registration for " + registration.getStudent().getFullName());
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        teacher.manageCourse(course);
        LogManager.getInstance().logAction(this, "assigned " + course.getCode() + " to " + teacher.getFullName());
    }

    public String createReport() {
        return ReportGenerator.getInstance().generateStudentPerformanceReport(University.getInstance().getStudents());
    }

    public String createMarkStatistics(Course course) {
        return ReportGenerator.getInstance().generateMarkStatistics(course);
    }

    public void manageNews(News news) {
        University.getInstance().addNews(news);
        LogManager.getInstance().logAction(this, "published news: " + news.getTitle());
    }

    public void activateResearcher(Researcher researcher, int hIndex) {
        researcher.activateResearchProfile(hIndex);
        LogManager.getInstance().logAction(this, "activated researcher: " + researcher.getResearcherName());
    }
}
