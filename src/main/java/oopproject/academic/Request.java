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
    private long requestId;

    public Request() {
        this.requestId = requestIdCounter++;
        this.status = RequestStatus.PENDING;
    }

    public Request(int senderId, RequestType type, RequestStatus status, String description) {
        this.requestId = requestIdCounter++;
        this.senderId = senderId;
        this.type = type;
        this.status = status == null ? RequestStatus.PENDING : status;
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

    public int getSenderId() {
        return senderId;
    }

    public RequestType getType() {
        return type;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static void ensureNextIdAbove(long requestId) {
        if (requestIdCounter <= requestId) {
            requestIdCounter = requestId + 1;
        }
    }

    @Override
    public String toString() {
        return "Request{id=" + requestId
                + ", senderId=" + senderId
                + ", type=" + type
                + ", status=" + status
                + ", description='" + description + "'}";
    }
}
