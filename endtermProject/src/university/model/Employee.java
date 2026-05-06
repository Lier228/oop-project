package endtermProject.src.university.model;

import endtermProject.src.university.service.LogManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Employee extends User implements Researcher {
    private static final long serialVersionUID = 1L;

    private double salary;
    private String department;
    private final List<Message> inbox = new ArrayList<>();
    private final List<Request> requests = new ArrayList<>();
    private final List<Complaint> complaints = new ArrayList<>();
    private final ResearchProfile researchProfile = new ResearchProfile();

    protected Employee(
            String id,
            String username,
            String password,
            String name,
            String surname,
            String email,
            double salary,
            String department
    ) {
        super(id, username, password, name, surname, email);
        this.salary = salary;
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Message> getInbox() {
        return Collections.unmodifiableList(inbox);
    }

    public List<Request> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public List<Complaint> getComplaints() {
        return Collections.unmodifiableList(complaints);
    }

    public void sendMessage(Employee receiver, Message message) {
        if (receiver == null || message == null) {
            return;
        }
        receiver.receiveMessage(message);
        LogManager.getInstance().logAction(this, "sent a message to " + receiver.getFullName());
    }

    public void sendRequest(Request request) {
        if (request != null) {
            requests.add(request);
            LogManager.getInstance().logAction(this, "created request #" + request.getRequestId());
        }
    }

    public void sendComplaint(Complaint complaint) {
        if (complaint != null) {
            complaints.add(complaint);
            LogManager.getInstance().logAction(this, "submitted a complaint");
        }
    }

    protected void receiveMessage(Message message) {
        inbox.add(message);
    }

    @Override
    public ResearchProfile getResearchProfile() {
        return researchProfile;
    }

    @Override
    public String getResearcherName() {
        return getFullName();
    }
}
