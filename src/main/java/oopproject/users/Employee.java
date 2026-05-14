package oopproject.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oopproject.academic.Request;
import oopproject.enums.UserType;

public class Employee extends User {
    protected double salary;
    protected LocalDate hireDate;
    protected List<Request> requests = new ArrayList<>();

    public Employee() {
        this.role = UserType.EMPLOYEE;
    }

    public Employee(int id, String username, String password, String email, double salary, LocalDate hireDate) {
        super(id, username, password, email, UserType.EMPLOYEE);
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public List<Request> getRequestInstance() {
        return Collections.unmodifiableList(requests);
    }

    public void setRequestInstance(List<Request> loadedRequests) {
        requests = new ArrayList<>(loadedRequests);
    }

    public void addRequest() {
        requests.add(new Request());
    }

    public void addRequest(Request request) {
        requests.add(request);
    }
}
