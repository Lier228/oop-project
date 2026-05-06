package oopproject.core;

import java.util.Scanner;
import oopproject.users.User;

public class ConsoleUIController {
    private final Scanner scanner = new Scanner(System.in);
    private final University university = University.getInstance();
    private User currentUser;

    public void start() {
    }

    public void loginMenu() {
    }

    public void roleMenu() {
    }

    public void adminMenu() {
    }

    public void studentMenu() {
    }

    public void teacherMenu() {
    }

    public void managerMenu() {
    }

    private int readInt() {
        return Integer.parseInt(scanner.nextLine());
    }

    private void seedUsers() {
    }
}
