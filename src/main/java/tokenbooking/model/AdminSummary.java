package tokenbooking.model;

import java.util.List;

public class AdminSummary {
    private Long clientId;
    private String clientName;
    private List<AdminSessionSummary> adminSessionSummaryList;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
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
