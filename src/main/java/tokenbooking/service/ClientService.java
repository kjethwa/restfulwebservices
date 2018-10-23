package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenbooking.model.Client;
import tokenbooking.repository.ClientRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(final Long clientId) {
        return clientRepository.findOne(clientId);
    }

    public void addClient(Client client) {
        clientRepository.save(client);
    }

    public void updateClient(Client client) {
        clientRepository.save(client);
    }
}
