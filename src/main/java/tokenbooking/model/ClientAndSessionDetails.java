package tokenbooking.model;

import tokenbooking.repository.ClientIdNameAddress;

import java.util.List;

public class ClientAndSessionDetails {

    private ClientIdNameAddress clientIdNameAddress;

    private List<SessionDetails> sessions;

    public ClientIdNameAddress getClientIdNameAddress() {
        return clientIdNameAddress;
    }

    public void setClientIdNameAddress(ClientIdNameAddress clientIdNameAddress) {
        this.clientIdNameAddress = clientIdNameAddress;
    }

    public List<SessionDetails> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionDetails> sessions) {
        this.sessions = sessions;
    }
}
