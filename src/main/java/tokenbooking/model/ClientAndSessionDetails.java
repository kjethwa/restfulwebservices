package tokenbooking.model;

import tokenbooking.repository.ClientIdNameAddress;

import java.util.List;

public class ClientAndSessionDetails {

    private ClientIdNameAddress clientIdNameAddress;

    private List<UserSessionSummary> sessions;

    public ClientIdNameAddress getClientIdNameAddress() {
        return clientIdNameAddress;
    }

    public void setClientIdNameAddress(ClientIdNameAddress clientIdNameAddress) {
        this.clientIdNameAddress = clientIdNameAddress;
    }

    public List<UserSessionSummary> getSessions() {
        return sessions;
    }

    public void setSessions(List<UserSessionSummary> sessions) {
        this.sessions = sessions;
    }
}
