package oopproject.academic;

import java.io.Serializable;
import oopproject.enums.RequestStatus;
import oopproject.enums.RequestType;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private static long requestIdCounter = 1;

    private int senderId;
    private RequestType type;
    private RequestStatus status;
    private String description;
    private String responseComment;
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

    public int getSenderId() {
        return senderId;
    }

    public RequestType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getResponseComment() {
        return responseComment;
    }

    public void setResponseComment(String responseComment) {
        this.responseComment = responseComment;
    }
}
