package oopproject.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oopproject.academic.Request;
import oopproject.enums.RequestStatus;
import oopproject.enums.RequestType;

public class Employee extends User {
    protected double salary;
    protected LocalDate hireDate;
    protected boolean active=false;
    protected String department;
    protected List<Request> requests = new ArrayList<>();

    public Employee() {
    }

    public Employee(int id, String username, String password, String email, double salary, LocalDate hireDate, String department) {
        super(id, username, password, email);
        this.salary = salary;
        this.hireDate = hireDate;
        this.department = department;
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

    public void changeActive() {
        if (active) {
            this.active=false;
        }
        else{
            this.active=true;
        }
    }
}
