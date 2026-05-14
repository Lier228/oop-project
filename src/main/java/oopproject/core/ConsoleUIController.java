package oopproject.core;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Scanner;
import oopproject.academic.Course;
import oopproject.academic.Mark;
import oopproject.enums.ManagerType;
import oopproject.enums.TeacherType;
import oopproject.exceptions.AlreadyRegisteredException;
import oopproject.exceptions.CreditLimitExceededException;
import oopproject.exceptions.LowHIndexException;
import oopproject.exceptions.NonResearcherException;
import oopproject.research.ResearchPaper;
import oopproject.research.ResearchPaperComparators;
import oopproject.research.ResearchProject;
import oopproject.research.Researcher;
import oopproject.users.Admin;
import oopproject.users.Manager;
import oopproject.users.Student;
import oopproject.users.Teacher;
import oopproject.users.User;

public class ConsoleUIController {
    private final Scanner scanner = new Scanner(System.in);
    private final University university = University.getInstance();
    private final AuthService authService = new AuthService(university);
    private User currentUser;

    public void start() {
        DataStore.loadState(university);
        seedUsers();
        seedDemoData();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1 -> loginMenu();
                case 2 -> printUniversitySummary();
                case 3 -> showCourses();
                case 4 -> saveState();
                case 0 -> {
                    saveState();
                    running = false;
                }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    public void loginMenu() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = authService.login(username, password);
        if (currentUser == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        System.out.println("Logged in as " + currentUser.getUsername());
        roleMenu();
    }

    public void roleMenu() {
        if (currentUser instanceof Admin) {
            adminMenu();
        } else if (currentUser instanceof Student) {
            studentMenu();
        } else if (currentUser instanceof Teacher) {
            teacherMenu();
        } else if (currentUser instanceof Manager) {
            managerMenu();
        } else {
            System.out.println("No menu is configured for this user type yet.");
        }
        authService.logout(currentUser);
        currentUser = null;
    }

    public void adminMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nAdmin menu");
            System.out.println("1. Show users");
            System.out.println("2. Show logs");
            System.out.println("3. Show courses");
            System.out.println("4. Save state");
            System.out.println("5. Block user");
            System.out.println("6. Unblock user");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> university.getUsers().forEach(System.out::println);
                case 2 -> university.getLogs().forEach(System.out::println);
                case 3 -> showCourses();
                case 4 -> saveState();
                case 5 -> setUserActive(false);
                case 6 -> setUserActive(true);
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    public void studentMenu() {
        Student student = (Student) currentUser;
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nStudent menu");
            System.out.println("1. Show open courses");
            System.out.println("2. Register for course");
            System.out.println("3. View transcript");
            System.out.println("4. View my courses");
            System.out.println("5. Assign research supervisor");
            System.out.println("6. View research supervisor");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> showOpenCourses();
                case 2 -> registerCurrentStudent(student);
                case 3 -> System.out.println(student.getTranscript());
                case 4 -> university.getCoursesByStudent(student).forEach(System.out::println);
                case 5 -> assignSupervisor(student);
                case 6 -> printSupervisor(student);
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    public void teacherMenu() {
        Teacher teacher = (Teacher) currentUser;
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nTeacher menu");
            System.out.println("1. View my courses");
            System.out.println("2. View students by course");
            System.out.println("3. Put mark");
            System.out.println("4. View research papers by citations");
            System.out.println("5. View research papers by date");
            System.out.println("6. View h-index");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> university.getCoursesByTeacher(teacher).forEach(System.out::println);
                case 2 -> showStudentsByCourse();
                case 3 -> putMark(teacher);
                case 4 -> teacher.printPapers(ResearchPaperComparators.BY_CITATIONS_DESC);
                case 5 -> teacher.printPapers(ResearchPaperComparators.BY_DATE_PUBLISHED_DESC);
                case 6 -> System.out.println("h-index: " + teacher.calculateHIndex());
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    public void managerMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nManager menu");
            System.out.println("1. Show courses");
            System.out.println("2. Open course registration");
            System.out.println("3. Close course registration");
            System.out.println("4. Assign teacher to course");
            System.out.println("5. Show researchers");
            System.out.println("6. Show top cited researcher");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> showCourses();
                case 2 -> setCourseOpen(true);
                case 3 -> setCourseOpen(false);
                case 4 -> assignTeacherToCourse();
                case 5 -> showResearchers();
                case 6 -> printTopCitedResearcher();
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n" + university.getName());
        System.out.println("1. Login");
        System.out.println("2. University summary");
        System.out.println("3. Show courses");
        System.out.println("4. Save state");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    private void printUniversitySummary() {
        System.out.println("Users: " + university.getUsers().size());
        System.out.println("Courses: " + university.getCourses().size());
        System.out.println("Researchers: " + university.getResearchers().size());
        System.out.println("Research projects: " + university.getProjects().size());
        university.findTopCitedResearcher()
                .ifPresent(researcher -> System.out.println("Top cited: " + researcher.getResearcherName()
                        + " (" + researcher.getTotalCitations() + " citations)"));
    }

    private void showCourses() {
        university.getCourses().forEach(System.out::println);
    }

    private void setUserActive(boolean active) {
        if (!(currentUser instanceof Admin admin)) {
            return;
        }
        university.getUsers().forEach(System.out::println);
        System.out.print("User id: ");
        int userId = readInt();
        User user = university.findUserById(userId).orElse(null);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        if (active) {
            admin.unblockUser(user);
            university.addLog(currentUser, "USER_UNBLOCKED " + user.getUsername());
        } else {
            admin.blockUser(user);
            university.addLog(currentUser, "USER_BLOCKED " + user.getUsername());
        }
        System.out.println("User updated.");
    }

    private void showOpenCourses() {
        university.getCourses().stream()
                .filter(Course::isOpen)
                .forEach(System.out::println);
    }

    private void registerCurrentStudent(Student student) {
        showOpenCourses();
        System.out.print("Course code: ");
        String courseCode = scanner.nextLine();
        try {
            if (university.registerStudentToCourse(student.getId(), courseCode)) {
                System.out.println("Registered successfully.");
            } else {
                System.out.println("Course not found.");
            }
        } catch (CreditLimitExceededException | AlreadyRegisteredException | IllegalStateException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void assignSupervisor(Student student) {
        showResearchers();
        System.out.print("Supervisor username: ");
        String username = scanner.nextLine();
        User user = university.findUserByUsername(username).orElse(null);
        if (!(user instanceof Researcher researcher)) {
            System.out.println("This user is not a researcher.");
            return;
        }
        try {
            student.assignResearchSupervisor(researcher);
            university.addLog(student, "SUPERVISOR_ASSIGNED " + researcher.getResearcherName());
            System.out.println("Supervisor assigned.");
        } catch (LowHIndexException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void printSupervisor(Student student) {
        Researcher supervisor = student.getResearchSupervisor();
        if (supervisor == null) {
            System.out.println("Supervisor is not assigned.");
            return;
        }
        System.out.println(supervisor.getResearcherName()
                + ", h-index=" + supervisor.calculateHIndex()
                + ", citations=" + supervisor.getTotalCitations());
    }

    private void showStudentsByCourse() {
        Course course = askCourse();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        course.getStudents().forEach(System.out::println);
    }

    private void putMark(Teacher teacher) {
        Course course = askCourse();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        if (!course.getInstructors().contains(teacher)) {
            System.out.println("Teacher is not assigned to this course.");
            return;
        }
        course.getStudents().forEach(System.out::println);
        System.out.print("Student id: ");
        int studentId = readInt();
        Mark mark = readMark();
        if (university.putMark(teacher.getId(), studentId, course.getCode(), mark)) {
            System.out.println("Mark saved.");
        } else {
            System.out.println("Could not put mark.");
        }
    }

    private Mark readMark() {
        System.out.print("Attestation 1: ");
        double att1 = readDouble();
        System.out.print("Attestation 2: ");
        double att2 = readDouble();
        System.out.print("Final exam: ");
        double finalExam = readDouble();
        return new Mark(att1, att2, finalExam);
    }

    private void setCourseOpen(boolean open) {
        Course course = askCourse();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        boolean changed = open
                ? university.openCourseForRegistration(course.getCode())
                : university.closeCourseForRegistration(course.getCode());
        System.out.println(changed ? "Course updated." : "Could not update course.");
    }

    private void assignTeacherToCourse() {
        university.getTeachers().forEach(System.out::println);
        System.out.print("Teacher id: ");
        int teacherId = readInt();
        Course course = askCourse();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        if (university.assignTeacherToCourse(teacherId, course.getCode())) {
            System.out.println("Teacher assigned.");
        } else {
            System.out.println("Could not assign teacher.");
        }
    }

    private Course askCourse() {
        showCourses();
        System.out.print("Course code: ");
        String courseCode = scanner.nextLine();
        return university.findCourseByCode(courseCode).orElse(null);
    }

    private void showResearchers() {
        university.getResearchers().stream()
                .sorted(Comparator.comparing(Researcher::getResearcherName))
                .forEach(researcher -> System.out.println(researcher.getResearcherName()
                        + ", school=" + researcher.getResearchSchool()
                        + ", h-index=" + researcher.calculateHIndex()
                        + ", citations=" + researcher.getTotalCitations()));
    }

    private void printTopCitedResearcher() {
        university.findTopCitedResearcher()
                .ifPresentOrElse(
                        researcher -> System.out.println(researcher.getResearcherName()
                                + " with " + researcher.getTotalCitations() + " citations"),
                        () -> System.out.println("No researchers found.")
                );
    }

    private void saveState() {
        if (DataStore.saveState(university)) {
            System.out.println("Saved to " + DataStore.getStateFile());
            System.out.println("JSON snapshot: " + DataStore.getJsonSnapshotFile());
        } else {
            System.out.println("Could not save state.");
        }
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exception) {
            return 0.0;
        }
    }

    private void seedUsers() {
        if (!university.getUsers().isEmpty()) {
            return;
        }
        university.addUser(new Admin(1, "admin", "admin", "admin@university.kz"));
        university.addUser(new Student(2, "student", "student", "student@university.kz", 3.4, 4, 0, 0));

        Teacher professor = new Teacher(3, "professor", "professor", "professor@university.kz",
                500000, LocalDate.now(), TeacherType.PROFESSOR);
        professor.becomeResearcher("SITE");
        university.addUser(professor);

        university.addUser(new Manager(4, "manager", "manager", "manager@university.kz",
                400000, LocalDate.now(), ManagerType.OR_MANAGER));
    }

    private void seedDemoData() {
        if (!university.getCourses().isEmpty()) {
            return;
        }

        Course oop = new Course("Object Oriented Programming", "OOP101", 5);
        Course researchMethods = new Course("Research Methods", "RES201", 4);
        oop.setOpen(true);
        researchMethods.setOpen(true);
        university.addCourse(oop);
        university.addCourse(researchMethods);
        university.assignTeacherToCourse(3, "OOP101");
        university.assignTeacherToCourse(3, "RES201");

        Teacher professor = (Teacher) university.findUserByUsername("professor").orElse(null);
        if (professor == null) {
            return;
        }
        professor.addResearchPaper(new ResearchPaper("AI in Education", "IEEE Access", 12, 20,
                LocalDate.of(2025, 3, 12), "10.1000/ai-edu"));
        professor.addResearchPaper(new ResearchPaper("Research Systems", "Springer", 8, 8,
                LocalDate.of(2024, 9, 20), "10.1000/research-systems"));
        professor.addResearchPaper(new ResearchPaper("Learning Analytics", "ACM", 10, 4,
                LocalDate.of(2026, 1, 15), "10.1000/learning-analytics"));

        ResearchProject project = new ResearchProject("Digital University Research");
        try {
            project.addParticipant(professor);
        } catch (NonResearcherException exception) {
            university.addLog(professor, "PROJECT_JOIN_FAILED " + exception.getMessage());
        }
        for (ResearchPaper paper : professor.getResearchPapers()) {
            project.addPaper(paper);
        }
        university.addProject(project);
    }
}
