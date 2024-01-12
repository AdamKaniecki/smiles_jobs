package pl.zajavka.infrastructure.database.entity;

public enum Status {
    UNDER_REVIEW("Under Review"),
    MEETING_SCHEDULING("Meeting scheduling"),
    WAITING_FOR_INTERVIEW("Waiting for Interview"),

    PENDING_DECISION("Pending Decision"),
    REJECT("Reject"),
    HIRED("Hired");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
