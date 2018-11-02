package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tokenbooking.model.Client;
import tokenbooking.model.ClientSearchDetails;
import tokenbooking.repository.ClientRepository;
import tokenbooking.specification.UserSpecificationsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public List<ClientSearchDetails> getClientSearchResult(String search) {
        UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Client> spec = builder.build();
        List<Client> listOfClient = clientRepository.findAll(spec);
        return getClientSearchDetailsResult(listOfClient);
    }

    private List<ClientSearchDetails> getClientSearchDetailsResult(List<Client> listOfClient) {
        List<ClientSearchDetails> resultClientSearch = new ArrayList<>(listOfClient.size());
        for (Client client : listOfClient) {
            ClientSearchDetails temp = new ClientSearchDetails(client.getClientId(), client.getClientCategory(), client.getClientName(), client.getOwnerFirstName(), client.getOwnerLastName(), client.getCity(), client.getState(), client.getStatus());
            resultClientSearch.add(temp);
        }
        return resultClientSearch;
    }

}
