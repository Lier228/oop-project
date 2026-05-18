package oopproject.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Lesson;
import oopproject.academic.Marks;
import oopproject.academic.StudyMaterial;
import oopproject.core.University;
import oopproject.enums.TeacherType;
import oopproject.enums.UserType;

public class Teacher extends Employee {
    private TeacherType title;
    private final List<Course> courses = new ArrayList<>();

    public Teacher() {
        this.role = UserType.TEACHER;
    }

    public Teacher(int id, String username, String password, String email,
                   double salary, LocalDate hireDate, TeacherType title) {
        super(id, username, password, email, salary, hireDate);
        this.role = UserType.TEACHER;
        this.title = title;
        if (title == TeacherType.PROFESSOR) {
            becomeResearcher(null);
        }
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
        if (course == null || !teachesCourse(course)) {
            return List.of();
        }
        return course.getEnrollments().stream()
                .map(Enrollment::getStudent)
                .toList();
    }

    public StudyMaterial createTask(String title, String description, String fileName, LocalDate deadline, Course course) {
        return StudyMaterial.builder()
                .title(title)
                .description(description)
                .fileName(fileName)
                .deadline(deadline)
                .task(true)
                .uploadedBy(this)
                .course(course)
                .build();
    }

    public boolean addLessonToCourse(Course course, Lesson lesson) {
        if (course == null || lesson == null || !teachesCourse(course)) {
            return false;
        }
        course.addLesson(lesson);
        University.getInstance().addLog(this, "LESSON_ADDED " + course.getCode());
        return true;
    }

    public boolean uploadMaterial(StudyMaterial material, Course course) {
        if (material == null || course == null || !teachesCourse(course)) {
            return false;
        }
        course.addMaterial(material);
        University.getInstance().addLog(this, "MATERIAL_ADDED " + course.getCode() + " " + material);
        return true;
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
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            course.addInstructor(this);
        }
    }

    public List<Course> getAssignedCourses() {
        return University.getInstance().getCoursesByTeacher(this);
    }

    public Course findAssignedCourseByCode(String courseCode) {
        if (courseCode == null) {
            return null;
        }
        return getAssignedCourses().stream()
                .filter(course -> courseCode.equalsIgnoreCase(course.getCode()))
                .findFirst()
                .orElse(null);
    }

    public TeacherType getTitle() {
        return title;
    }

    public void setTitle(TeacherType title) {
        this.title = title;
        if (title == TeacherType.PROFESSOR) {
            becomeResearcher(getResearchSchool());
        }
    }

    public List<Course> getCourses() {
        return List.copyOf(courses);
    }

    public boolean teachesCourse(Course course) {
        return course != null && course.getInstructors().contains(this);
    }
}
