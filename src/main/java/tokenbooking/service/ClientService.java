package tokenbooking.service;

import org.springframework.stereotype.Service;
import tokenbooking.model.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClientService {

    private List<Client> allclients = new ArrayList<>(Arrays.asList(new Client("123", "Dr. Mehta"), new Client("124", "Mawali bhai dosa")));

    public List<Client> getAllClients() {
        return allclients;
    }

    public Client getClientById(final String clientId) {
        return allclients.stream().filter(c -> c.getClientId().equals(clientId)).findAny().get();
    }

    public void addClient(Client client) {
        allclients.add(client);
    }
}
