package oopproject.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.academic.Lesson;
import oopproject.academic.Marks;
import oopproject.academic.Request;
import oopproject.enums.ManagerType;
import oopproject.enums.DaysOfWeek;
import oopproject.enums.LessonType;
import oopproject.enums.RequestStatus;
import oopproject.enums.RequestType;
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
import oopproject.academic.StudyMaterial;
import oopproject.academic.TimeSlot;

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
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nAdmin menu");
            System.out.println("1. Show users");
            System.out.println("2. Show logs");
            System.out.println("3. Show courses");
            System.out.println("4. Save state");
            System.out.println("5. Block user");
            System.out.println("6. Unblock user");
            System.out.println("7. Communication");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> university.getUsers().forEach(System.out::println);
                case 2 -> university.getLogs().forEach(System.out::println);
                case 3 -> showCourses();
                case 4 -> saveState();
                case 5 -> setUserActive(false);
                case 6 -> setUserActive(true);
                case 7 -> communicationMenu();
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
            System.out.println("7. Communication");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> showOpenCourses();
                case 2 -> registerCurrentStudent(student);
                case 3 -> System.out.println(student.getTranscript());
                case 4 -> university.getCoursesByStudent(student).forEach(System.out::println);
                case 5 -> assignSupervisor(student);
                case 6 -> printSupervisor(student);
                case 7 -> communicationMenu();
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
            System.out.println("7. Communication");
            System.out.println("8. Manage course");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> university.getCoursesByTeacher(teacher).forEach(System.out::println);
                case 2 -> showStudentsByCourse();
                case 3 -> putMark(teacher);
                case 4 -> teacher.printPapers(ResearchPaperComparators.BY_CITATIONS_DESC);
                case 5 -> teacher.printPapers(ResearchPaperComparators.BY_DATE_PUBLISHED_DESC);
                case 6 -> System.out.println("h-index: " + teacher.calculateHIndex());
                case 7 -> communicationMenu();
                case 8 -> manageCourseMenu();
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
            System.out.println("7. Review requests");
            System.out.println("8. Communication");
            System.out.println("9. Manage course");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> showCourses();
                case 2 -> setCourseOpen(true);
                case 3 -> setCourseOpen(false);
                case 4 -> assignTeacherToCourse();
                case 5 -> showResearchers();
                case 6 -> printTopCitedResearcher();
                case 7 -> requestReviewMenu();
                case 8 -> communicationMenu();
                case 9 -> manageCourseMenu();
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
        List<Student> students = course.getEnrollments().stream()
                .map(Enrollment::getStudent)
                .toList();
        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
            return;
        }
        students.forEach(System.out::println);
    }

    private void putMark(Teacher teacher) {
        Course course = askCourse();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        List<Student> students = course.getEnrollments().stream()
                .map(Enrollment::getStudent)
                .toList();
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
                case 2 -> printRequests(manager.showRequests().stream()
                        .filter(request -> request.getStatus() == RequestStatus.PENDING)
                        .toList());
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
        Request request = manager.findRequestById(requestId);
        if (request == null) {
            System.out.println("Request not found.");
            return;
        }
        if (manager.approveRequest(request)) {
            System.out.println("Request approved.");
        } else {
            System.out.println("Could not approve request.");
        }
    }

    private void rejectRequest(Manager manager) {
        printRequests(manager.showRequests());
        System.out.print("Request id: ");
        long requestId = readLong();
        Request request = manager.findRequestById(requestId);
        if (request == null) {
            System.out.println("Request not found.");
            return;
        }
        if (manager.rejectRequest(request)) {
            System.out.println("Request rejected.");
        } else {
            System.out.println("Could not reject request.");
        }
    }

    private void commentRequest(Manager manager) {
        printRequests(manager.showRequests());
        System.out.print("Request id: ");
        long requestId = readLong();
        Request request = manager.findRequestById(requestId);
        if (request == null) {
            System.out.println("Request not found.");
            return;
        }
        System.out.print("Response comment: ");
        String comment = scanner.nextLine();
        if (manager.commentRequest(request, comment)) {
            System.out.println("Comment added.");
        } else {
            System.out.println("Could not add comment.");
        }
    }

    private void removeRequest(Manager manager) {
        printRequests(manager.showRequests());
        System.out.print("Request id: ");
        long requestId = readLong();
        if (manager.findRequestById(requestId) == null) {
            System.out.println("Request not found.");
            return;
        }
        manager.removeRequest(requestId);
        System.out.println(manager.findRequestById(requestId) == null ? "Request removed." : "Could not remove request.");
    }

    private boolean canCurrentUserSubmitComplaint() {
        return currentUser instanceof Student || currentUser instanceof Teacher;
    }

    private void manageCourseMenu() {
        Course course = askCourse();
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
        if (currentUser instanceof Admin || currentUser instanceof Manager || (currentUser instanceof Teacher && course.getInstructors().contains((Teacher) currentUser))) {
            return true;
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
        course.addLesson(new Lesson(new TimeSlot(day, startTime, endTime), room, lessonType));
        university.addLog(currentUser, "LESSON_ADDED " + course.getCode());
        System.out.println("Lesson added.");
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
        course.addMaterial(new StudyMaterial(title, description, fileName, deadline, task, uploadedBy, course));
        university.addLog(currentUser, "MATERIAL_ADDED " + course.getCode() + " " + title);
        System.out.println("Material added.");
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
