package oopproject.academic;

import oopproject.enums.RequestStatus;
import oopproject.enums.RequestType;

public class Request {
    private static long requestIdCounter = 1;

    private int senderId;
    private RequestType type;
    private RequestStatus status;
    private String description;
    private long requestId;

    public Request() {
        this.requestId = requestIdCounter++;
        this.status = RequestStatus.PENDING;
    }

    public Request(int senderId, RequestType type, RequestStatus status, String description) {
        this.requestId = requestIdCounter++;
        this.senderId = senderId;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public void approve() {
        status = RequestStatus.APPROVED;
    }

    public void reject() {
        status = RequestStatus.REJECTED;
    }

    public long getRequestId() {
        return requestId;
    }

    public RequestStatus getStatus() {
        return status;
    }
}
