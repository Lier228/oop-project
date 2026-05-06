package endtermProject.src;

import endtermProject.src.university.enums.LessonType;
import endtermProject.src.university.enums.ManagerType;
import endtermProject.src.university.enums.TeacherTitle;
import endtermProject.src.university.exceptions.LowHIndexException;
import endtermProject.src.university.exceptions.NotResearcherException;
import endtermProject.src.university.model.Admin;
import endtermProject.src.university.model.Course;
import endtermProject.src.university.model.Lesson;
import endtermProject.src.university.model.Manager;
import endtermProject.src.university.model.Mark;
import endtermProject.src.university.model.News;
import endtermProject.src.university.model.Registration;
import endtermProject.src.university.model.ResearchPaper;
import endtermProject.src.university.model.ResearchProject;
import endtermProject.src.university.model.Researcher;
import endtermProject.src.university.model.Student;
import endtermProject.src.university.model.Teacher;
import endtermProject.src.university.model.University;
import endtermProject.src.university.service.AuthService;

import java.time.LocalDate;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        University university = University.getInstance();

        Admin admin = new Admin(
                "A-001", "admin", "admin123", "Aruzhan", "Tulegenova",
                "admin@uni.kz", 350000, "Administration"
        );
        Manager manager = new Manager(
                "M-001", "manager", "manager123", "Dana", "Saparova",
                "manager@uni.kz", 300000, "Registrar Office", ManagerType.OR
        );
        Teacher professor = new Teacher(
                "T-001", "professor", "prof123", "Asset", "Mukhamedov",
                "asset@uni.kz", 420000, "Computer Science", TeacherTitle.PROFESSOR, 12
        );
        Student student = new Student(
                "S-001", "student", "stud123", "Aigerim", "Nurgaliyeva",
                "student@uni.kz", 4, "Computer Science"
        );

        admin.addUser(admin);
        admin.addUser(manager);
        admin.addUser(professor);
        admin.addUser(student);

        Course oop = new Course("CS301", "Advanced OOP", 5, "Computer Science", 4);
        oop.addLesson(new Lesson("L1", "Inheritance and Polymorphism", LessonType.LECTURE, LocalDate.now()));
        university.addCourse(oop);
        manager.assignCourseToTeacher(oop, professor);

        try {
            Registration registration = student.registerForCourse(oop);
            manager.approveRegistration(registration);
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }

        professor.putMark(student, oop, new Mark(28, 27, 35));
        student.rateTeacher(professor, 5);

        ResearchPaper paper = new ResearchPaper(
                "Research on Adaptive Learning Systems",
                "IEEE Access",
                12,
                LocalDate.of(2025, 11, 15),
                "10.1109/example.2025.123456",
                47,
                Arrays.asList(professor.getFullName(), student.getFullName())
        );
        professor.addResearchPaper(paper);
        student.activateResearchProfile(1);
        student.addResearchPaper(new ResearchPaper(
                "Student Contribution to Educational Analytics",
                "ACM Journal",
                8,
                LocalDate.of(2026, 2, 2),
                "10.1145/example.2026.000111",
                6,
                Arrays.asList(student.getFullName())
        ));

        try {
            student.assignResearchSupervisor(professor);
        } catch (LowHIndexException e) {
            System.out.println("Supervisor error: " + e.getMessage());
        }

        ResearchProject researchProject = new ResearchProject("Digital University and Student Analytics");
        university.addResearchProject(researchProject);
        try {
            researchProject.addParticipant(professor);
            researchProject.addParticipant(student);
        } catch (NotResearcherException e) {
            System.out.println("Research project error: " + e.getMessage());
        }
        researchProject.publishPaper(paper);

        manager.manageNews(new News(
                "Research Week",
                "University research week starts next Monday.",
                LocalDate.now()
        ));

        AuthService authService = AuthService.getInstance();
        System.out.println("Authenticated user: " + authService.authenticate("student", "stud123"));
        System.out.println();
        System.out.println(student.viewTranscript());
        System.out.println();
        System.out.println(manager.createReport());
        System.out.println(manager.createMarkStatistics(oop));
        System.out.println();
        System.out.println("All research papers by citations:");
        university.printAllResearchPapers(ResearchPaper.byCitations());
        System.out.println();

        Researcher topResearcher = university.getTopCitedResearcher();
        if (topResearcher != null) {
            System.out.println("Top cited researcher: " + topResearcher.getResearcherName()
                    + ", citations=" + topResearcher.getTotalCitations());
        }
    }
}
