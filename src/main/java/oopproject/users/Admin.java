package oopproject.users;

import java.util.Scanner;
import oopproject.core.University;
import oopproject.enums.UserType;

public class Admin extends User {
    private final Scanner sc = new Scanner(System.in);

    public Admin() {

    }

    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email);
    }

    public void addUser() {
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.println("Type: 1-Admin 2-Student 3-Teacher 4-Manager");
        int typeChoice = Integer.parseInt(sc.nextLine());
        UserType type = UserType.STUDENT;
        if(typeChoice == 1) type = UserType.ADMIN;
        if(typeChoice == 3) type = UserType.TEACHER;
        if(typeChoice == 4) type = UserType.MANAGER;

        User user = UserFactory.createUser(type, 100 + University.getInstance().users.size(), username, password, email);
        University.getInstance().users.add(user);
        System.out.println("User added: " + user.username);
    }

    public void removeUser() {

        System.out.print("Username: ");
        String username = sc.nextLine();

        University uni = University.getInstance();

        User target = null;

        for (User u : uni.users) {
            if (u.username.equals(username)) {
                target = u;
                break;
            }
        }

        if (target == null) {
            System.out.println("User not found");
            return;
        }

        uni.users.remove(target);

        System.out.println("User removed: " + target);
    }

    public void updateLogs() {
        System.out.println("Logs updated");
    }

    public void blockUser(User user) {
        System.out.println(user.username + " blocked");
    }

    public void assignRole(User user, UserType role) {
        System.out.println("Role assigned to " + user.username);
    }
}

//
