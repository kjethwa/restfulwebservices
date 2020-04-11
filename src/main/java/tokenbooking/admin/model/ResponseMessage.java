package tokenbooking.admin.model;

public class ResponseMessage {
    Object message;
    ResponseStatus status;

    public ResponseMessage(Object message, ResponseStatus status) {
        this.message = message;
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}
