package oopproject.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Marks;
import oopproject.academic.StudyMaterial;
import oopproject.core.University;
import oopproject.enums.TeacherType;
import oopproject.enums.UserType;
import oopproject.research.ResearchPaper;
import oopproject.research.Researcher;

public class Teacher extends Employee implements Researcher {
    private TeacherType title;
    private boolean researcher;
    private String researchSchool = "UNSPECIFIED";
    private final List<Course> courses = new ArrayList<>();
    private final List<ResearchPaper> researchPapers = new ArrayList<>();

    public Teacher() {
        this.role = UserType.TEACHER;
    }

    public Teacher(int id, String username, String password, String email,
                   double salary, LocalDate hireDate, TeacherType title) {
        super(id, username, password, email, salary, hireDate);
        this.role = UserType.TEACHER;
        this.title = title;
        this.researcher = title == TeacherType.PROFESSOR;
    }

    public boolean putMark(Student student, Course course, Marks marks) {
        if (student == null || course == null || marks == null) {
            return false;
        }
        if (!course.getInstructors().contains(this)) {
            System.out.println("Teacher is not assigned to this course.");
            return false;
        }
        Enrollment enrollment = course.findEnrollment(student);
        if (enrollment == null) {
            return false;
        }
        if (enrollment.getMarks() == null) {
            enrollment.setMarks(marks);
        } else {
            enrollment.getMarks().mergeFrom(marks);
        }
        University.getInstance().addLog(this, "MARK_PUT " + course.getCode() + " studentId=" + student.getId());
        return true;
    }

    public List<Student> viewStudents(Course course) {
        if (course == null) {
            return List.of();
        }
        return course.getEnrollments().stream()
                .map(Enrollment::getStudent)
                .toList();
    }

    public void uploadMaterial(StudyMaterial material, Course course) {
        course.addMaterial(material);
    }

    public StudyMaterial createTask(String title, String description, String fileName, LocalDate deadline, Course course) {
        return new StudyMaterial(title, description, fileName, deadline, true, this, course);
    }

    public void gradeSubmission(Student student, StudyMaterial material, Marks marks, Course course) {
        Enrollment enrollment = course.findEnrollment(student);
        if (enrollment != null) {
            if (enrollment.getMarks() == null) {
                enrollment.setMarks(marks);
            } else {
                enrollment.getMarks().mergeFrom(marks);
            }
        }
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.addInstructor(this);
    }

    public void becomeResearcher(String school) {
        researcher = true;
        researchSchool = school;
    }

    @Override
    public boolean addResearchPaper(ResearchPaper paper) {
        if (paper == null || researchPapers.contains(paper)) {
            return false;
        }
        researcher = true;
        researchPapers.add(paper);
        paper.addAuthor(this);
        return true;
    }

    @Override
    public int calculateHIndex() {
        return Researcher.super.calculateHIndex();
    }

    @Override
    public boolean removeResearchPaper(ResearchPaper paper) {
        return researchPapers.remove(paper);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return Collections.unmodifiableList(researchPapers);
    }

    @Override
    public String getResearcherName() {
        return username;
    }

    @Override
    public boolean isResearcher() {
        return researcher;
    }

    @Override
    public String getResearchSchool() {
        return researchSchool;
    }

    public TeacherType getTitle() {
        return title;
    }
}
