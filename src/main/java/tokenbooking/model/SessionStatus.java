package tokenbooking.model;

public enum SessionStatus {
    ACTIVE("ACTIVE"),
    INPROGRESS("INPROGRESS"),
    CANCELLED("CANCELLED"),
    FINISHED("FINISHED");

    private String value;

    SessionStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
