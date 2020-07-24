package tokenbooking.model;

import java.util.List;
import java.util.UUID;

public class AdminSummary {
    private UUID clientId;
    private String clientName;
    private List<AdminSessionSummary> adminSessionSummaryList;

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<AdminSessionSummary> getAdminSessionSummaryList() {
        return adminSessionSummaryList;
    }

    public void setAdminSessionSummaryList(List<AdminSessionSummary> adminSessionSummaryList) {
        this.adminSessionSummaryList = adminSessionSummaryList;
    }
}
