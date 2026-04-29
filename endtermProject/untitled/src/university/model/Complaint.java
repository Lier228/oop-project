package university.model;

import university.enums.ComplaintStatus;

import java.io.Serializable;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String text;
    private ComplaintStatus status;

    public Complaint(String text) {
        this.text = text;
        this.status = ComplaintStatus.OPEN;
    }

    public String getText() {
        return text;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void resolve() {
        status = ComplaintStatus.RESOLVED;
    }
}
