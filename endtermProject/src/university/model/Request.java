package endtermProject.src.university.model;

import endtermProject.src.university.enums.RequestStatus;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int sequence = 1;

    private final int requestId;
    private final String description;
    private RequestStatus status;

    public Request(String description) {
        this.requestId = sequence++;
        this.description = description;
        this.status = RequestStatus.NEW;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void approve() {
        status = RequestStatus.APPROVED;
    }

    public void reject() {
        status = RequestStatus.REJECTED;
    }
}
