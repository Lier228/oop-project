package oopproject.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Mark;
import oopproject.academic.StudyMaterial;
import oopproject.enums.TeacherType;
import oopproject.research.ResearchPaper;
import oopproject.research.Researcher;

public class Teacher extends Employee implements Researcher {
    private TeacherType title;
    private final List<Course> courses = new ArrayList<>();
    private final List<ResearchPaper> researchPapers = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(int id, String username, String password, String email,
                   double salary, LocalDate hireDate, TeacherType title) {
        super(id, username, password, email, salary, hireDate);
        this.title = title;
    }

    public void putMark(Enrollment enrollment, Mark mark) {
        enrollment.setMark(mark);
    }

    public List<Student> viewStudents(Course course) {
        return course.getStudents();
    }

    public void uploadMaterial(StudyMaterial material, Course course) {
        course.addMaterial(material);
    }

    public StudyMaterial createTask(String title, String description, String fileName, LocalDate deadline, Course course) {
        return new StudyMaterial(title, description, fileName, deadline, true, this, course);
    }

    public void gradeSubmission(Student student, StudyMaterial material, Mark mark, Course course) {
        Enrollment enrollment = course.findEnrollment(student);
        if (enrollment != null) {
            enrollment.setMark(mark);
        }
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.addInstructor(this);
    }

    @Override
    public boolean addResearchPaper(ResearchPaper paper) {
        return researchPapers.add(paper);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return Collections.unmodifiableList(researchPapers);
    }

    public TeacherType getTitle() {
        return title;
    }
}
