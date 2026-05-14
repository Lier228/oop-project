package oopproject.core;

import java.time.LocalDate;
import java.util.Scanner;
import oopproject.enums.ManagerType;
import oopproject.enums.TeacherType;
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

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1 -> loginMenu();
                case 2 -> printUniversitySummary();
                case 3 -> saveState();
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
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> university.getUsers().forEach(System.out::println);
                case 2 -> university.getLogs().forEach(System.out::println);
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
            System.out.println("1. View transcript");
            System.out.println("2. View schedule");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> student.viewTranscript().forEach(System.out::println);
                case 2 -> student.viewSchedule().getScheduleEntries().forEach(System.out::println);
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
            System.out.println("1. View research papers");
            System.out.println("2. View h-index");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> teacher.printPapers(null);
                case 2 -> System.out.println("h-index: " + teacher.calculateHIndex());
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
            System.out.println("2. Show researchers");
            System.out.println("0. Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> university.getCourses().forEach(System.out::println);
                case 2 -> university.getResearchers().forEach(System.out::println);
                case 0 -> inMenu = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n" + university.getName());
        System.out.println("1. Login");
        System.out.println("2. University summary");
        System.out.println("3. Save state");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    private void printUniversitySummary() {
        System.out.println("Users: " + university.getUsers().size());
        System.out.println("Courses: " + university.getCourses().size());
        System.out.println("Researchers: " + university.getResearchers().size());
        System.out.println("Research projects: " + university.getProjects().size());
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
        university.addResearcher(professor);

        university.addUser(new Manager(4, "manager", "manager", "manager@university.kz",
                400000, LocalDate.now(), ManagerType.OR_MANAGER));
    }
}
