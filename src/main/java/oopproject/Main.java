package oopproject;

import oopproject.core.ConsoleUIController;
import oopproject.core.University;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "--console".equals(args[0]) || true) {
            new ConsoleUIController().start();
            return;
        }

        University university = University.getInstance();
        System.out.println("University model initialized. Users: " + university.getUsers().size());
        System.out.println("Run with --console to open the console demo.");
    }
}
