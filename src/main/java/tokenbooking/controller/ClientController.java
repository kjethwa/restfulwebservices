package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.Client;
import tokenbooking.repository.ClientNameAndId;
import tokenbooking.service.ClientService;

import java.util.List;
import java.util.UUID;

@RestController("/mdmapi")
public class ClientController {

    @Autowired
    ClientService clientService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients/{clientId}", method = RequestMethod.GET)
    public Client getClientById(@PathVariable UUID clientId) {
        return clientService.getClientById(clientId);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public void addClient(@RequestBody Client client) {
        clientService.addClient(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients", method = RequestMethod.PUT)
    public void updateClient(@RequestBody Client client) {
        clientService.updateClient(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clientname", method = RequestMethod.GET)
    public List<ClientNameAndId> getListOfAllActiveClient() {
        return clientService.getListOfAllActiveClients();
    }

}
