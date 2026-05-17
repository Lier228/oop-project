package oopproject.users;

import java.time.LocalDate;
import oopproject.enums.UserType;

public class Employee extends User {
    protected double salary;
    protected LocalDate hireDate;

    public Employee() {
        this.role = UserType.EMPLOYEE;
    }

    public Employee(int id, String username, String password, String email, double salary, LocalDate hireDate) {
        super(id, username, password, email, UserType.EMPLOYEE);
        this.salary = salary;
        this.hireDate = hireDate;
    }
}
