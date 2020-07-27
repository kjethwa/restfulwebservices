package tokenbooking.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class ClientConfigurationSetting {

    @Id
    @GeneratedValue()
    private UUID id;

    @Basic
    private Integer noOfTokensPerQuarter;

    @OneToOne(mappedBy = "clientConfigurationSetting")
    private Client client;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getNoOfTokensPerQuarter() {
        return noOfTokensPerQuarter;
    }

    public void setNoOfTokensPerQuarter(Integer noOfTokensPerQuarter) {
        this.noOfTokensPerQuarter = noOfTokensPerQuarter;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
