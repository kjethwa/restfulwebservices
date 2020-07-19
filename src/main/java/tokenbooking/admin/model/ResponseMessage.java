package tokenbooking.admin.model;

public class ResponseMessage<T> {
    T message;
    ResponseStatus status;
    String errorMessage;

    public ResponseMessage(T message, ResponseStatus status) {
        this.message = message;
        this.status = status;
    }

    public ResponseMessage(T message, String errorMessage, ResponseStatus status) {
        this.message = message;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
