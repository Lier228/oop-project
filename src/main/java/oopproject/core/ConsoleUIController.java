package oopproject.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Lesson;
import oopproject.academic.Marks;
import oopproject.academic.Request;
import oopproject.academic.Report;
import oopproject.enums.ManagerType;
import oopproject.enums.DaysOfWeek;
import oopproject.enums.LessonType;
import oopproject.enums.RequestType;
import oopproject.enums.TeacherType;
import oopproject.exceptions.AlreadyRegisteredException;
import oopproject.exceptions.CreditLimitExceededException;
import oopproject.exceptions.LowHIndexException;
import oopproject.exceptions.NonResearcherException;
import oopproject.research.ResearchPaper;
import oopproject.research.ResearchProject;
import oopproject.research.Researcher;
import oopproject.users.Admin;
import oopproject.users.Employee;
import oopproject.users.Manager;
import oopproject.users.Student;
import oopproject.users.Teacher;
import oopproject.users.User;
import oopproject.academic.StudyMaterial;
import oopproject.academic.TimeSlot;
import oopproject.enums.ResearchSortType;

public class ConsoleUIController {
    private final Scanner scanner = new Scanner(System.in);
    private final University university = University.getInstance();
    private final AuthService authService = new AuthService(university);
    private final Admin admin = Admin.getInstance();
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
        Admin adminUser = (Admin) currentUser;
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nAdmin menu");
            System.out.println("1. Show users");
            System.out.println("2. Manage users");
            System.out.println("3. Show logs");
            System.out.println("4. Show courses");
            System.out.println("5. Save state");
            System.out.println("6. Block user");
            System.out.println("7. Unblock user");
            System.out.println("8. Communication");
            System.out.println("9. Research");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> adminUser.showUsers().forEach(System.out::println);
                case 2 -> manageUsersMenu();
                case 3 -> adminUser.showLogs().forEach(System.out::println);
                case 4 -> showCourses();
                case 5 -> saveState();
                case 6 -> setUserActive(false);
                case 7 -> setUserActive(true);
                case 8 -> communicationMenu();
                case 9 -> researchMenu();
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
            System.out.println("5. View teachers of my course");
            System.out.println("6. View marks");
            System.out.println("7. Rate teacher");
            System.out.println("8. Research");
            System.out.println("9. Assign research supervisor");
            System.out.println("10. View research supervisor");
            System.out.println("11. Communication");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> student.getOpenCoursesForRegistration().forEach(System.out::println);
                case 2 -> registerCurrentStudent(student);
                case 3 -> System.out.println(student.getTranscript());
                case 4 -> student.getRegisteredCourses().forEach(System.out::println);
                case 5 -> showCourseTeachersForStudent(student);
                case 6 -> viewStudentMarks(student);
                case 7 -> rateTeacher(student);
                case 8 -> researchMenu();
                case 9 -> assignSupervisor(student);
                case 10 -> printSupervisor(student);
                case 11 -> communicationMenu();
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
            System.out.println("4. Research");
            System.out.println("5. Communication");
            System.out.println("6. Manage course");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> teacher.getAssignedCourses().forEach(System.out::println);
                case 2 -> showStudentsByCourse();
                case 3 -> putMark(teacher);
                case 4 -> researchMenu();
                case 5 -> communicationMenu();
                case 6 -> manageCourseMenu();
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
            System.out.println("2. Create course");
            System.out.println("3. Open course registration");
            System.out.println("4. Close course registration");
            System.out.println("5. Assign teacher to course");
            System.out.println("6. Generate academic report");
            System.out.println("7. Manage news");
            System.out.println("8. View students and teachers");
            System.out.println("9. Show researchers");
            System.out.println("10. Assign research supervisor");
            System.out.println("11. Research");
            System.out.println("12. Review requests");
            System.out.println("13. Communication");
            System.out.println("14. Manage course");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> showCourses();
                case 2 -> createCourseForRegistration();
                case 3 -> setCourseOpen(true);
                case 4 -> setCourseOpen(false);
                case 5 -> assignTeacherToCourse();
                case 6 -> generateAcademicPerformanceReport();
                case 7 -> manageNewsMenu();
                case 8 -> viewPeopleMenu();
                case 9 -> showResearchers();
                case 10 -> assignSupervisorAsManager();
                case 11 -> researchMenu();
                case 12 -> requestReviewMenu();
                case 13 -> communicationMenu();
                case 14 -> manageCourseMenu();
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
        admin.showUsers().forEach(System.out::println);
        System.out.print("User id: ");
        int userId = readInt();
        User user = university.findUserById(userId).orElse(null);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        System.out.println(admin.setUserActive(user, active) ? "User updated." : "Could not update user.");
    }

    private void manageUsersMenu() {
        if (!(currentUser instanceof Admin adminUser)) {
            return;
        }
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nManage users");
            System.out.println("1. Add user");
            System.out.println("2. Remove user");
            System.out.println("3. Update user email");
            System.out.println("4. Show users");
            System.out.println("0. Back");
            int choice = readInt();
            switch (choice) {
                case 1 -> addUserFromConsole();
                case 2 -> removeUserFromConsole();
                case 3 -> updateUserEmailFromConsole();
                case 4 -> adminUser.showUsers().forEach(System.out::println);
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void addUserFromConsole() {
        User user = createUserFromInput();
        if (user == null) {
            System.out.println("User creation cancelled.");
            return;
        }
        if (admin.addUser(user)) {
            maybeActivateResearcher(user);
            System.out.println("User added.");
        } else {
            System.out.println("Could not add user.");
        }
    }

    private void removeUserFromConsole() {
        admin.showUsers().forEach(System.out::println);
        System.out.print("User id: ");
        int userId = readInt();
        System.out.println(admin.removeUserById(userId) ? "User removed." : "Could not remove user.");
    }

    private void updateUserEmailFromConsole() {
        admin.showUsers().forEach(System.out::println);
        System.out.print("User id: ");
        int userId = readInt();
        User user = university.findUserById(userId).orElse(null);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        System.out.print("New email: ");
        String email = scanner.nextLine().trim();
        if (admin.updateUserEmail(user, email)) {
            System.out.println("Email updated.");
        } else {
            System.out.println("Could not update email.");
        }
    }

    private User createUserFromInput() {
        System.out.println("Role:");
        System.out.println("1. Admin");
        System.out.println("2. Student");
        System.out.println("3. Teacher");
        System.out.println("4. Manager");
        System.out.println("5. Employee");
        int roleChoice = readInt();

        System.out.print("Id: ");
        int id = readInt();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        return switch (roleChoice) {
            case 1 -> new Admin(id, username, password, email);
            case 2 -> createStudentFromInput(id, username, password, email);
            case 3 -> createTeacherFromInput(id, username, password, email);
            case 4 -> createManagerFromInput(id, username, password, email);
            case 5 -> createEmployeeFromInput(id, username, password, email);
            default -> null;
        };
    }

    private Student createStudentFromInput(int id, String username, String password, String email) {
        System.out.print("GPA: ");
        double gpa = readDouble();
        System.out.print("Year: ");
        int year = readInt();
        System.out.print("Registered credits: ");
        int credits = readInt();
        System.out.print("Failed courses: ");
        int failedCourses = readInt();
        return new Student(id, username, password, email, gpa, year, credits, failedCourses);
    }

    private Teacher createTeacherFromInput(int id, String username, String password, String email) {
        System.out.print("Salary: ");
        double salary = readDouble();
        TeacherType title = readTeacherTypeOption();
        Teacher teacher = new Teacher(id, username, password, email, salary, LocalDate.now(), title);
        System.out.print("Department: ");
        teacher.setDepartment(scanner.nextLine().trim());
        if (title == TeacherType.PROFESSOR) {
            teacher.becomeResearcher(teacher.getDepartment());
        }
        return teacher;
    }

    private Manager createManagerFromInput(int id, String username, String password, String email) {
        System.out.print("Salary: ");
        double salary = readDouble();
        ManagerType managerType = readManagerTypeOption();
        Manager manager = new Manager(id, username, password, email, salary, LocalDate.now(), managerType);
        System.out.print("Department: ");
        manager.setDepartment(scanner.nextLine().trim());
        return manager;
    }

    private Employee createEmployeeFromInput(int id, String username, String password, String email) {
        System.out.print("Salary: ");
        double salary = readDouble();
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        return new Employee(id, username, password, email, salary, LocalDate.now(), department);
    }

    private void maybeActivateResearcher(User user) {
        if (!(user instanceof Student || user instanceof Employee)) {
            return;
        }
        if (user instanceof Researcher researcher && researcher.isResearcher()) {
            return;
        }
        System.out.print("Activate researcher profile now? (y/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
            return;
        }
        System.out.print("Research school: ");
        String school = scanner.nextLine().trim();
        university.activateResearcher(user, school);
    }

    private void registerCurrentStudent(Student student) {
        student.getOpenCoursesForRegistration().forEach(System.out::println);
        System.out.print("Course code: ");
        String courseCode = scanner.nextLine();
        try {
            if (student.registerForCourse(courseCode)) {
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
        if (!(user instanceof Researcher researcher) || !researcher.isResearcher()) {
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

    private void assignSupervisorAsManager() {
        if (!(currentUser instanceof Manager manager)) {
            return;
        }
        List<Student> researcherStudents = manager.getResearcherStudents();
        if (researcherStudents.isEmpty()) {
            System.out.println("No researcher students found.");
            return;
        }

        researcherStudents.forEach(student -> System.out.println(student
                + ", researcher=" + student.isResearcher()
                + ", supervisor=" + (student.getResearchSupervisor() == null
                ? "none"
                : student.getResearchSupervisor().getResearcherName())));
        System.out.print("Student id: ");
        int studentId = readInt();
        Student student = researcherStudents.stream()
                .filter(candidate -> candidate.getId() == studentId)
                .findFirst()
                .orElse(null);
        if (student == null) {
            System.out.println("Researcher student not found.");
            return;
        }

        showResearchers();
        System.out.print("Supervisor username: ");
        String username = scanner.nextLine();
        User user = university.findUserByUsername(username).orElse(null);
        if (!(user instanceof Researcher researcher) || !researcher.isResearcher()) {
            System.out.println("This user is not a researcher.");
            return;
        }

        try {
            if (manager.assignResearchSupervisor(student, researcher)) {
                System.out.println("Supervisor assigned.");
            } else {
                System.out.println("Could not assign supervisor.");
            }
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
        if (!(currentUser instanceof Teacher teacher)) {
            return;
        }
        Course course = askTeacherCourse(teacher);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        List<Student> students = teacher.viewStudents(course);
        if (students.isEmpty()) {
            System.out.println("No students found for this course.");
            return;
        }
        students.forEach(System.out::println);
    }

    private void putMark(Teacher teacher) {
        Course course = askTeacherCourse(teacher);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        List<Student> students = teacher.viewStudents(course);
        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
            return;
        }
        students.forEach(System.out::println);
        System.out.print("Student id: ");
        int studentId = readInt();
        User user = university.findUserById(studentId).orElse(null);
        if (!(user instanceof Student student)) {
            System.out.println("Student not found.");
            return;
        }
        Marks marks = readMarks();
        if (teacher.putMark(student, course, marks)) {
            System.out.println("Mark saved.");
        } else {
            System.out.println("Could not put mark.");
        }
    }

    private Marks readMarks() {
        System.out.println("Choose mark destination:");
        System.out.println("1. Add attestation 1 mark");
        System.out.println("2. Add attestation 2 mark");
        System.out.println("3. Set final exam mark");
        int choice = readInt();

        Marks marks = new Marks();
        switch (choice) {
            case 1 -> {
                System.out.print("Attestation 1 mark: ");
                marks.addAtt1Mark(readDouble());
            }
            case 2 -> {
                System.out.print("Attestation 2 mark: ");
                marks.addAtt2Mark(readDouble());
            }
            case 3 -> {
                System.out.print("Final exam mark: ");
                marks.addFinalExamMark(readDouble());
            }
            default -> {
                System.out.println("Unknown option.");
                return null;
            }
        }
        return marks;
    }

    private void showCourseTeachersForStudent(Student student) {
        Course course = askStudentCourse(student);
        if (course == null) {
            System.out.println("Course not found in your registrations.");
            return;
        }
        List<Teacher> teachers = student.getCourseTeachers(course);
        if (teachers.isEmpty()) {
            System.out.println("No instructors assigned.");
            return;
        }
        teachers.forEach(System.out::println);
    }

    private void viewStudentMarks(Student student) {
        List<String> lines = student.getMarksSummaryLines();
        if (lines.isEmpty()) {
            System.out.println("No enrolled courses.");
            return;
        }
        lines.forEach(System.out::println);
    }

    private void rateTeacher(Student student) {
        Course course = askStudentCourse(student);
        if (course == null) {
            System.out.println("Course not found in your registrations.");
            return;
        }
        if (course.getInstructors().isEmpty()) {
            System.out.println("No teachers assigned to this course.");
            return;
        }

        course.getInstructors().forEach(System.out::println);
        System.out.print("Teacher id: ");
        int teacherId = readInt();
        User user = university.findUserById(teacherId).orElse(null);
        if (!(user instanceof Teacher teacher) || !course.getInstructors().contains(teacher)) {
            System.out.println("Teacher not found for this course.");
            return;
        }

        System.out.print("Rating (1-5): ");
        int rating = readInt();
        try {
            if (!student.rateTeacherForCourse(course, teacher, rating)) {
                System.out.println("Teacher not found for this course.");
                return;
            }
            university.addLog(student, "TEACHER_RATED teacherId=" + teacher.getId() + " rating=" + rating);
            System.out.println("Teacher rated.");
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void researchMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nResearch menu");
            if (canCurrentUserBecomeResearcher()) {
                System.out.println("1. Become researcher / update research school");
            }
            System.out.println("2. View my research papers");
            System.out.println("3. Add research paper");
            System.out.println("4. View my research projects");
            System.out.println("5. Create research project");
            System.out.println("6. Join research project");
            System.out.println("7. View all university research papers");
            System.out.println("8. Show top cited researcher");
            System.out.println("9. Show top cited researcher of school");
            System.out.println("10. Show top cited researcher of year");
            System.out.println("11. Show researchers by school");
            System.out.println("12. Show research projects");
            System.out.println("0. Back");
            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    if (canCurrentUserBecomeResearcher()) {
                        activateCurrentUserAsResearcher();
                    } else {
                        System.out.println("Unknown option.");
                    }
                }
                case 2 -> viewCurrentUserResearchPapers();
                case 3 -> addResearchPaperForCurrentUser();
                case 4 -> viewCurrentUserResearchProjects();
                case 5 -> createResearchProject();
                case 6 -> joinResearchProject();
                case 7 -> viewAllResearchPapers();
                case 8 -> printTopCitedResearcher();
                case 9 -> printTopCitedResearcherOfSchool();
                case 10 -> printTopCitedResearcherOfYear();
                case 11 -> showResearchersBySchool();
                case 12 -> showResearchProjects();
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void activateCurrentUserAsResearcher() {
        if (!canCurrentUserBecomeResearcher()) {
            System.out.println("This user type cannot become a researcher.");
            return;
        }
        System.out.print("Research school: ");
        String school = scanner.nextLine().trim();
        if (university.activateResearcher(currentUser, school)) {
            Researcher researcher = currentUserResearcher();
            System.out.println("Researcher profile is active for " + researcher.getResearcherName()
                    + " (" + researcher.getResearchSchool() + ").");
        } else {
            System.out.println("Could not activate researcher profile.");
        }
    }

    private void viewCurrentUserResearchPapers() {
        Researcher researcher = currentUserResearcher();
        if (researcher == null || !researcher.isResearcher()) {
            System.out.println("Current user is not a researcher.");
            return;
        }
        List<ResearchPaper> papers = researcher.getPapersSorted(readResearchSortType());
        printResearchPapers(papers);
    }

    private void addResearchPaperForCurrentUser() {
        Researcher researcher = currentUserResearcher();
        if (researcher == null) {
            System.out.println("Current user cannot have a researcher profile.");
            return;
        }
        if (!researcher.isResearcher()) {
            System.out.println("Activate the researcher profile first.");
            return;
        }

        ResearchPaper paper = readResearchPaper();
        if (paper == null) {
            System.out.println("Research paper creation cancelled.");
            return;
        }
        if (researcher.addResearchPaper(paper)) {
            university.addResearcher(researcher);
            university.addLog(currentUser, "RESEARCH_PAPER_ADDED " + paper.getTitle());
            System.out.println("Research paper added.");
        } else {
            System.out.println("Could not add research paper.");
        }
    }

    private void viewCurrentUserResearchProjects() {
        Researcher researcher = currentUserResearcher();
        if (researcher == null || !researcher.isResearcher()) {
            System.out.println("Current user is not a researcher.");
            return;
        }
        if (researcher.getResearchProjects().isEmpty()) {
            System.out.println("No research projects.");
            return;
        }
        researcher.getResearchProjects().forEach(System.out::println);
    }

    private void createResearchProject() {
        System.out.print("Project topic: ");
        String topic = scanner.nextLine().trim();
        if (topic.isBlank()) {
            System.out.println("Topic cannot be empty.");
            return;
        }

        ResearchProject project = new ResearchProject(topic);
        if (!university.addProject(project)) {
            System.out.println("Could not create project.");
            return;
        }

        Researcher researcher = currentUserResearcher();
        if (researcher != null && researcher.isResearcher()) {
            try {
                project.addParticipant(researcher);
            } catch (NonResearcherException exception) {
                university.addLog(currentUser, "PROJECT_JOIN_FAILED " + exception.getMessage());
            }
        }
        university.addLog(currentUser, "RESEARCH_PROJECT_CREATED " + topic);
        System.out.println("Research project created.");
    }

    private void joinResearchProject() {
        Researcher researcher = currentUserResearcher();
        if (researcher == null || !researcher.isResearcher()) {
            System.out.println("Only active researchers can join projects.");
            return;
        }
        ResearchProject project = askResearchProject();
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }
        try {
            boolean joined;
            if (currentUser instanceof Student student) {
                joined = student.joinResearchProject(project);
            } else if (currentUser instanceof Employee employee) {
                joined = employee.joinResearchProject(project);
            } else {
                joined = project.addParticipant(researcher);
            }
            if (joined) {
                university.addLog(currentUser, "RESEARCH_PROJECT_JOINED " + project.getTopic());
                System.out.println("Joined project.");
            } else {
                System.out.println("Already participating in this project.");
            }
        } catch (NonResearcherException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void viewAllResearchPapers() {
        printResearchPapers(university.getResearchPapersSorted(readResearchSortType()));
    }

    private void printTopCitedResearcherOfSchool() {
        System.out.print("School: ");
        String school = scanner.nextLine().trim();
        university.findTopCitedResearcherOfSchool(school)
                .ifPresentOrElse(
                        this::printResearcherSummary,
                        () -> System.out.println("No researchers found for that school.")
                );
    }

    private void printTopCitedResearcherOfYear() {
        System.out.print("Year: ");
        int year = readInt();
        if (year <= 0) {
            System.out.println("Invalid year.");
            return;
        }
        university.findTopCitedResearcherOfYear(year)
                .ifPresentOrElse(
                        researcher -> System.out.println(researcher.getResearcherName()
                                + " with " + researcher.getCitationsInYear(year)
                                + " citations in " + year),
                        () -> System.out.println("No researchers found for that year.")
                );
    }

    private void showResearchersBySchool() {
        System.out.print("School: ");
        String school = scanner.nextLine().trim();
        List<Researcher> researchers = university.getResearchersBySchool(school);
        if (researchers.isEmpty()) {
            System.out.println("No researchers found.");
            return;
        }
        researchers.stream()
                .sorted(Comparator.comparing(Researcher::getResearcherName))
                .forEach(this::printResearcherSummary);
    }

    private void showResearchProjects() {
        if (university.getProjects().isEmpty()) {
            System.out.println("No research projects.");
            return;
        }
        university.getProjects().forEach(project -> System.out.println(project.getTopic()
                + " | participants=" + project.getParticipants().size()
                + ", papers=" + project.getPapers().size()));
    }

    private ResearchPaper readResearchPaper() {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Journal: ");
        String journal = scanner.nextLine().trim();
        System.out.print("Publisher: ");
        String publisher = scanner.nextLine().trim();
        System.out.print("Pages count: ");
        int pages = readInt();
        System.out.print("Citations: ");
        int citations = readInt();
        LocalDate publishDate = readDate("Publication date");
        if (title.isBlank() || journal.isBlank() || pages <= 0 || citations < 0 || publishDate == null) {
            return null;
        }
        System.out.print("DOI: ");
        String doi = scanner.nextLine().trim();
        System.out.print("Keywords: ");
        String keywords = scanner.nextLine().trim();
        try {
            return ResearchPaper.builder()
                    .title(title)
                    .journal(journal)
                    .publisher(publisher)
                    .pages(pages)
                    .citations(citations)
                    .publishDate(publishDate)
                    .doi(doi)
                    .keywords(keywords)
                    .build();
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private void printResearchPapers(List<ResearchPaper> papers) {
        if (papers == null || papers.isEmpty()) {
            System.out.println("No research papers found.");
            return;
        }
        papers.forEach(paper -> System.out.println(paper.toDisplayString()));
    }

    private ResearchSortType readResearchSortType() {
        System.out.println("Sort papers by:");
        System.out.println("1. Publication date");
        System.out.println("2. Citations");
        System.out.println("3. Article length");
        int choice = readInt();
        return switch (choice) {
            case 1 -> ResearchSortType.DATE;
            case 3 -> ResearchSortType.LENGTH;
            default -> ResearchSortType.CITATIONS;
        };
    }

    private ResearchProject askResearchProject() {
        showResearchProjects();
        System.out.print("Project topic: ");
        String topic = scanner.nextLine().trim();
        return university.findProjectByTopic(topic).orElse(null);
    }

    private Researcher currentUserResearcher() {
        return currentUser instanceof Researcher researcher ? researcher : null;
    }

    private boolean canCurrentUserBecomeResearcher() {
        return currentUser instanceof Student || currentUser instanceof Employee;
    }

    private void printResearcherSummary(Researcher researcher) {
        System.out.println(researcher.getResearcherName()
                + ", school=" + researcher.getResearchSchool()
                + ", h-index=" + researcher.calculateHIndex()
                + ", citations=" + researcher.getTotalCitations()
                + ", projects=" + researcher.getResearchProjects().size());
    }

    private void communicationMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nCommunication menu");
            System.out.println("1. View news");
            System.out.println("2. View direct messages");
            System.out.println("3. Send direct message");
            System.out.println("4. View my requests");
            if (canCurrentUserSubmitComplaint()) {
                System.out.println("5. Submit complaint");
            }
            System.out.println("0. Back");
            int choice = readInt();
            switch (choice) {
                case 1 -> viewInboxNews();
                case 2 -> viewDirectMessages();
                case 3 -> sendDirectMessage();
                case 4 -> viewMyRequests();
                case 5 -> {
                    if (canCurrentUserSubmitComplaint()) {
                        submitComplaint();
                    } else {
                        System.out.println("Unknown option.");
                    }
                }
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void viewInboxNews() {
        if (currentUser.getInboxNews().isEmpty()) {
            System.out.println("No news.");
            return;
        }
        currentUser.getInboxNews().forEach(System.out::println);
    }

    private void viewDirectMessages() {
        if (currentUser.getInboxMessages().isEmpty()) {
            System.out.println("No direct messages.");
            return;
        }
        currentUser.getInboxMessages().forEach(System.out::println);
    }

    private void sendDirectMessage() {
        university.getUsers().stream()
                .filter(user -> !user.equals(currentUser))
                .forEach(System.out::println);
        System.out.print("Recipient username: ");
        String username = scanner.nextLine();
        User recipient = university.findUserByUsername(username).orElse(null);
        if (recipient == null) {
            System.out.println("Recipient not found.");
            return;
        }
        System.out.print("Message: ");
        String content = scanner.nextLine();
        if (currentUser.sendMessage(recipient, content, university)) {
            System.out.println("Message sent.");
        } else {
            System.out.println("Could not send message.");
        }
    }

    private void submitComplaint() {
        if (!canCurrentUserSubmitComplaint()) {
            System.out.println("Only students and teachers can submit complaints.");
            return;
        }
        System.out.print("Complaint description: ");
        String description = scanner.nextLine();
        if (university.submitRequest(currentUser, RequestType.COMPLAIN, description)) {
            System.out.println("Complaint submitted.");
        } else {
            System.out.println("Could not submit complaint.");
        }
    }

    private void viewMyRequests() {
        printRequests(currentUser.viewCreatedRequests());
    }

    private void requestReviewMenu() {
        if (!(currentUser instanceof Manager manager)) {
            return;
        }
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nRequests menu");
            System.out.println("1. View all requests");
            System.out.println("2. View pending requests");
            System.out.println("3. View finished requests");
            System.out.println("4. Approve request");
            System.out.println("5. Reject request");
            System.out.println("6. Comment request");
            System.out.println("7. Remove request");
            System.out.println("0. Back");
            int choice = readInt();
            switch (choice) {
                case 1 -> printRequests(manager.showRequests());
                case 2 -> printRequests(manager.showPendingRequests());
                case 3 -> printRequests(manager.showFinishedRequests());
                case 4 -> approveRequest(manager);
                case 5 -> rejectRequest(manager);
                case 6 -> commentRequest(manager);
                case 7 -> removeRequest(manager);
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void printRequests(List<Request> requests) {
        if (requests.isEmpty()) {
            System.out.println("No requests found.");
            return;
        }
        requests.forEach(System.out::println);
    }

    private void approveRequest(Manager manager) {
        printRequests(manager.showRequests());
        System.out.print("Request id: ");
        long requestId = readLong();
        if (manager.approveRequest(requestId)) {
            System.out.println("Request approved.");
        } else {
            System.out.println("Could not approve request.");
        }
    }

    private void rejectRequest(Manager manager) {
        printRequests(manager.showRequests());
        System.out.print("Request id: ");
        long requestId = readLong();
        if (manager.rejectRequest(requestId)) {
            System.out.println("Request rejected.");
        } else {
            System.out.println("Could not reject request.");
        }
    }

    private void commentRequest(Manager manager) {
        printRequests(manager.showRequests());
        System.out.print("Request id: ");
        long requestId = readLong();
        System.out.print("Response comment: ");
        String comment = scanner.nextLine();
        if (manager.commentRequest(requestId, comment)) {
            System.out.println("Comment added.");
        } else {
            System.out.println("Could not add comment.");
        }
    }

    private void removeRequest(Manager manager) {
        printRequests(manager.showRequests());
        System.out.print("Request id: ");
        long requestId = readLong();
        System.out.println(manager.removeRequest(requestId) ? "Request removed." : "Could not remove request.");
    }

    private boolean canCurrentUserSubmitComplaint() {
        return currentUser instanceof Student || currentUser instanceof Teacher;
    }

    private void createCourseForRegistration() {
        if (!(currentUser instanceof Manager manager)) {
            return;
        }
        System.out.print("Course name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Course code: ");
        String code = scanner.nextLine().trim();
        System.out.print("Credits: ");
        int credits = readInt();
        System.out.print("Target major: ");
        String major = scanner.nextLine().trim();
        System.out.print("Target year: ");
        int year = readInt();

        if (manager.createCourseForRegistration(name, code, credits,
                major.isBlank() ? null : major,
                year > 0 ? year : null)) {
            System.out.println("Course created.");
        } else {
            System.out.println("Could not create course.");
        }
    }

    private void generateAcademicPerformanceReport() {
        if (!(currentUser instanceof Manager manager)) {
            return;
        }
        Report report = manager.generateAcademicPerformanceReport();
        System.out.println(report.getData());
    }

    private void manageNewsMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nNews menu");
            System.out.println("1. Broadcast news");
            System.out.println("2. View my inbox news");
            System.out.println("0. Back");
            int choice = readInt();
            switch (choice) {
                case 1 -> broadcastNews();
                case 2 -> viewInboxNews();
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void broadcastNews() {
        if (!(currentUser instanceof Manager manager)) {
            return;
        }
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Content: ");
        String content = scanner.nextLine().trim();
        System.out.println(manager.broadcastNews(title, content) ? "News broadcasted." : "Could not broadcast news.");
    }

    private void viewPeopleMenu() {
        if (!(currentUser instanceof Manager manager)) {
            return;
        }
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nPeople view");
            System.out.println("1. Students by GPA");
            System.out.println("2. Students alphabetically");
            System.out.println("3. Teachers alphabetically");
            System.out.println("4. Teachers by h-index");
            System.out.println("0. Back");
            int choice = readInt();
            switch (choice) {
                case 1 -> manager.getStudentsByGpa().forEach(student -> System.out.println(student + ", transcriptGpa="
                        + String.format(Locale.US, "%.2f", student.getTranscript().calculateGpa())));
                case 2 -> manager.getStudentsAlphabetically().forEach(System.out::println);
                case 3 -> manager.getTeachersAlphabetically().forEach(System.out::println);
                case 4 -> manager.getTeachersByHIndex().forEach(teacher -> System.out.println(teacher + ", h-index=" + teacher.calculateHIndex()));
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void assignTeacherToCourse() {
        if (!(currentUser instanceof Manager manager)) {
            return;
        }
        university.getTeachers().forEach(System.out::println);
        System.out.print("Teacher id: ");
        int teacherId = readInt();
        User user = university.findUserById(teacherId).orElse(null);
        if (!(user instanceof Teacher teacher)) {
            System.out.println("Teacher not found.");
            return;
        }
        Course course = askCourse();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        if (manager.assignTeacherToCourse(teacher, course)) {
            System.out.println("Teacher assigned.");
        } else {
            System.out.println("Could not assign teacher.");
        }
    }

    private void setCourseOpen(boolean open) {
        if (!(currentUser instanceof Manager manager)) {
            return;
        }
        Course course = askCourse();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        boolean changed = manager.setCourseRegistrationStatus(course, open);
        System.out.println(changed ? "Course updated." : "Could not update course.");
    }

    private void manageCourseMenu() {
        Course course = currentUser instanceof Teacher teacher ? askTeacherCourse(teacher) : askCourse();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        if (!canManageCourse(course)) {
            System.out.println("You do not have permission to manage this course.");
            return;
        }

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nManage course: " + course);
            System.out.println("1. View instructors");
            System.out.println("2. View enrollments");
            System.out.println("3. Add lesson");
            System.out.println("4. Add material");
            System.out.println("0. Back");
            int choice = readInt();
            switch (choice) {
                case 1 -> viewCourseInstructors(course);
                case 2 -> viewCourseEnrollments(course);
                case 3 -> addLessonToCourse(course);
                case 4 -> addMaterialToCourse(course);
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private boolean canManageCourse(Course course) {
        if (currentUser instanceof Admin || currentUser instanceof Manager) {
            return true;
        }
        if (currentUser instanceof Teacher teacher) {
            return teacher.teachesCourse(course);
        }
        return false;
    }

    private void viewCourseInstructors(Course course) {
        if (course.getInstructors().isEmpty()) {
            System.out.println("No instructors assigned.");
            return;
        }
        course.getInstructors().forEach(System.out::println);
    }

    private void viewCourseEnrollments(Course course) {
        if (course.getEnrollments().isEmpty()) {
            System.out.println("No enrollments yet.");
            return;
        }
        for (Enrollment enrollment : course.getEnrollments()) {
            String marksText = enrollment.getMarks() == null ? "not graded" : enrollment.getMarks().toString();
            System.out.println(enrollment.getStudent() + " -> " + marksText);
        }
    }

    private void addLessonToCourse(Course course) {
        LessonType lessonType = readLessonType();
        DaysOfWeek day = readDayOfWeek();
        LocalTime startTime = readTime("Start time");
        LocalTime endTime = readTime("End time");
        if (lessonType == null || day == null || startTime == null || endTime == null) {
            System.out.println("Lesson creation cancelled.");
            return;
        }
        if (!endTime.isAfter(startTime)) {
            System.out.println("End time must be after start time.");
            return;
        }
        System.out.print("Room: ");
        String room = scanner.nextLine();
        Lesson lesson = new Lesson(new TimeSlot(day, startTime, endTime), room, lessonType);
        boolean added;
        if (currentUser instanceof Teacher teacher) {
            added = teacher.addLessonToCourse(course, lesson);
        } else {
            course.addLesson(lesson);
            university.addLog(currentUser, "LESSON_ADDED " + course.getCode());
            added = true;
        }
        System.out.println(added ? "Lesson added." : "Could not add lesson.");
    }

    private void addMaterialToCourse(Course course) {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("File name: ");
        String fileName = scanner.nextLine();
        System.out.print("Is this a task? (y/n): ");
        boolean task = scanner.nextLine().trim().equalsIgnoreCase("y");
        LocalDate deadline = task ? readDate("Deadline") : null;
        if (task && deadline == null) {
            System.out.println("Material creation cancelled.");
            return;
        }

        Teacher uploadedBy = currentUser instanceof Teacher teacher ? teacher : null;
        StudyMaterial material;
        try {
            material = StudyMaterial.builder()
                    .title(title)
                    .description(description)
                    .fileName(fileName)
                    .deadline(deadline)
                    .task(task)
                    .uploadedBy(uploadedBy)
                    .course(course)
                    .build();
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
            return;
        }
        boolean added;
        if (currentUser instanceof Teacher teacher) {
            added = teacher.uploadMaterial(material, course);
        } else {
            course.addMaterial(material);
            university.addLog(currentUser, "MATERIAL_ADDED " + course.getCode() + " " + title);
            added = true;
        }
        System.out.println(added ? "Material added." : "Could not add material.");
    }

    private LessonType readLessonType() {
        LessonType[] lessonTypes = LessonType.values();
        System.out.println("Lesson type:");
        for (int i = 0; i < lessonTypes.length; i++) {
            System.out.println((i + 1) + ". " + lessonTypes[i]);
        }
        int choice = readInt();
        if (choice < 1 || choice > lessonTypes.length) {
            System.out.println("Unknown lesson type.");
            return null;
        }
        return lessonTypes[choice - 1];
    }

    private DaysOfWeek readDayOfWeek() {
        DaysOfWeek[] days = DaysOfWeek.values();
        System.out.println("Day of week:");
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }
        int choice = readInt();
        if (choice < 1 || choice > days.length) {
            System.out.println("Unknown day.");
            return null;
        }
        return days[choice - 1];
    }

    private TeacherType readTeacherTypeOption() {
        TeacherType[] teacherTypes = TeacherType.values();
        System.out.println("Teacher title:");
        for (int i = 0; i < teacherTypes.length; i++) {
            System.out.println((i + 1) + ". " + teacherTypes[i]);
        }
        int choice = readInt();
        if (choice < 1 || choice > teacherTypes.length) {
            return TeacherType.TUTOR;
        }
        return teacherTypes[choice - 1];
    }

    private ManagerType readManagerTypeOption() {
        ManagerType[] managerTypes = ManagerType.values();
        System.out.println("Manager type:");
        for (int i = 0; i < managerTypes.length; i++) {
            System.out.println((i + 1) + ". " + managerTypes[i]);
        }
        int choice = readInt();
        if (choice < 1 || choice > managerTypes.length) {
            return ManagerType.OR_MANAGER;
        }
        return managerTypes[choice - 1];
    }

    private LocalTime readTime(String label) {
        System.out.print(label + " (HH:mm): ");
        try {
            return LocalTime.parse(scanner.nextLine().trim());
        } catch (DateTimeParseException exception) {
            System.out.println("Invalid time format.");
            return null;
        }
    }

    private LocalDate readDate(String label) {
        System.out.print(label + " (YYYY-MM-DD): ");
        try {
            return LocalDate.parse(scanner.nextLine().trim());
        } catch (DateTimeParseException exception) {
            System.out.println("Invalid date format.");
            return null;
        }
    }

    private Course askCourse() {
        showCourses();
        System.out.print("Course code: ");
        String courseCode = scanner.nextLine();
        return university.findCourseByCode(courseCode).orElse(null);
    }

    private Course askStudentCourse(Student student) {
        if (student == null || student.getRegisteredCourses().isEmpty()) {
            return null;
        }
        student.getRegisteredCourses().forEach(System.out::println);
        System.out.print("Course code: ");
        String courseCode = scanner.nextLine();
        return student.findRegisteredCourseByCode(courseCode);
    }

    private Course askTeacherCourse(Teacher teacher) {
        if (teacher == null || teacher.getAssignedCourses().isEmpty()) {
            return null;
        }
        teacher.getAssignedCourses().forEach(System.out::println);
        System.out.print("Course code: ");
        String courseCode = scanner.nextLine();
        return teacher.findAssignedCourseByCode(courseCode);
    }

    private void showResearchers() {
        university.getResearchers().stream()
                .filter(Researcher::isResearcher)
                .sorted(Comparator.comparing(Researcher::getResearcherName))
                .forEach(this::printResearcherSummary);
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

    private long readLong() {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException exception) {
            return -1L;
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
        admin.addUser(new Admin(1, "admin", "admin", "admin@university.kz"));
        admin.addUser(new Student(2, "student", "student", "student@university.kz", 3.4, 4, 0, 0));

        Teacher professor = new Teacher(3, "professor", "professor", "professor@university.kz",
                500000, LocalDate.now(), TeacherType.PROFESSOR);
        professor.becomeResearcher("SITE");
        admin.addUser(professor);

        admin.addUser(new Manager(4, "manager", "manager", "manager@university.kz",
                400000, LocalDate.now(), ManagerType.OR_MANAGER));

        admin.addUser(new Teacher(5, "tutor", "tutor", "tutor@university.kz",
                280000, LocalDate.now(), TeacherType.TUTOR));

        Employee analyst = new Employee(6, "analyst", "analyst", "analyst@university.kz",
                320000, LocalDate.now(), "Research Office");
        analyst.becomeResearcher("Research Office");
        admin.addUser(analyst);
    }

    private void seedDemoData() {
        if (!university.getCourses().isEmpty()) {
            return;
        }

        Course oop = new Course("Object Oriented Programming", "OOP101", 5);
        Course researchMethods = new Course("Research Methods", "RES201", 4);
        oop.setOpen(true);
        researchMethods.setOpen(true);
        admin.addCourse(oop);
        admin.addCourse(researchMethods);
        university.assignTeacherToCourse(3, "OOP101");
        university.assignTeacherToCourse(3, "RES201");
        university.assignTeacherToCourse(5, "OOP101");

        Teacher professor = (Teacher) university.findUserByUsername("professor").orElse(null);
        if (professor == null) {
            return;
        }
        professor.addResearchPaper(ResearchPaper.builder()
                .title("AI in Education")
                .journal("IEEE Access")
                .pages(12)
                .citations(20)
                .publishDate(LocalDate.of(2025, 3, 12))
                .doi("10.1000/ai-edu")
                .build());
        professor.addResearchPaper(ResearchPaper.builder()
                .title("Research Systems")
                .journal("Springer")
                .pages(8)
                .citations(8)
                .publishDate(LocalDate.of(2024, 9, 20))
                .doi("10.1000/research-systems")
                .build());
        professor.addResearchPaper(ResearchPaper.builder()
                .title("Learning Analytics")
                .journal("ACM")
                .pages(10)
                .citations(4)
                .publishDate(LocalDate.of(2026, 1, 15))
                .doi("10.1000/learning-analytics")
                .build());

        ResearchProject project = new ResearchProject("Digital University Research");
        try {
            professor.joinResearchProject(project);
        } catch (NonResearcherException exception) {
            university.addLog(professor, "PROJECT_JOIN_FAILED " + exception.getMessage());
        }
        for (ResearchPaper paper : professor.getResearchPapers()) {
            project.addPaper(paper);
        }
        university.addProject(project);

        Employee analyst = (Employee) university.findUserByUsername("analyst").orElse(null);
        if (analyst != null) {
            analyst.addResearchPaper(ResearchPaper.builder()
                    .title("Institutional Data Insights")
                    .journal("Elsevier")
                    .publisher("Elsevier")
                    .pages(14)
                    .citations(11)
                    .publishDate(LocalDate.of(2023, 6, 3))
                    .doi("10.1000/data-insights")
                    .keywords("analytics, dashboards")
                    .build());
            try {
                analyst.joinResearchProject(project);
            } catch (NonResearcherException exception) {
                university.addLog(analyst, "PROJECT_JOIN_FAILED " + exception.getMessage());
            }
        }
    }
}
