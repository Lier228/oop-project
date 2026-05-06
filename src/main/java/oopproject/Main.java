package oopproject;

import oopproject.core.University;

public class Main {
    public static void main(String[] args) {
        University university = University.getInstance();
        System.out.println("University model initialized. Users: " + university.getUsers().size());
    }
}
